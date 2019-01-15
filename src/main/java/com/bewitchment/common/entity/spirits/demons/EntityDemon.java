package com.bewitchment.common.entity.spirits.demons;

import com.bewitchment.api.BewitchmentAPI;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.IMob;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Joseph on 1/14/2019.
 */
public class EntityDemon extends EntityDemonBase implements IAnimatedEntity, IMob {
	private int animationTick;
	private Animation currentAnimation;

	public EntityDemon(World worldIn) {
		super(worldIn);
		setSize(1.8F, 4.6F);
		this.isImmuneToFire = true;
	}

	@Override
	public int getSkinTypes() {
		return 4;
	}

	public EnumCreatureAttribute getCreatureAttribute() {
		return BewitchmentAPI.getAPI().DEMON;
	}

	@Override
	public int getAnimationTick() {
		return animationTick;
	}

	@Override
	public void setAnimationTick(int tick) {
		animationTick = tick;
	}

	@Override
	public Animation getAnimation() {
		return currentAnimation;
	}

	@Override
	public void setAnimation(Animation animation) {
		currentAnimation = animation;
	}

	@Override
	public Animation[] getAnimations() {
		return new Animation[0];
	}

	@Nullable
	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return null;
	}
}
