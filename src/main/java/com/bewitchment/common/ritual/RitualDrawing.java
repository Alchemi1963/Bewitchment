package com.bewitchment.common.ritual;

import com.bewitchment.api.registry.Ritual;
import com.bewitchment.common.block.BlockGlyph;
import com.bewitchment.registry.ModObjects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class RitualDrawing extends Ritual {
	private final int[][] circle;
	
	public RitualDrawing(ResourceLocation name, List<Ingredient> input, int startingPower, int small, int medium, int big, int[][] circle) {
		super(name, input, null, null, false, 0, startingPower, 0, small, medium, big);
		this.circle = circle;
	}
	
	@Override
	public boolean isValid(World world, BlockPos pos, EntityPlayer caster) {
		for (int x = 0; x < circle.length; x++) {
			for (int z = 0; z < circle.length; z++) {
				BlockPos pos0 = pos.add(x - circle.length / 2, 0, z - circle.length / 2);
				if (circle[x][z] == 1 && !ModObjects.glyph.canPlaceBlockAt(world, pos0)) return false;
			}
		}
		Item item = caster.getHeldItemOffhand().getItem();
		return (item == ModObjects.ritual_chalk || item == ModObjects.fiery_chalk || item == ModObjects.phasing_chalk) && (caster.isCreative() || caster.getHeldItemOffhand().getItemDamage() >= circle.length);
	}
	
	@Override
	public void onStarted(World world, BlockPos pos, EntityPlayer caster) {
		Item item = caster.getHeldItemOffhand().getItem();
		int type = item == ModObjects.ritual_chalk ? BlockGlyph.NORMAL : item == ModObjects.fiery_chalk ? BlockGlyph.NETHER : BlockGlyph.ENDER;
		for (int x = 0; x < circle.length; x++) {
			for (int z = 0; z < circle.length; z++) {
				if (circle[x][z] == 1) world.setBlockState(pos.add(x - circle.length / 2, 0, z - circle.length / 2), ModObjects.glyph.getDefaultState().withProperty(BlockGlyph.TYPE, type));
			}
		}
		if (!caster.isCreative()) caster.getHeldItemOffhand().damageItem(circle.length, caster);
	}
}