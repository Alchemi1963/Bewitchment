package com.bewitchment.common.ritual;

import com.bewitchment.Bewitchment;
import com.bewitchment.Util;
import com.bewitchment.api.registry.Ritual;
import com.bewitchment.common.block.BlockGlyph;
import com.bewitchment.registry.ModObjects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Arrays;

public class RitualHighMoon extends Ritual {
	public RitualHighMoon() {
		super(new ResourceLocation(Bewitchment.MODID, "high_moon"), Arrays.asList(Util.get("ingotSilver"), Util.get(Items.NETHERBRICK), Util.get(ModObjects.hellebore)), null, null, 5, 500, 20, BlockGlyph.NORMAL, -1, -1);
	}
	
	@Override
	public boolean isValid(World world, BlockPos pos, EntityPlayer caster, ItemStackHandler inventory) {
		return world.isDaytime();
	}
	
	@Override
	public void onFinished(World world, BlockPos pos, EntityPlayer caster, ItemStackHandler inventory) {
		super.onFinished(world, pos, caster, inventory);
		if (!world.isRemote) world.setWorldTime(world.getWorldTime() + (41600 - (world.getWorldTime() % 24000)) % 24000);
	}
}