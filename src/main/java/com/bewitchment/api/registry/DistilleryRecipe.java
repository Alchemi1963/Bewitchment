package com.bewitchment.api.registry;

import com.bewitchment.Util;
import com.bewitchment.common.block.tile.entity.util.ModTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class DistilleryRecipe extends IForgeRegistryEntry.Impl<DistilleryRecipe> {
	public final List<Ingredient> input;
	public final List<ItemStack> output;
	
	public DistilleryRecipe(ResourceLocation name, List<Ingredient> input, List<ItemStack> output) {
		setRegistryName(name);
		this.input = Util.expandList(input);
		this.output = output;
		if (this.input.size() > 6) throw new IllegalArgumentException("Input size for " + name.toString() + " is too big, must be 6 at most.");
	}
	
	public final boolean matches(ItemStackHandler input) {
		return Util.areISListsEqual(this.input, input);
	}
	
	public final boolean isValid(ItemStackHandler input, ItemStackHandler output) {
		for (ItemStack stack : this.output)
			if (ModTileEntity.getFirstValidSlot(output, stack) < 0) return false;
		return true;
	}
	
	public final void giveOutput(ItemStackHandler input, ItemStackHandler output) {
		for (int i = 0; i < input.getSlots(); i++)
			input.extractItem(i, 1, false);
		for (ItemStack stack : this.output)
			output.insertItem(ModTileEntity.getFirstValidSlot(output, stack), stack.copy(), false);
	}
}