package com.bewitchment.proxy;

import com.bewitchment.Bewitchment;
import com.bewitchment.client.model.block.ModelLeonardIdol;
import com.bewitchment.client.render.entity.living.*;
import com.bewitchment.client.render.entity.misc.RenderCypressBroom;
import com.bewitchment.client.render.entity.misc.RenderElderBroom;
import com.bewitchment.client.render.entity.misc.RenderJuniperBroom;
import com.bewitchment.client.render.entity.misc.RenderYewBroom;
import com.bewitchment.client.render.entity.spirit.demon.*;
import com.bewitchment.client.render.entity.spirit.ghost.RenderBlackDog;
import com.bewitchment.client.render.fx.ModParticleBubble;
import com.bewitchment.client.render.tile.*;
import com.bewitchment.common.block.BlockGlyph;
import com.bewitchment.common.block.tile.entity.*;
import com.bewitchment.common.entity.living.*;
import com.bewitchment.common.entity.misc.EntityCypressBroom;
import com.bewitchment.common.entity.misc.EntityElderBroom;
import com.bewitchment.common.entity.misc.EntityJuniperBroom;
import com.bewitchment.common.entity.misc.EntityYewBroom;
import com.bewitchment.common.entity.spirit.demon.*;
import com.bewitchment.common.entity.spirit.ghost.EntityBlackDog;
import com.bewitchment.registry.ModObjects;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@SuppressWarnings({"ConstantConditions", "unused", "WeakerAccess"})
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends ServerProxy {
	public static final Map<Item, ModelBase> IDOL_MODELS = new HashMap<>();
	public static final Map<Item, ResourceLocation> IDOL_TEXTURES = new HashMap<>();
	
	@Override
	public List<ItemStack> getEntireInventory(EntityPlayer unused) {
		return super.getEntireInventory(Minecraft.getMinecraft().player);
	}
	
	public boolean doesPlayerHaveAdvancement(EntityPlayer player, ResourceLocation name) {
		if (player instanceof EntityPlayerSP) {
			ClientAdvancementManager manager = ((EntityPlayerSP) player).connection.getAdvancementManager();
			Advancement adv = manager.getAdvancementList().getAdvancement(name);
			if (adv != null) {
				AdvancementProgress progress = ObfuscationReflectionHelper.getPrivateValue(ClientAdvancementManager.class, manager, "advancementToProgress", "field_192803_d");
				return progress != null && progress.isDone();
			}
		}
		return super.doesPlayerHaveAdvancement(player, name);
	}
	
	@Override
	public boolean isFancyGraphicsEnabled() {
		return Minecraft.getMinecraft().gameSettings.fancyGraphics;
	}
	
	@Override
	public void registerRendersPreInit() {
		RenderingRegistry.registerEntityRenderingHandler(EntityCypressBroom.class, RenderCypressBroom::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityElderBroom.class, RenderElderBroom::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityJuniperBroom.class, RenderJuniperBroom::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityYewBroom.class, RenderYewBroom::new);
		
		RenderingRegistry.registerEntityRenderingHandler(EntityLizard.class, RenderLizard::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityNewt.class, RenderNewt::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityOwl.class, RenderOwl::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityRaven.class, RenderRaven::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySnake.class, RenderSnake::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityToad.class, RenderToad::new);
		
		RenderingRegistry.registerEntityRenderingHandler(EntityBlackDog.class, RenderBlackDog::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityHellhound.class, RenderHellhound::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySerpent.class, RenderSerpent::new);
		
		RenderingRegistry.registerEntityRenderingHandler(EntityDemon.class, RenderDemon::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDemoness.class, RenderDemoness::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityImp.class, RenderImp::new);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGlyph.class, new RenderTileEntityGlyph());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWitchesCauldron.class, new RenderTileEntityWitchesCauldron());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityJuniperChest.class, new RenderTileEntityJuniperChest());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPlacedItem.class, new RenderTileEntityPlacedItem());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityIdol.class, new RenderTileEntityIdol());
	}
	
	@Override
	public void registerRendersInit() {
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((state, world, pos, tintIndex) -> {
			int type = state.getValue(BlockGlyph.TYPE);
			return type == BlockGlyph.GOLDEN ? 0xe3dc3c : type == BlockGlyph.NETHER ? 0xbb0000 : type == BlockGlyph.ENDER ? 0x770077 : 0xffffff;
		}, ModObjects.glyph);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) -> tintIndex == 0 ? 0xe6c44f : 0xffffff, ModObjects.snake_venom);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) -> tintIndex == 0 ? 0x717d39 : 0xffffff, ModObjects.liquid_wroth);
		
		ModelBase lenny = new ModelLeonardIdol();
		registerIdol(ModObjects.stone_leonard_idol, lenny, new ResourceLocation(Bewitchment.MODID, "textures/blocks/idol/leonard/stone.png"));
		registerIdol(ModObjects.clay_leonard_idol, lenny, new ResourceLocation(Bewitchment.MODID, "textures/blocks/idol/leonard/clay.png"));
		registerIdol(ModObjects.gold_leonard_idol, lenny, new ResourceLocation(Bewitchment.MODID, "textures/blocks/idol/leonard/gold.png"));
		registerIdol(ModObjects.nether_brick_leonard_idol, lenny, new ResourceLocation(Bewitchment.MODID, "textures/blocks/idol/leonard/nether_brick.png"));
		registerIdol(ModObjects.nethersteel_leonard_idol, lenny, new ResourceLocation(Bewitchment.MODID, "textures/blocks/idol/leonard/nethersteel.png"));
		registerIdol(ModObjects.scorned_brick_leonard_idol, lenny, new ResourceLocation(Bewitchment.MODID, "textures/blocks/idol/leonard/scorned_brick.png"));
	}
	
	@Override
	public void registerTexture(Item item, String variant) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), variant));
	}
	
	@Override
	public void registerTextureVariant(Item item, List<Predicate<ItemStack>> predicates) {
		ResourceLocation[] names = new ResourceLocation[predicates.size() + 1];
		for (int i = 0; i <= predicates.size(); i++)
			names[i] = new ResourceLocation(item.getRegistryName().toString() + (i == 0 ? "" : "_variant" + (predicates.size() == 1 ? "" : (i - 1))));
		ModelBakery.registerItemVariants(item, names);
		ModelLoader.setCustomMeshDefinition(item, stack -> {
			for (int i = 0; i < predicates.size(); i++)
				if (predicates.get(i).test(stack)) return new ModelResourceLocation(names[i + 1], "inventory");
			return new ModelResourceLocation(item.getRegistryName(), "inventory");
		});
	}
	
	@Override
	public void ignoreProperty(Block block, IProperty<?>... properties) {
		ModelLoader.setCustomStateMapper(block, new StateMap.Builder().ignore(properties).build());
	}
	
	@SubscribeEvent
	public static void stitch(TextureStitchEvent.Pre event) {
		event.getMap().registerSprite(RenderTileEntityWitchesCauldron.TEX);
		event.getMap().registerSprite(ModParticleBubble.TEX);
	}
	
	public static void registerIdol(Item item, ModelBase model, ResourceLocation texture) {
		IDOL_MODELS.put(item, model);
		IDOL_TEXTURES.put(item, texture);
	}
}