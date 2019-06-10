package com.bewitchment.common.item;

import com.bewitchment.Util;
import com.bewitchment.common.block.BlockGlyph;
import com.bewitchment.registry.ModObjects;
import com.bewitchment.registry.ModSounds;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings("NullableProblems")
public class ItemChalk extends Item {
	public ItemChalk(String name) {
		super();
		setMaxStackSize(1);
		setMaxDamage(40);
		setNoRepair();
		Util.registerItem(this, name);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ) {
		boolean isReplacing = world.getBlockState(pos).getBlock() == ModObjects.glyph && world.getBlockState(pos).getValue(BlockGlyph.TYPE) != BlockGlyph.GOLDEN;
		if (!world.isRemote && (face == EnumFacing.UP && ModObjects.glyph.canPlaceBlockAt(world, pos.up()) || isReplacing)) {
			ItemStack stack = player.getHeldItem(hand);
			if (!player.isCreative()) stack.damageItem(1, player);
			world.setBlockState(isReplacing ? pos : pos.up(), ModObjects.glyph.getDefaultState().withProperty(BlockGlyph.TYPE, stack.getItem() == ModObjects.focal_chalk ? BlockGlyph.GOLDEN : stack.getItem() == ModObjects.ritual_chalk ? BlockGlyph.NORMAL : stack.getItem() == ModObjects.fiery_chalk ? BlockGlyph.NETHER : BlockGlyph.ENDER).withProperty(BlockHorizontal.FACING, EnumFacing.HORIZONTALS[player.getRNG().nextInt(4)]));
			world.playSound(null, pos, ModSounds.CHALK_SCRIBBLE, SoundCategory.BLOCKS, 0.5f, 1 + 0.5f * player.getRNG().nextFloat());
		}
		return EnumActionResult.SUCCESS;
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return false;
	}
	
	@Override
	public boolean canItemEditBlocks() {
		return true;
	}
}