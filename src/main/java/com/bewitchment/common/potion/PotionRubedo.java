package com.bewitchment.common.potion;

import com.bewitchment.common.entity.living.EntityOwl;
import com.bewitchment.common.entity.living.EntityToad;
import com.bewitchment.common.potion.util.ModPotion;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.function.Consumer;

@SuppressWarnings({"unused"})
public class PotionRubedo extends ModPotion {
	public PotionRubedo() {
		super("rubedo", false, 0xff0000);
	}
	
	@Override
	public boolean isInstant() {
		return true;
	}
	
	@Override
	public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase living, int amplifier, double health) {
		super.affectEntity(source, indirectSource, living, amplifier, health);
		if (living instanceof EntitySheep) ((EntitySheep) living).setFleeceColor(EnumDyeColor.RED);
		else if (living instanceof EntityWolf) ((EntityWolf)living).setCollarColor(EnumDyeColor.RED);

		living.getArmorInventoryList().forEach(new Consumer<ItemStack>() {
            @Override
            public void accept(ItemStack itemStack) {
                if (itemStack.getItem() instanceof ItemArmor){
                    try {
                        ((ItemArmor) itemStack.getItem()).setColor(itemStack, EnumDyeColor.RED.getColorValue());
                    }catch (UnsupportedOperationException e){
                        //blank
                    }
                }
            }
        });
	}
	
	@Override
	public boolean onImpact(World world, BlockPos pos, int amplifier) {
		boolean flag = false;
		int radius = 2 * (amplifier + 1);
		for (BlockPos pos0 : BlockPos.getAllInBoxMutable(pos.add(-radius, -radius, -radius), pos.add(radius, radius, radius))) {
			FakePlayer thrower = FakePlayerFactory.getMinecraft((WorldServer) world);
			if (!ForgeEventFactory.onPlayerBlockPlace(thrower, new BlockSnapshot(world, pos0, world.getBlockState(pos0)), EnumFacing.fromAngle(thrower.rotationYaw), thrower.getActiveHand()).isCanceled()) {
				if (world.rand.nextInt(3) == 0) {
					Block block = world.getBlockState(pos0).getBlock();
					if (block instanceof BlockSand) {
						world.setBlockState(pos0, Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND));
						flag = true;
					}
					else if (block instanceof BlockSandStone) {
						world.setBlockState(pos0, Blocks.RED_SANDSTONE.getDefaultState());
						flag = true;
					}
					else if (block instanceof BlockDoubleStoneSlab && world.getBlockState(pos0).getValue(BlockDoubleStoneSlab.VARIANT) == BlockStoneSlab.EnumType.SAND) {
						world.setBlockState(pos0, Blocks.DOUBLE_STONE_SLAB2.getDefaultState());
						flag = true;
					}
					else if (block instanceof BlockStoneSlab && world.getBlockState(pos0).getValue(BlockStoneSlab.VARIANT) == BlockStoneSlab.EnumType.SAND) {
						world.setBlockState(pos0, Blocks.STONE_SLAB2.getDefaultState().withProperty(BlockSlab.HALF, world.getBlockState(pos0).getValue(BlockSlab.HALF)));
						flag = true;
					}
					else if (block == Blocks.SANDSTONE_STAIRS) {
						world.setBlockState(pos0, Blocks.RED_SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.HALF, world.getBlockState(pos0).getValue(BlockStairs.HALF)).withProperty(BlockStairs.FACING, world.getBlockState(pos0).getValue(BlockStairs.FACING)).withProperty(BlockStairs.SHAPE, world.getBlockState(pos0).getValue(BlockStairs.SHAPE)));
						flag = true;
					}
					else if(block instanceof BlockColored) {
						world.setBlockState(pos0, block.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.RED));
					}
				}
			}
		}
		return flag;
	}
}