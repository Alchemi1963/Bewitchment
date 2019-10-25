package com.bewitchment.common.entity.util;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

@SuppressWarnings("EntityConstructor")
public abstract class ModEntityMob extends EntityMob {
	public static final DataParameter<Integer> SKIN = EntityDataManager.createKey(ModEntityMob.class, DataSerializers.VARINT);
	
	private final ResourceLocation lootTableLocation;
	
	public boolean limitedLifeSpan = false;
	public int lifeTimeTicks = 0;
	public UUID summoner;
	
	protected ModEntityMob(World world, ResourceLocation lootTableLocation) {
		super(world);
		this.lootTableLocation = lootTableLocation;
	}
	
	protected int getSkinTypes() {
		return 1;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if (this.limitedLifeSpan) {
			if (lifeTimeTicks <= 0) {
				setDead();
				for (int i = 0; i < 128; i++) {
					world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX - 0.25 + world.rand.nextDouble() * width, posY + world.rand.nextDouble() * height, posZ - 0.25 + world.rand.nextDouble() * width, 0, 0, 0);
				}
			}
			else lifeTimeTicks--;
		}
		if (summoner != null) {
			EntityPlayer player = world.getPlayerEntityByUUID(summoner);
			if (player != null && this.getAttackTarget() == player) {
				this.setAttackTarget(player.getAttackingEntity() == null ? player.getLastAttackedEntity() : player.getAttackingEntity());
			}
		}
	}
	
	@Override
	protected abstract boolean isValidLightLevel();
	
	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(SKIN, 0);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		tag.setInteger("skin", dataManager.get(SKIN));
		tag.setBoolean("limitedLifeSpan", limitedLifeSpan);
		tag.setInteger("lifeTimeTick", lifeTimeTicks);
		tag.setString("summoner", summoner == null ? "" : summoner.toString());
		dataManager.setDirty(SKIN);
		super.writeEntityToNBT(tag);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		dataManager.set(SKIN, tag.getInteger("skin"));
		lifeTimeTicks = tag.getInteger("lifeTimeTick");
		limitedLifeSpan = tag.getBoolean("limitedLifeSpan");
		summoner = tag.getString("summoner").equals("") ? null : UUID.fromString(tag.getString("summoner"));
		super.readEntityFromNBT(tag);
	}
	
	@Override
	protected ResourceLocation getLootTable() {
		return lootTableLocation;
	}
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData data) {
		dataManager.set(SKIN, rand.nextInt(getSkinTypes()));
		return super.onInitialSpawn(difficulty, data);
	}
}