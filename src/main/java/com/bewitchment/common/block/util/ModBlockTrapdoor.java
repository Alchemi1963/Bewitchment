package com.bewitchment.common.block.util;

import com.bewitchment.Bewitchment;
import com.bewitchment.Util;
import com.bewitchment.common.item.tool.ItemJuniperKey;
import com.bewitchment.registry.ModObjects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings({"deprecation", "NullableProblems"})
public class ModBlockTrapdoor extends BlockTrapDoor {
	public ModBlockTrapdoor(String name, Block base, String... oreDictionaryNames) {
		super(base.getDefaultState().getMaterial());
		Util.registerBlock(this, name, base, oreDictionaryNames);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return Util.isTransparent(getDefaultState()) ? BlockRenderLayer.TRANSLUCENT : BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		return (!Util.isTransparent(state) || world.getBlockState(pos.offset(face)).getBlock() != this) && super.shouldSideBeRendered(state, world, pos, face);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ) {
		if (this == ModObjects.juniper_trapdoor) {
			boolean found = false;
			for (ItemStack stack : Bewitchment.proxy.getEntireInventory(player)) {
				if (ItemJuniperKey.canAccess(world, pos, world.provider.getDimension(), stack)) {
					found = true;
					break;
				}
			}
			if (!found) {
				if (!world.isRemote) player.sendStatusMessage(new TextComponentTranslation("juniper_key.invalid"), true);
				return true;
			}
		}
		return super.onBlockActivated(world, pos, state, player, hand, face, hitX, hitY, hitZ);
	}
	
	@Override
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World world, BlockPos pos) {
		float val = super.getPlayerRelativeBlockHardness(state, player, world, pos);
		if (this == ModObjects.juniper_trapdoor) {
			for (ItemStack stack : Bewitchment.proxy.getEntireInventory(player)) if (ItemJuniperKey.canAccess(world, pos, player.dimension, stack)) return val;
			return -1;
		}
		return val;
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, placer, stack);
		if (this == ModObjects.juniper_trapdoor && placer instanceof EntityPlayer) Util.giveItem((EntityPlayer) placer, ItemJuniperKey.setTags(world, pos, new ItemStack(ModObjects.juniper_key)));
	}
}