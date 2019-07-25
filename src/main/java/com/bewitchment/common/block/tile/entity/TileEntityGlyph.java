package com.bewitchment.common.block.tile.entity;

import com.bewitchment.Util;
import com.bewitchment.api.capability.extendedplayer.ExtendedPlayer;
import com.bewitchment.api.capability.magicpower.MagicPower;
import com.bewitchment.api.registry.Ritual;
import com.bewitchment.common.block.tile.entity.util.TileEntityAltarStorage;
import com.bewitchment.registry.ModObjects;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.ItemStackHandler;

import java.util.UUID;

@SuppressWarnings({"WeakerAccess", "ConstantConditions"})
public class TileEntityGlyph extends TileEntityAltarStorage implements ITickable {
	private final ItemStackHandler inventory = new ItemStackHandler(12) {
		@Override
		protected void onContentsChanged(int slot) {
			syncToClient();
		}
	};
	
	public Ritual ritual;
	public UUID casterId;
	public EntityPlayer caster;
	public BlockPos effectivePos;
	public int effectiveDim, time;
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("ritual", ritual == null ? "" : ritual.getRegistryName().toString());
		tag.setLong("effectivePos", effectivePos == null ? 0 : effectivePos.toLong());
		tag.setInteger("effectiveDim", effectiveDim);
		tag.setString("casterId", casterId == null ? "" : casterId.toString());
		tag.setInteger("time", time);
		return super.writeToNBT(tag);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		ritual = tag.getString("ritual").isEmpty() ? null : GameRegistry.findRegistry(Ritual.class).getValue(new ResourceLocation(tag.getString("ritual")));
		effectivePos = BlockPos.fromLong(tag.getLong("effectivePos"));
		effectiveDim = tag.getInteger("effectiveDim");
		casterId = tag.getString("casterId").isEmpty() ? null : UUID.fromString(tag.getString("casterId"));
		time = tag.getInteger("time");
		super.readFromNBT(tag);
	}
	
	@Override
	public ItemStackHandler[] getInventories() {
		return new ItemStackHandler[]{inventory};
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		boolean flag = super.shouldRefresh(world, pos, oldState, newState);
		if (!world.isRemote && flag) stopRitual(false);
		return flag;
	}
	
	@Override
	public void update() {
		if (!world.isRemote && ritual != null) {
			if (caster != null) {
				if (world.getTotalWorldTime() % 20 == 0) {
					if (!MagicPower.attemptDrain(altarPos != null ? world.getTileEntity(altarPos) : null, caster, ritual.runningPower)) stopRitual(false);
					else time++;
				}
				ritual.onUpdate(world, effectivePos, caster, inventory);
			}
			if (ritual.time >= 0 && time >= ritual.time) stopRitual(true);
		}
	}
	
	@Override
	public boolean activate(World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing face) {
		if (!world.isRemote) {
			if (player.isSneaking()) {
				int last = getLastNonEmptySlot(inventory);
				if (last > -1) InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), inventory.extractItem(last, inventory.getStackInSlot(last).getCount(), false));
			}
			else {
				ItemStack stack = player.getHeldItem(hand);
				if (ritual == null) {
					if (!stack.isEmpty()) {
						int slot = getFirstEmptySlot(inventory);
						if (slot < 10 && slot > -1) inventory.insertItem(slot, stack.splitStack(1), false);
					}
					else {
						Ritual rit = GameRegistry.findRegistry(Ritual.class).getValuesCollection().stream().filter(r -> r.matches(world, pos, inventory)).findFirst().orElse(null);
						if (rit != null) {
							if (rit.isValid(world, pos, player, inventory)) startRitual(player, rit);
							else player.sendStatusMessage(new TextComponentTranslation("ritual.invalid"), true);
						}
						else player.sendStatusMessage(new TextComponentTranslation("ritual.null"), true);
					}
				}
				else {
					if (stack.getItem() == ModObjects.waystone && stack.hasTagCompound() && stack.getTagCompound().hasKey("location")) {
						if (ritual.canBePerformedRemotely) {
							effectivePos = BlockPos.fromLong(stack.getTagCompound().getLong("location"));
							effectiveDim = stack.getTagCompound().getInteger("dimension");
							stack.damageItem(1, player);
							syncToClient();
						}
					}
					else stopRitual(false);
				}
			}
		}
		return true;
	}
	
	public void startRitual(EntityPlayer player, Ritual rit) {
		if (!player.world.isRemote) {
			if (MagicPower.attemptDrain(altarPos != null ? world.getTileEntity(altarPos) : null, player, rit.startingPower)) {
				ritual = rit;
				player.getCapability(ExtendedPlayer.CAPABILITY, null).ritualsCast++;
				ExtendedPlayer.syncToClient(player);
				casterId = player.getGameProfile().getId();
				caster = Util.findPlayer(casterId);
				effectivePos = pos;
				effectiveDim = world.provider.getDimension();
				time = 0;
				ritual.onStarted(world, pos, player, inventory);
				player.sendStatusMessage(new TextComponentTranslation("ritual." + ritual.getRegistryName().toString().replace(":", ".")), true);
				syncToClient();
			}
			else player.sendStatusMessage(new TextComponentTranslation("altar.no_power"), true);
		}
	}
	
	public void stopRitual(boolean finished) {
		if (!world.isRemote) {
			if (ritual != null && caster != null) {
				if (finished) ritual.onFinished(world, effectivePos, caster, inventory);
				else ritual.onHalted(world, effectivePos, caster, inventory);
			}
			ritual = null;
			casterId = null;
			caster = null;
			effectivePos = pos;
			effectiveDim = world.provider.getDimension();
			time = 0;
			syncToClient();
		}
	}
}