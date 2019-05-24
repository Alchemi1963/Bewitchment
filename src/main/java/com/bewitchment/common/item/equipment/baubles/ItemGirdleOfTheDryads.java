package com.bewitchment.common.item.equipment.baubles;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.render.IRenderBauble;
import com.bewitchment.Util;
import com.bewitchment.client.model.bauble.ModelGirdleOfTheDryad;
import com.bewitchment.client.model.bauble.ModelGirdleOfTheDryadArmor;
import com.bewitchment.common.item.util.ModItemBauble;
import com.bewitchment.registry.ModObjects;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Joseph on 5/24/2019.
 */

@SuppressWarnings({"ConstantConditions", "unused", "WeakerAccess"})
public class ItemGirdleOfTheDryads extends ModItemBauble implements IRenderBauble {
	public ItemGirdleOfTheDryads() {
		super("girdle_of_the_dryads", BaubleType.BELT);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {
		if (type == RenderType.BODY) {
			ModelBase model = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty() ? new ModelGirdleOfTheDryad() : new ModelGirdleOfTheDryadArmor();
			GlStateManager.pushMatrix();
			IRenderBauble.Helper.rotateIfSneaking(player);
			GlStateManager.rotate(180, 1, 0, 0);
			GlStateManager.translate(0, 0, 0.02);
			GlStateManager.scale(0.12, 0.12, 0.12);
			IRenderBauble.Helper.translateToChest();
			IRenderBauble.Helper.defaultTransforms();
			model.render(player, player.limbSwing, player.limbSwingAmount, player.ticksExisted, player.prevRotationYaw, player.rotationPitch, 1);
			GlStateManager.popMatrix();
		}
	}
	
	@Override
	public void onEquipped(ItemStack stack, EntityLivingBase player) {
		player.playSound(SoundEvents.BLOCK_WOOD_STEP, .75F, 1.9f);
	}
	
	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		setBark(stack, 0);
	}
	
	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase living) {
		if (!living.world.isRemote && living.getRNG().nextDouble() < 0.0008 && living.world.getBlockState(living.getPosition().down()).getBlock() instanceof BlockGrass && getBark(living) > -1 && getBark(living) < 4) {
			living.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 80, 10, false, false));
			living.world.playSound(null, living.getPosition(), SoundEvents.BLOCK_CHORUS_FLOWER_GROW, SoundCategory.PLAYERS, 1, 1);
			setBark(getGirdle(living), getBark(living) + 1);
		}
	}
	
	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event) {
		if (!event.getEntityLiving().world.isRemote && event.getAmount() > 2 && event.getSource().getTrueSource() != null && getBark(event.getEntityLiving()) > 0) {
			event.getEntityLiving().world.playSound(null, event.getEntityLiving().getPosition(), SoundEvents.BLOCK_WOOD_STEP, SoundCategory.PLAYERS, 0.75f, 1.9f);
			setBark(getGirdle(event.getEntityLiving()), getBark(event.getEntityLiving()) - 1);
			event.setCanceled(true);
		}
	}
	
	public static ItemStack getGirdle(EntityLivingBase living) {
		if (Util.hasBauble(living, ModObjects.girdle_of_the_dryads)) {
			EntityPlayer player = (EntityPlayer) living;
			for (int i = 0; i < BaublesApi.getBaublesHandler(player).getSlots(); i++) {
				ItemStack bauble = BaublesApi.getBaublesHandler(player).getStackInSlot(i);
				if (bauble.getItem() == ModObjects.girdle_of_the_dryads) return bauble;
			}
		}
		return ItemStack.EMPTY;
	}
	
	public static int getBark(EntityLivingBase living) {
		if (Util.hasBauble(living, ModObjects.girdle_of_the_dryads)) {
			ItemStack stack = getGirdle(living);
			if (!stack.isEmpty()) {
				if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
				return stack.getTagCompound().getInteger("bark");
			}
		}
		return -1;
	}
	
	public static void setBark(ItemStack stack, int amount) {
		if (!stack.isEmpty()) {
			if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("bark", amount);
		}
	}
}