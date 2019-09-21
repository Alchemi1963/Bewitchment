package com.bewitchment.api.capability.extendedplayer;

import com.bewitchment.Bewitchment;
import com.bewitchment.Util;
import com.bewitchment.api.registry.Curse;
import com.bewitchment.registry.ModObjects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@SuppressWarnings({"ConstantConditions", "unused"})
public class ExtendedPlayerHandler {
	private static final ResourceLocation LOC = new ResourceLocation(Bewitchment.MODID, "extended_player");
	
	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityPlayer) event.addCapability(LOC, new ExtendedPlayer());
	}
	
	@SubscribeEvent
	public void clonePlayer(PlayerEvent.Clone event) {
		event.getEntityPlayer().getCapability(ExtendedPlayer.CAPABILITY, null).deserializeNBT(event.getOriginal().getCapability(ExtendedPlayer.CAPABILITY, null).serializeNBT());
	}
	
	@SubscribeEvent
	public void playerTick(TickEvent.PlayerTickEvent event) {
		if (!event.player.world.isRemote && event.phase == TickEvent.Phase.END) {
			ExtendedPlayer cap = event.player.getCapability(ExtendedPlayer.CAPABILITY, null);
			if (cap.fortune != null) {
				if (event.player.world.getTotalWorldTime() % 20 == 0) cap.fortuneTime--;
				if (cap.fortuneTime == 0) {
					if (cap.fortune.apply(event.player)) cap.fortune = null;
					else cap.fortuneTime = (event.player.getRNG().nextInt(cap.fortune.maxTime - cap.fortune.minTime) + cap.fortune.minTime);
					ExtendedPlayer.syncToClient(event.player);
				}
			}
			if(cap.curses != null) { //check "curse condition"
				for(Curse curse : cap.getCurses()) {
					if (curse.getCurseCondition() == Curse.CurseCondition.EXIST)
						curse.doCurse(event.player);
					if (event.player.world.getTotalWorldTime() % 24000 == 0){
						System.out.println("Day passed");
						cap.updateCurses();
					}
				}
			}
			if (event.player.ticksExisted % 20 == 0) {
				NBTTagList list = cap.exploredChunks;
				long pos = ChunkPos.asLong(event.player.chunkCoordX, event.player.chunkCoordZ);
				boolean found = false;
				for (int i = 0; i < list.tagCount(); i++) {
					if (((NBTTagLong) list.get(i)).getLong() == pos) {
						found = true;
						break;
					}
				}
				if (!found) {
					list.appendTag(new NBTTagLong(pos));
					ExtendedPlayer.syncToClient(event.player);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		if (!event.getEntityLiving().world.isRemote && event.getSource().getTrueSource() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
			if (!event.getEntityLiving().isNonBoss()) {
				NBTTagList list = player.getCapability(ExtendedPlayer.CAPABILITY, null).uniqueDefeatedBosses;
				String name = EntityRegistry.getEntry(event.getEntityLiving().getClass()).getName();
				boolean found = false;
				for (int i = 0; i < list.tagCount(); i++) {
					if (list.getStringTagAt(i).equals(name)) {
						found = true;
						break;
					}
				}
				if (!found) {
					list.appendTag(new NBTTagString(name));
					ExtendedPlayer.syncToClient(player);
				}
			}
			if (event.getEntityLiving() instanceof EntityMob) {
				player.getCapability(ExtendedPlayer.CAPABILITY, null).mobsKilled++;
				ExtendedPlayer.syncToClient(player);
			}
			
			// This should probably go somewhere else
			if (event.getEntityLiving() instanceof EntityAnimal || event.getEntityLiving() instanceof EntityPlayer || event.getEntityLiving() instanceof EntityVillager) {
				if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == ModObjects.athame && player.getHeldItem(EnumHand.OFF_HAND).getItem() == Items.GLASS_BOTTLE) {
					Util.replaceAndConsumeItem(player, EnumHand.OFF_HAND, new ItemStack(ModObjects.bottle_of_blood));
				}
			}
		}
	}
}