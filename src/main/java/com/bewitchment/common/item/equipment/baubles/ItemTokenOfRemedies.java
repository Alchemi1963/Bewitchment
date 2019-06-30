package com.bewitchment.common.item.equipment.baubles;

import baubles.api.BaubleType;
import com.bewitchment.api.capability.magicpower.MagicPower;
import com.bewitchment.common.item.util.ModItemBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;

@SuppressWarnings("unused")
public class ItemTokenOfRemedies extends ModItemBauble {
	public ItemTokenOfRemedies() {
		super("token_of_remedies", BaubleType.TRINKET);
	}
	
	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_IRON, .75F, 1.9f);
	}
	
	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase living) {
		if (living.ticksExisted % 40 == 0 && living instanceof EntityPlayer && (living.isPotionActive(MobEffects.BLINDNESS) || living.isPotionActive(MobEffects.NAUSEA) || living.isPotionActive(MobEffects.POISON) || living.isPotionActive(MobEffects.WEAKNESS) || living.isPotionActive(MobEffects.WITHER)) && MagicPower.attemptDrain(null, (EntityPlayer) living, 20)) {
			living.removePotionEffect(MobEffects.BLINDNESS);
			living.removePotionEffect(MobEffects.NAUSEA);
			living.removePotionEffect(MobEffects.POISON);
			living.removePotionEffect(MobEffects.WEAKNESS);
			living.removePotionEffect(MobEffects.WITHER);
		}
	}
}