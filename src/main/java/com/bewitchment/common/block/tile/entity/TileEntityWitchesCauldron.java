package com.bewitchment.common.block.tile.entity;

import com.bewitchment.Bewitchment;
import com.bewitchment.Util;
import com.bewitchment.api.BewitchmentAPI;
import com.bewitchment.api.message.SpawnBubble;
import com.bewitchment.api.message.SpawnParticle;
import com.bewitchment.api.registry.Brew;
import com.bewitchment.common.block.tile.entity.util.TileEntityAltarStorage;
import com.bewitchment.registry.ModObjects;
import com.bewitchment.registry.ModPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

@SuppressWarnings({"ConstantConditions", "NullableProblems"})
public class TileEntityWitchesCauldron extends TileEntityAltarStorage implements ITickable {
	private static final AxisAlignedBB collectionZone = new AxisAlignedBB(0, 0, 0, 1, 0.65, 1);
	
	private final ItemStackHandler inventory = new ItemStackHandler(Byte.MAX_VALUE);
	private final FluidTank tank = new FluidTank(Fluid.BUCKET_VOLUME);
	
	/**
	 * 0 = none, 1 = failed, 2 = draining, 3 = brewing, 4 = teleporting
	 */
	private int mode = 0;
	
	private static final int[] defaultColor = {0, 63, 255};
	public int[] color = {defaultColor[0], defaultColor[1], defaultColor[2]};
	private int[] targetColor = {defaultColor[0], defaultColor[1], defaultColor[2]};
	private int heatTimer = 0;
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
		tag.setInteger("mode", mode);
		tag.setIntArray("color", color);
		tag.setIntArray("targetColor", targetColor);
		tag.setInteger("heatTimer", heatTimer);
		return super.writeToNBT(tag);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		tank.readFromNBT(tag.getCompoundTag("tank"));
		mode = tag.getInteger("mode");
		color = tag.getIntArray("color");
		targetColor = tag.getIntArray("targetColor");
		heatTimer = tag.getInteger("heatTimer");
		super.readFromNBT(tag);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing face) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank) : super.getCapability(capability, face);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing face) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, face);
	}
	
	@Override
	public ItemStackHandler[] getInventories() {
		return new ItemStackHandler[]{inventory};
	}
	
	@Override
	public void update() {
		if (color.length == 0 || targetColor.length == 0) return;
		if (mode == 2) tank.drain(Math.min(32, tank.getFluidAmount()), true);
		if (!world.isRemote) {
			if (color[0] != targetColor[0]) for (int i = 0; i < 6; i++) color[0] += color[0] < targetColor[0] ? 1 : -1;
			if (color[1] != targetColor[1]) for (int i = 0; i < 6; i++) color[1] += color[1] < targetColor[1] ? 1 : -1;
			if (color[2] != targetColor[2]) for (int i = 0; i < 6; i++) color[2] += color[2] < targetColor[2] ? 1 : -1;
			boolean isLava = tank.getFluid() != null && tank.getFluid().getFluid().getTemperature() >= FluidRegistry.LAVA.getTemperature();
			if (isLava) {
				if (world.rand.nextInt(100) == 0) {
					world.playSound(null, getPos(), SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 0.2f + world.rand.nextFloat() * 0.2f, 0.9f + world.rand.nextFloat() * 0.15f);
					Bewitchment.network.sendToDimension(new SpawnParticle(EnumParticleTypes.LAVA, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5), world.provider.getDimension());
				}
				if (world.rand.nextInt(200) == 0) world.playSound(null, getPos(), SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, 0.2f + world.rand.nextFloat() * 0.2f, 0.9f + world.rand.nextFloat() * 0.15f);
			}
			for (EntityLivingBase living : world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(getPos()))) {
				if (isLava) {
					living.attackEntityFrom(DamageSource.LAVA, 4);
					living.setFire(15);
				}
				else {
					living.extinguish();
					if (heatTimer >= 5) living.attackEntityFrom(DamageSource.HOT_FLOOR, 1);
				}
			}
			if (world.getTotalWorldTime() % 20 == 0) {
				if (tank.getFluid() != null && Bewitchment.config.heatSources.contains(world.getBlockState(pos.down()).getBlock().getTranslationKey()) && heatTimer <= 5) heatTimer++;
				else if (heatTimer > 0) heatTimer--;
			}
			if (heatTimer >= 5) {
				double height = pos.getY() + getLiquidHeight();
				if (height > pos.getY() + 0.2) for (int i = 0; i < 6; i++)
					Bewitchment.network.sendToDimension(new SpawnBubble(getPos().getX() + 0.2 + (world.rand.nextDouble() * 0.6), height, getPos().getZ() + 0.2 + (world.rand.nextDouble() * 0.6), color[0] / 255f, color[1] / 255f, color[2] / 255f), world.provider.getDimension());
			}
			if (tank.getFluidAmount() < 1) {
				mode = 0;
				resetColor();
				syncToClient();
			}
			if (mode != 2 && world.getTotalWorldTime() % 5 == 0) insertNextItem(isLava);
		}
	}
	
	@Override
	public boolean activate(World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing face) {
		if (!player.isSneaking()) {
			if (!world.isRemote && (mode == 0 || mode == 3)) {
				if (isEmpty(inventory) && player.getHeldItem(hand).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) && FluidUtil.interactWithFluidHandler(player, hand, world, pos, face))
					world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
				else if (player.getHeldItem(hand).getItem() instanceof ItemGlassBottle) {
					if (tank.canDrainFluidType(tank.getFluid())) {
						Util.giveAndConsumeItem(player, hand, createPotion());
						tank.drain(Fluid.BUCKET_VOLUME / 3, true);
						world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1, 1);
						if (tank.getFluidAmount() < 2) {
							tank.drain(Fluid.BUCKET_VOLUME, true);
							clear(inventory);
						}
					}
				}
				syncToClient();
			}
			return true;
		}
		return false;
	}
	
	/**
	 * arg == 1 = splash, arg == 2 = lingering, else normal
	 */
	public static ItemStack createPotion(Collection<PotionEffect> effects, int arg) {
		ItemStack stack;
		if (arg == 1) stack = new ItemStack(Items.SPLASH_POTION);
		else if (arg == 2) stack = new ItemStack(Items.LINGERING_POTION);
		else stack = new ItemStack(Items.POTIONITEM);
		PotionUtils.appendEffects(stack, effects);
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setInteger("CustomPotionColor", PotionUtils.getPotionColorFromEffectList(effects));
		stack.getTagCompound().setBoolean("bewitchment_brew", true);
		return stack;
	}
	
	public double getLiquidHeight() {
		return ((((double) tank.getFluidAmount() / Fluid.BUCKET_VOLUME) - 1) * (2 / 5d)) + (3 / 5d);
	}
	
	private ItemStack createPotion() {
		List<PotionEffect> finalEffects = new ArrayList<>();
		boolean noParticles = contains(inventory, new ItemStack(ModObjects.ravens_feather)), splash = contains(inventory, new ItemStack(Items.GUNPOWDER)), lingering = contains(inventory, new ItemStack(ModObjects.owlets_wing)) && splash;
		int duration = 1, potency = 1;
		LinkedHashSet<PotionEffect> effects = new LinkedHashSet<>();
		for (int i = 0; i < inventory.getSlots(); i++) {
			for (Brew brew : BewitchmentAPI.REGISTRY_BREW.getValuesCollection()) if (brew.input.apply(inventory.getStackInSlot(i))) effects.add(brew.effect);
			if (inventory.getStackInSlot(i).getItem() == Items.REDSTONE) duration++;
			else if (inventory.getStackInSlot(i).getItem() == Items.GLOWSTONE_DUST) potency++;
		}
		potency = Math.min(2, potency); //todo: allow 3 when familiars etc exist
		duration = Math.min(3, duration);
		for (PotionEffect effect : effects) finalEffects.add(new PotionEffect(effect.getPotion(), (int) (effect.getDuration() * (1d / potency) * duration), effect.getAmplifier() + potency - 1, effect.getIsAmbient(), !noParticles));
		List<PotionEffect> toBeginning = new ArrayList<>();
		for (int i = finalEffects.size() - 1; i >= 0; i--) {
			PotionEffect effect = finalEffects.get(i);
			if (effect.getPotion() == ModPotions.magic_resistance || effect.getPotion() == ModPotions.magic_weakness) {
				toBeginning.add(effect);
				finalEffects.remove(i);
			}
		}
		for (PotionEffect effect : toBeginning) finalEffects.add(0, effect);
		List<PotionEffect> toEnd = new ArrayList<>();
		for (int i = finalEffects.size() - 1; i >= 0; i--) {
			PotionEffect effect = finalEffects.get(i);
			if (effect.getPotion() == ModPotions.absence || effect.getPotion() == ModPotions.purification || effect.getPotion() == ModPotions.corruption) {
				toEnd.add(effect);
				finalEffects.remove(i);
			}
		}
		finalEffects.addAll(toEnd);
		return createPotion(finalEffects, lingering ? 2 : splash ? 1 : 0);
	}
	
	private void insertNextItem(boolean isLava) {
		if (!world.isRemote && tank.getFluid() != null) {
			List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, collectionZone.offset(getPos()));
			if (!list.isEmpty()) {
				EntityItem entity = list.get(0);
				ItemStack stack = entity.getItem().splitStack(1);
				if (isLava) {
					world.playSound(null, getPos(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 1, (float) (0.2 * world.rand.nextDouble()) + 1);
					stack.shrink(stack.getCount());
				}
				else {
					world.playSound(null, getPos(), SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.BLOCKS, 1, (float) (0.2 * world.rand.nextDouble()) + 1);
					if (stack.getItem() == ModObjects.wood_ash) {
						mode = 2;
						clear(inventory);
					}
					else {
						if (stack.getItem() == ModObjects.mandrake_root && mode == 0) mode = 3;
						int slot = getFirstEmptySlot(inventory);
						if (slot > -1) {
							boolean valid = mode == 3 && isBrewItem(stack) && heatTimer >= 5;
							inventory.insertItem(slot, stack, false);
							if (valid) {
								if (mode == 3) {
									Brew brew = BewitchmentAPI.REGISTRY_BREW.getValuesCollection().stream().filter(b -> b.matches(stack)).findFirst().orElse(null);
									if (brew != null && brew.output != null && (brew.outputPredicate == null || brew.outputPredicate.test(stack))) {
										EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, brew.output.copy());
										item.setNoGravity(true);
										item.motionX = 0;
										item.motionY = 0;
										item.motionZ = 0;
										world.spawnEntity(item);
									}
								}
								setTargetColor(PotionUtils.getColor(createPotion()));
							}
							else {
								mode = 1;
								setTargetColor(0x604040);
							}
						}
					}
				}
			}
			syncToClient();
		}
	}
	
	private boolean isBrewItem(ItemStack stack) {
		for (Brew brew : BewitchmentAPI.REGISTRY_BREW.getValuesCollection()) if (brew.input.apply(stack)) return true;
		return stack.getItem() == ModObjects.mandrake_root || stack.getItem() == ModObjects.ravens_feather || stack.getItem() == Items.GUNPOWDER || stack.getItem() == ModObjects.owlets_wing || stack.getItem() == Items.REDSTONE || stack.getItem() == Items.GLOWSTONE_DUST;
	}
	
	private void setTargetColor(int color) {
		targetColor[0] = ((color >> 16) & 255);
		targetColor[1] = ((color >> 8) & 255);
		targetColor[2] = (color & 255);
	}
	
	private void resetColor() {
		color[0] = defaultColor[0];
		color[1] = defaultColor[1];
		color[2] = defaultColor[2];
		targetColor[0] = defaultColor[0];
		targetColor[1] = defaultColor[1];
		targetColor[2] = defaultColor[2];
	}
}