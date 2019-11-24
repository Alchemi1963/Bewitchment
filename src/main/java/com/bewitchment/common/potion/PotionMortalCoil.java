package com.bewitchment.common.potion;

import com.bewitchment.common.potion.util.ModPotion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings({"ConstantConditions", "unused"})
public class PotionMortalCoil extends ModPotion {
	public PotionMortalCoil() {
		super("mortal_coil", true, 0x4B3621);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onLivingTick(LivingEvent.LivingUpdateEvent event) {
		if (!event.getEntityLiving().world.isRemote && event.getEntityLiving().isPotionActive(this) && event.getEntityLiving().getActivePotionEffect(this).getDuration() == 1) {
			if (event.getEntityLiving() instanceof EntityPlayer) event.getEntityLiving().attackEntityFrom(DamageSource.WITHER, Integer.MAX_VALUE);
			else event.getEntityLiving().setDead();
		}
	}
}