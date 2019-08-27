package com.bewitchment.common.block.util;

import com.bewitchment.Util;
import com.bewitchment.common.item.tool.ItemBoline;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings({"deprecation", "NullableProblems"})
public class ModBlockLog extends BlockLog {
	public static final PropertyBool IS_NATURAL = PropertyBool.create("is_natural");
	public static final PropertyBool IS_SLASHED = PropertyBool.create("is_slashed");

	public ModBlockLog(String name, Block base, String... oreDictionaryNames) {
		super();
		Util.registerBlock(this, name, base, oreDictionaryNames);
		setDefaultState(getBlockState().getBaseState().withProperty(LOG_AXIS, EnumAxis.Y).withProperty(IS_NATURAL, true).withProperty(IS_SLASHED, false));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return Util.isTransparent(getDefaultState()) ? BlockRenderLayer.TRANSLUCENT : BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getMaterial() == Material.WOOD;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return !Util.isTransparent(state) && super.isFullCube(state);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return !Util.isTransparent(state) && super.isOpaqueCube(state);
	}
	
	@Override
	public boolean isWood(IBlockAccess world, BlockPos pos) {
		return world.getBlockState(pos).getMaterial() == Material.WOOD;
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		return (!Util.isTransparent(state) || world.getBlockState(pos.offset(face)).getBlock() != this) && super.shouldSideBeRendered(state, world, pos, face);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(playerIn.getHeldItem(hand).getItem() instanceof ItemBoline && state.getValue(IS_NATURAL) && !state.getValue(IS_SLASHED)) {
			worldIn.setBlockState(pos, state.withProperty(IS_SLASHED, true));
			return true;
		}
		return false;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState iblockstate = this.getDefaultState();
		switch(meta & 12) {
			case 0:
				iblockstate = iblockstate.withProperty(LOG_AXIS, EnumAxis.Y);
				break;
			case 4:
				iblockstate = iblockstate.withProperty(LOG_AXIS, EnumAxis.X);
				break;
			case 8:
				iblockstate = iblockstate.withProperty(LOG_AXIS, EnumAxis.Z);
				break;
			default:
				iblockstate = iblockstate.withProperty(LOG_AXIS, EnumAxis.NONE);
		}
		if ((meta & 1) == 0) {
			iblockstate = iblockstate.withProperty(IS_NATURAL, false);
		} else {
			iblockstate = iblockstate.withProperty(IS_NATURAL, true);
		}
		if ((meta & 2) == 0) {
			iblockstate = iblockstate.withProperty(IS_SLASHED, false);
		} else {
			iblockstate = iblockstate.withProperty(IS_SLASHED, true);
		}
		return iblockstate;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;
		switch(state.getValue(LOG_AXIS)) {
			case X:
				i |= 4;
				break;
			case Z:
				i |= 8;
				break;
			case NONE:
				i |= 12;
		}
		if(state.getValue(IS_NATURAL)) i |= 1;
		if(state.getValue(IS_SLASHED)) i |= 2;
		return i;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, IS_NATURAL, IS_SLASHED, LOG_AXIS);
	}
}