package com.bewitchment.common.enchantment.util;

import com.bewitchment.api.BewitchmentAPI;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings({"unused"})
public class EnchantmentSpiritProtection extends ModEnchantment {
	public EnchantmentSpiritProtection() {
		super("spirit_protection", Rarity.UNCOMMON, 4, EnumEnchantmentType.ARMOR, EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	protected boolean canApplyTogether(Enchantment enchantment) {
		if (enchantment instanceof EnchantmentProtection) {
			EnchantmentProtection protection = (EnchantmentProtection) enchantment;
			return protection.protectionType != EnchantmentProtection.Type.FALL;
		}
		return super.canApplyTogether(enchantment);
	}
	
	@SubscribeEvent
	public void livingHurt(LivingHurtEvent event) {
		applyEnchantment(event, getTotalLevelOnEntity(event.getEntityLiving()));
	}
	
	public void applyEnchantment(LivingHurtEvent event, int level) {
		if (level > 0) event.setAmount(event.getAmount() * (1 - Math.min(20, calcModifierDamage(level, event.getSource())) / 25f));
	}
	
	@Override
	public int calcModifierDamage(int level, DamageSource source) {
		return source.canHarmInCreative() ? 0 : source.getTrueSource() instanceof EntityLivingBase && BewitchmentAPI.isWeakToColdIron((EntityLivingBase) source.getTrueSource()) ? level * 2 : 0;
	}
}