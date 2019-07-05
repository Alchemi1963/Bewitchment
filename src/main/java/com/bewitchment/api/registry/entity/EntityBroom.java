package com.bewitchment.api.registry.entity;

import com.bewitchment.Bewitchment;
import com.bewitchment.api.capability.magicpower.MagicPower;
import com.bewitchment.api.message.ControlBroom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

@SuppressWarnings({"NullableProblems"})
public abstract class EntityBroom extends Entity {
	private ItemStack item;
	private boolean canFly = true;
	
	public EntityBroom(World world) {
		super(world);
		setSize(0.7f, 0.7f);
		setNoGravity(true);
	}
	
	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {
		if (!player.isRiding() && !player.isSneaking()) {
			player.rotationYaw = rotationYaw;
			player.rotationPitch = rotationPitch;
			player.startRiding(this);
			return EnumActionResult.SUCCESS;
		}
		return super.applyPlayerInteraction(player, vec, hand);
	}
	
	@Override
	public Entity getControllingPassenger() {
		return getPassengers().isEmpty() ? null : getPassengers().get(0);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (getControllingPassenger() == null && source.getTrueSource() instanceof EntityPlayer) {
			if (!world.isRemote) InventoryHelper.spawnItemStack(world, posX, posY, posZ, item.copy());
			setDead();
			return true;
		}
		return super.attackEntityFrom(source, amount);
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return !isDead;
	}
	
	@Override
	public boolean canBePushed() {
		return canBeCollidedWith();
	}
	
	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (item == null) {
			item = player.getHeldItem(hand).splitStack(1);
			return true;
		}
		return super.processInitialInteract(player, hand);
	}
	
	@Override
	public double getMountedYOffset() {
		return 0.4;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		Entity rider = getControllingPassenger();
		if (world.isRemote) {
			if (rider instanceof EntityPlayer) {
				rotationYaw = rider.rotationYaw;
				boolean jump = getJump((EntityPlayer) rider);
				if (rider.ticksExisted % 20 == 0 && (!onGround || jump)) canFly = MagicPower.attemptDrain(null, (EntityPlayer) rider, getMagicCost());
				if (canFly) {
					motionX += rider.motionX * getSpeed();
					motionZ += rider.motionZ * getSpeed();
					if (jump && motionY < 1) motionY += (0.1f + getThrust());
				}
			}
			float friction = 0.98f;
			if (onGround) friction = 0.4f;
			motionX *= friction;
			motionZ *= friction;
			if (collidedHorizontally) {
				motionX = 0;
				motionZ = 0;
			}
			motionY -= 0.1f;
			motionY *= 0.75f;
			motionX = MathHelper.clamp(motionX, -getMaxSpeed(), getMaxSpeed());
			motionZ = MathHelper.clamp(motionZ, -getMaxSpeed(), getMaxSpeed());
			Bewitchment.network.sendToServer(new ControlBroom(getEntityId(), motionX, motionY, motionZ));
		}
		move(MoverType.SELF, motionX, motionY, motionZ);
	}
	
	@Override
	protected void updateFallState(double y, boolean onGround, IBlockState state, BlockPos pos) {
	}
	
	@Override
	public void setDead() {
		if (getControllingPassenger() == null) super.setDead();
	}
	
	@Override
	protected void entityInit() {
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound tag) {
		if (item != null) tag.setTag("item", item.serializeNBT());
		tag.setBoolean("canFly", canFly);
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound tag) {
		item = tag.hasKey("item") ? new ItemStack(tag.getCompoundTag("item")) : ItemStack.EMPTY;
		canFly = tag.getBoolean("canFly");
	}
	
	protected abstract float getSpeed();
	
	protected abstract float getMaxSpeed();
	
	protected abstract float getThrust();
	
	protected abstract int getMagicCost();
	
	public void dismount() {
		//		if (!world.isRemote) {
		//			if (getRidingEntity() != null && this.getControllingPassenger() instanceof EntityPlayer) {
		//				EntityPlayer player = (EntityPlayer) getControllingPassenger();
		//				Util.giveItem(player, item.copy());
		//			}
		//			else InventoryHelper.spawnItemStack(world, posX, posY, posZ, item.copy());
		//		}
		//		setDead();
	}
	
	private static boolean getJump(EntityLivingBase rider) throws IllegalArgumentException {
		return ObfuscationReflectionHelper.getPrivateValue(EntityLivingBase.class, rider, "isJumping", "field_70703_bu");
	}
}