package com.bewitchment.common.entity.spirits.demons;

import com.bewitchment.common.entity.living.EntityMultiSkin;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.monster.IMob;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Joseph on 1/13/2019.
 */
public class EntityDemonBase extends EntityMultiSkin implements IAnimatedEntity, IMob {
	public EntityDemonBase(World worldIn) {
		super(worldIn);
	}

	@Override
	public int getSkinTypes() {
		return 0;
	}

	@Override
	public int getAnimationTick() {
		return 0;
	}

	@Override
	public void setAnimationTick(int tick) {

	}

	@Override
	public Animation getAnimation() {
		return null;
	}

	@Override
	public void setAnimation(Animation animation) {

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
