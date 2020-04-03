package com.bewitchment.common.potion;

import com.bewitchment.common.potion.util.ModPotion;
import com.sun.jna.platform.win32.Guid;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

/**
 * Created by Joseph on 4/2/2020.
 */

//Todo: Everything.
public class PotionCorrosion extends ModPotion {
	protected PotionCorrosion(String name, boolean isNegative, int color) {
		super("corrosion", true, 0x0BDA51);
	}
	
	@SubscribeEvent
	public void onLivingTick(LivingEvent.LivingUpdateEvent event) {
		AttributeModifier attributeModifier = new AttributeModifier(UUID.fromString("e8506ffe-e2b4-4f19-8669-becb8e3eb666"));
		
		if (!event.getEntityLiving().world.isRemote && event.getEntityLiving().isPotionActive(this)) {
			event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.ARMOR).getModifier(UUID.fromString("e8506ffe-e2b4-4f19-8669-becb8e3eb666"));
		}
	}
}
