package com.bewitchment.common.entity.living;

import com.bewitchment.Bewitchment;
import com.bewitchment.Util;
import com.bewitchment.common.entity.spirit.demon.EntitySerpent;
import com.bewitchment.common.entity.util.ModEntityTameable;
import com.bewitchment.registry.ModObjects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

@SuppressWarnings({"ConstantConditions"})
public class EntitySnake extends ModEntityTameable {
	
	private int milkTimer = 0;
	
	public EntitySnake(World world) {
		super(world, new ResourceLocation(Bewitchment.MODID, "entities/snake"), Items.CHICKEN, Items.RABBIT);
		setSize(1, 0.3f);
	}
	
	@Override
	public boolean attackEntityAsMob(Entity entity) {
		if (entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue())) {
			applyEnchantments(this, entity);
			if (entity instanceof EntityLivingBase) ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0, false, false));
		}
		return super.attackEntityAsMob(entity);
	}
	
	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == Items.RABBIT;
	}
	
	@Override
	public boolean isPotionApplicable(PotionEffect effect) {
		return effect.getPotion() != MobEffects.POISON && super.isPotionApplicable(effect);
	}
	
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		if (!world.isRemote && (getAttackTarget() == null || getAttackTarget().isDead || getRevengeTarget() == null || getRevengeTarget().isDead)) {
			ItemStack stack = player.getHeldItem(hand);
			if (stack.getItem() == Items.GLASS_BOTTLE) {
				if (milkTimer == 0 && getRNG().nextBoolean()) {
					if (getGrowingAge() >= 0) {
						world.playSound(null, getPosition(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1, 1);
						Util.giveAndConsumeItem(player, hand, new ItemStack(ModObjects.snake_venom));
						milkTimer = 3600;
						return true;
					}
				}
				else {
					setAttackTarget(player);
					setRevengeTarget(player);
				}
			}
		}
		return super.processInteract(player, hand);
	}
	
	@Override
	public int getMaxSpawnedInChunk() {
		return 2;
	}
	
	@Override
	protected int getSkinTypes() {
		return 6;
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (milkTimer > 0) milkTimer--;
	}
	
	@Override
	public void onStruckByLightning(EntityLightningBolt bolt) {
		Util.convertEntity(this, new EntitySerpent(world));
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.5);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(15);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6);
	}
	
	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, this.aiSit);
		tasks.addTask(2, new EntityAIMate(this, getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() / 2));
		tasks.addTask(2, new EntityAIAttackMelee(this, 0.5, false));
		tasks.addTask(3, new EntityAIWatchClosest2(this, EntityPlayer.class, 5, 1));
		tasks.addTask(3, new EntityAIFollowParent(this, getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
		tasks.addTask(3, new EntityAIWander(this, getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
		this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.3D));
		tasks.addTask(3, new EntityAILookIdle(this));
		tasks.addTask(4, new EntityAIFollowOwner(this, getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue(), 10, 2));
		targetTasks.addTask(0, new EntityAIHurtByTarget(this, true));
		targetTasks.addTask(0, new EntityAIOwnerHurtByTarget(this));
		targetTasks.addTask(1, new EntityAIOwnerHurtTarget(this));
		targetTasks.addTask(2, new EntityAITargetNonTamed<>(this, EntityPlayer.class, false, p -> p.getDistanceSq(this) < 1));
		targetTasks.addTask(3, new EntityAITargetNonTamed<>(this, EntityLivingBase.class, false, e -> e instanceof EntityChicken || e instanceof EntityLizard || e instanceof EntityRabbit || e instanceof EntityParrot || e.getClass().getName().endsWith("Rat")));
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		tag.setInteger("milkTimer", milkTimer);
		super.writeEntityToNBT(tag);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		milkTimer = tag.getInteger("milkTimer");
		super.readEntityFromNBT(tag);
	}
}
