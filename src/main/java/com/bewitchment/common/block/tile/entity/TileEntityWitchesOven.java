package com.bewitchment.common.block.tile.entity;

import com.bewitchment.api.BewitchmentAPI;
import com.bewitchment.api.registry.OvenRecipe;
import com.bewitchment.common.block.BlockWitchesOven;
import com.bewitchment.common.block.tile.entity.util.ModTileEntity;
import com.bewitchment.registry.ModObjects;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vazkii.botania.api.item.IExoflameHeatable;

@SuppressWarnings({"NullableProblems", "ConstantConditions"})
@Optional.Interface(iface = "vazkii.botania.api.item.IExoflameHeatable", modid = "botania")
public class TileEntityWitchesOven extends ModTileEntity implements ITickable, IExoflameHeatable {
	private final ItemStackHandler inventory_up = new ItemStackHandler(3) {
		@Override
		public boolean isItemValid(int index, ItemStack stack) {
			return index == 0 ? TileEntityFurnace.isItemFuel(stack) : index != 1 || stack.getItem() == ModObjects.empty_jar;
		}
		
		@Override
		protected void onContentsChanged(int index) {
			recipe = BewitchmentAPI.REGISTRY_OVEN.getValuesCollection().stream().filter(p -> p.matches(getStackInSlot(2))).findFirst().orElse(null);
		}
	};
	private final ItemStackHandler inventory_down = new ItemStackHandler(2) {
		@Override
		public boolean isItemValid(int index, ItemStack stack) {
			return false;
		}
	};
	public int burnTime, fuelBurnTime, progress;
	private OvenRecipe recipe;
	private boolean burning;
	
	@Override
	public void update() {
		if (!world.isRemote) {
			if (burning && burnTime < 1) burning = world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockWitchesOven.LIT, false));
			if (burnTime > 0) burnTime--;
			else {
				if (progress > 0) {
					progress -= 2;
					if (progress < 0) progress = 0;
				}
			}
			if (recipe == null || !recipe.isValid(inventory_up, inventory_down)) progress = 0;
			else {
				if (burnTime < 1) {
					int time = TileEntityFurnace.getItemBurnTime(inventory_up.getStackInSlot(0));
					if (time > 0) burnFuel(time, true);
				}
				else {
					progress++;
					if (progress >= 200) {
						progress = 0;
						recipe.giveOutput(world.rand, inventory_up, inventory_down);
					}
				}
			}
		}
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing face) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(face == EnumFacing.DOWN ? inventory_down : inventory_up) : super.getCapability(capability, face);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing face) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, face);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("recipe", recipe == null ? "" : recipe.getRegistryName().toString());
		tag.setBoolean("burning", burning);
		tag.setInteger("burnTime", burnTime);
		tag.setInteger("fuelBurnTime", fuelBurnTime);
		tag.setInteger("progress", progress);
		return super.writeToNBT(tag);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		recipe = tag.getString("recipe").isEmpty() ? null : BewitchmentAPI.REGISTRY_OVEN.getValue(new ResourceLocation(tag.getString("recipe")));
		burning = tag.getBoolean("burning");
		burnTime = tag.getInteger("burnTime");
		fuelBurnTime = tag.getInteger("fuelBurnTime");
		progress = tag.getInteger("progress");
		super.readFromNBT(tag);
	}
	
	@Override
	public ItemStackHandler[] getInventories() {
		return new ItemStackHandler[]{inventory_up, inventory_down};
	}
	
	private void burnFuel(int time, boolean consume) {
		burnTime = time + 1;
		fuelBurnTime = burnTime;
		if (consume) {
			ItemStack stack = inventory_up.extractItem(0, 1, false);
			if (stack.getItem() == Items.LAVA_BUCKET) inventory_up.insertItem(0, new ItemStack(Items.BUCKET), false);
		}
		if (!burning) burning = world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockWitchesOven.LIT, true));
	}
	
	@Override
	@Optional.Method(modid = "botania")
	public boolean canSmelt() {
		return recipe != null && recipe.isValid(inventory_up, inventory_down);
	}
	
	@Override
	@Optional.Method(modid = "botania")
	public int getBurnTime() {
		return Math.max(0, burnTime - 1);
	}
	
	@Override
	@Optional.Method(modid = "botania")
	public void boostBurnTime() {
		burnFuel(200, false);
	}
	
	@Override
	@Optional.Method(modid = "botania")
	public void boostCookTime() {
		progress++;
	}
}