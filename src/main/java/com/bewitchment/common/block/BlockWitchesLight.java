package com.bewitchment.common.block;

import com.bewitchment.Util;
import com.bewitchment.common.item.ItemLantern;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

@SuppressWarnings("ConstantConditions")
public class BlockWitchesLight extends BlockAir {
	public BlockWitchesLight() {
		super();
		setLightLevel(1);
		Util.registerBlock(this, "witches_light", Blocks.AIR);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		ItemStack main = Minecraft.getMinecraft().player.getHeldItemMainhand(), off = Minecraft.getMinecraft().player.getHeldItemOffhand();
		if ((main.getItem() instanceof ItemLantern && main.hasTagCompound() && main.getTagCompound().getBoolean("lit")) || (off.getItem() instanceof ItemLantern && off.hasTagCompound() && off.getTagCompound().getBoolean("lit")))
			world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, pos.getX() + 0.5 + rand.nextGaussian() * 0.2, pos.getY() + rand.nextDouble() * 0.6 + 0.2, pos.getZ() + 0.5 + rand.nextGaussian() * 0.2, 0, 0, 0);
	}
}