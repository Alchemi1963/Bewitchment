package com.bewitchment.common.entity.living.animals;

import com.bewitchment.common.entity.spirits.demons.EntityUran;
import com.bewitchment.common.lib.LibMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityBlindworm extends EntityAnimal {

	private static final ResourceLocation loot = new ResourceLocation(LibMod.MOD_ID, "entities/blindworm");

	public EntityBlindworm(World worldIn) {
		super(worldIn);
		setSize(1F, .3F);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
	}

	@Override
	public boolean getCanSpawnHere() {
		int i = MathHelper.floor(this.posX);
		int j = MathHelper.floor(this.getEntityBoundingBox().minY);
		int k = MathHelper.floor(this.posZ);
		BlockPos blockpos = new BlockPos(i, j, k);
		Block block = this.world.getBlockState(blockpos.down()).getBlock();
		return block instanceof BlockGrass && this.world.getLight(blockpos) > 8 && super.getCanSpawnHere();
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIWanderAvoidWater(this, 0.2f, 0.00001f));
		this.tasks.addTask(5, new EntityAILookIdle(this));
		this.tasks.addTask(4, new EntityAIWatchClosest2(this, EntityPlayer.class, 5f, 1f));
		this.tasks.addTask(3, new EntityAIMate(this, 1d));
	}

	@Override
	protected ResourceLocation getLootTable() {
		return loot;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.8d);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.55d);
	}

	public void onStruckByLightning(EntityLightningBolt lightningBolt) {
		if (!this.world.isRemote && !this.isDead) {
			EntityUran entityuran = new EntityUran(this.world);
			entityuran.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			entityuran.setNoAI(this.isAIDisabled());

			if (this.hasCustomName()) {
				entityuran.setCustomNameTag(this.getCustomNameTag());
				entityuran.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
			}

			this.world.spawnEntity(entityuran);
			this.setDead();
		}
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 2;
	}

	@Override
	public boolean canMateWith(EntityAnimal otherAnimal) {
		if (otherAnimal == this) {
			return false;
		} else if (!(otherAnimal instanceof EntityBlindworm)) {
			return false;
		} else {
			EntityBlindworm entityblindworm = (EntityBlindworm) otherAnimal;
			return this.isInLove() && entityblindworm.isInLove();
		}
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == Items.SPIDER_EYE;
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return new EntityBlindworm(world);
	}
}
