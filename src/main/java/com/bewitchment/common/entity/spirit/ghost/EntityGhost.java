package com.bewitchment.common.entity.spirit.ghost;

import com.bewitchment.Bewitchment;
import com.bewitchment.api.BewitchmentAPI;
import com.bewitchment.common.entity.util.ModEntityMob;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by Joseph on 9/18/2019.
 */
public class EntityGhost extends ModEntityMob {
	private boolean charging = false;
	
	public EntityGhost(World world) {
		super(world, new ResourceLocation(Bewitchment.MODID, "entities/ghost"));
		isImmuneToFire = true;
		this.moveHelper = new AIMoveControl(this);
	}
	
	private boolean isCharging() {
		return charging;
	}
	
	private void setCharging(boolean charging) {
		this.charging = charging;
	}
	
	@Override
	protected int getSkinTypes() {
		return 5;
	}
	
	@Override
	protected boolean isValidLightLevel() {
		return !world.isDaytime();
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!world.isRemote && world.isDaytime()) setDead();
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4);
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
		getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.25);
	}
	
	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new AIChargeAttack());
		this.tasks.addTask(3, new AIMoveRandom());
		this.tasks.addTask(3, new EntityAILookIdle(this));
		this.tasks.addTask(2, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
		this.targetTasks.addTask(0, new EntityAIHurtByTarget(this, false, EntityGhost.class));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
	}
	
	@Override
	public boolean isPotionApplicable(PotionEffect effect) {
		return effect.getPotion() != MobEffects.POISON && effect.getPotion() != MobEffects.WITHER && super.isPotionApplicable(effect);
	}
	
	@Override
	public boolean isOnLadder() {
		return false;
	}
	
	public void fall(float distance, float damageMultiplier) {
	}
	
	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return BewitchmentAPI.SPIRIT;
	}
	
	@Override
	public void travel(float strafe, float vertical, float forward) {
		setNoGravity(true);
		noClip = true;
		super.travel(strafe, vertical, forward);
	}
	
	@Override
	public boolean shouldRenderInPass(int pass) {
		if (pass == 1) return true;
		return super.shouldRenderInPass(pass);
	}
	
	class AIMoveRandom extends EntityAIBase {
		public AIMoveRandom() {
			this.setMutexBits(1);
		}
		
		@Override
		public boolean shouldExecute() {
			return !EntityGhost.this.getMoveHelper().isUpdating() && EntityGhost.this.rand.nextInt(7) == 0;
		}
		
		@Override
		public boolean shouldContinueExecuting() {
			return false;
		}
		
		@Override
		public void updateTask() {
			BlockPos blockpos = new BlockPos(EntityGhost.this);
			for (int i = 0; i < 3; ++i) {
				BlockPos blockpos1 = blockpos.add(EntityGhost.this.rand.nextInt(15) - 7, EntityGhost.this.rand.nextInt(7) - 3, EntityGhost.this.rand.nextInt(15) - 7);
				if (EntityGhost.this.world.isAirBlock(blockpos1)) {
					EntityGhost.this.moveHelper.setMoveTo((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 0.25D);
					if (EntityGhost.this.getAttackTarget() == null) {
						EntityGhost.this.getLookHelper().setLookPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
					}
					break;
				}
			}
		}
	}
	
	class AIMoveControl extends EntityMoveHelper {
		private AIMoveControl(EntityGhost ghost) {
			super(ghost);
		}
		
		@Override
		public void onUpdateMoveHelper() {
			if (this.action == Action.MOVE_TO) {
				double d0 = this.posX - EntityGhost.this.posX;
				double d1 = this.posY - EntityGhost.this.posY;
				double d2 = this.posZ - EntityGhost.this.posZ;
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				d3 = (double) MathHelper.sqrt(d3);
				EntityGhost tempGhost;
				if (d3 < EntityGhost.this.getEntityBoundingBox().getAverageEdgeLength()) {
					this.action = Action.WAIT;
					tempGhost = EntityGhost.this;
					tempGhost.motionX *= 0.5D;
					tempGhost = EntityGhost.this;
					tempGhost.motionY *= 0.5D;
					tempGhost = EntityGhost.this;
					tempGhost.motionZ *= 0.5D;
				}
				else {
					tempGhost = EntityGhost.this;
					tempGhost.motionX += d0 / d3 * 0.05D * this.speed;
					tempGhost = EntityGhost.this;
					tempGhost.motionY += d1 / d3 * 0.05D * this.speed;
					tempGhost = EntityGhost.this;
					tempGhost.motionZ += d2 / d3 * 0.05D * this.speed;
					if (EntityGhost.this.getAttackTarget() == null) {
						EntityGhost.this.rotationYaw = -((float) MathHelper.atan2(EntityGhost.this.motionX, EntityGhost.this.motionZ)) * 57.295776F;
						EntityGhost.this.renderYawOffset = EntityGhost.this.rotationYaw;
					}
					else {
						double d4 = EntityGhost.this.getAttackTarget().posX - EntityGhost.this.posX;
						double d5 = EntityGhost.this.getAttackTarget().posZ - EntityGhost.this.posZ;
						EntityGhost.this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * 57.295776F;
						EntityGhost.this.renderYawOffset = EntityGhost.this.rotationYaw;
					}
				}
			}
		}
	}
	
	class AIChargeAttack extends EntityAIBase {
		private AIChargeAttack() {
			this.setMutexBits(1);
		}
		
		private double getAttackReachSqr(EntityLivingBase attackTarget) {
			return EntityGhost.this.width * 2.0F * EntityGhost.this.width * 2.0F + attackTarget.width;
		}
		
		public boolean shouldExecute() {
			if (EntityGhost.this.getAttackTarget() != null && !EntityGhost.this.getMoveHelper().isUpdating() && EntityGhost.this.rand.nextInt(7) == 0) {
				return EntityGhost.this.getDistanceSq(EntityGhost.this.getAttackTarget()) > 4.0D;
			}
			else {
				return false;
			}
		}
		
		public boolean shouldContinueExecuting() {
			return EntityGhost.this.getMoveHelper().isUpdating() && EntityGhost.this.isCharging() && EntityGhost.this.getAttackTarget() != null && EntityGhost.this.getAttackTarget().isEntityAlive();
		}
		
		public void startExecuting() {
			EntityLivingBase entitylivingbase = EntityGhost.this.getAttackTarget();
			Vec3d vec3d = entitylivingbase.getPositionEyes(1.0F);
			EntityGhost.this.moveHelper.setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
			EntityGhost.this.setCharging(true);
		}
		
		public void resetTask() {
			EntityGhost.this.setCharging(false);
		}
		
		public void updateTask() {
			EntityLivingBase entitylivingbase = EntityGhost.this.getAttackTarget();
			if (entitylivingbase != null) {
				if (EntityGhost.this.getDistanceSq(entitylivingbase) <= getAttackReachSqr(entitylivingbase)) {
					EntityGhost.this.attackEntityAsMob(entitylivingbase);
					EntityGhost.this.setCharging(false);
				}
				else {
					double d0 = EntityGhost.this.getDistanceSq(entitylivingbase);
					if (d0 < 12.0D) {
						Vec3d vec3d = entitylivingbase.getPositionEyes(1.0F);
						EntityGhost.this.moveHelper.setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
					}
				}
			}
		}
	}
}
