package com.bewitchment;

import net.minecraft.init.Blocks;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("WeakerAccess")
@Config(modid = Bewitchment.MODID, name = Bewitchment.NAME)
public class ModConfig {
	//todo, add localization via. @Config.LangKey, might also specify which require a world/mc restart
	
	@Config.Comment("Miscellaneous settings")
	public static final Misc misc = new Misc();
	
	@Config.Comment("World generation settings")
	public static final WorldGen worldGen = new WorldGen();
	
	@Config.Comment("Mob spawn settings")
	public static final MobSpawns mobSpawns = new MobSpawns();

	@Config.Comment("Compat Settings")
	public static final Compat compat = new Compat();
	
	@Config.Comment("Ael is banned")
	public static final Maymays memes = new Maymays();
	
	public static class Misc {
		@Config.Comment("The list of blocks that the broom will sweep when right clicked on.")
		public String[] broomSweepables = {Blocks.REDSTONE_WIRE.getTranslationKey(), "tile.bewitchment.glyph", "tile.bewitchment.salt_barrier"};
		
		@Config.Comment("The list of blocks that the witches' cauldron will count as heat sources.")
		public String[] heatSources = {Blocks.FIRE.getTranslationKey(), Blocks.LAVA.getTranslationKey(), Blocks.MAGMA.getTranslationKey()};
		
		@Config.Comment("The amount of blocks an altar should scan per tick.")
		@Config.RangeInt(min = 0, max = Short.MAX_VALUE)
		public int altarScansPerTick = 64;
		
		@Config.Comment("The maximum power a Grimoire Magia can have.")
		@Config.RangeInt(min = 0)
		public int maxGrimoirePower = 1000;
	}
	
	public static class WorldGen {
		public final TreeGen treeGen = new TreeGen();
		public final OreGen oreGen = new OreGen();
		@Config.Comment("The dimensions that trees, ores, and coquina are allowed to spawn in.")
		public Integer[] worldGenWhitelist = {0};
		@Config.Comment("Allow bewitchment structures to be generated in the OverWorld")
		public boolean enableStructures = true;
		
		public static class TreeGen {
			@Config.Comment("The biome IDs that Bewitchment trees are blacklisted from spawning")
			public Integer[] treeGenBlacklist = {};
			
			@Config.Comment("The chance for cypress trees to spawn. Set to 0 to disable.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int cypressChance = 20;
			
			@Config.Comment("The chance for elder trees to spawn. Set to 0 to disable.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int elderChance = 20;
			
			@Config.Comment("The chance for juniper trees to spawn. Set to 0 to disable.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int juniperChance = 20;
		}
		
		public static class OreGen {
			
			@Config.Comment("The size of silver ore veins.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int silverSize = 7;
			
			@Config.Comment("The chance for silver ore veins to spawn. 0 to disable.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int silverChance = 12;
			
			@Config.Comment("The minimum height for silver ore veins to spawn.")
			@Config.RangeInt(min = 0, max = 255)
			public int silverMin = 10;
			
			@Config.Comment("The maximum height for silver ore veins to spawn.")
			@Config.RangeInt(min = 0, max = 255)
			public int silverMax = 128;
			
			
			@Config.Comment("The size of salt ore veins.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int saltSize = 5;
			
			@Config.Comment("The chance for salt ore veins to spawn. 0 to disable.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int saltChance = 10;
			
			@Config.Comment("The minimum height for salt ore veins to spawn.")
			@Config.RangeInt(min = 0, max = 255)
			public int saltMin = 10;
			
			@Config.Comment("The maximum height for salt ore veins to spawn.")
			@Config.RangeInt(min = 0, max = 255)
			public int saltMax = 120;
			
			
			@Config.Comment("The size of amethyst ore veins.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int amethystSize = 6;
			
			@Config.Comment("The chance for amethyst ore veins to spawn. 0 to disable.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int amethystChance = 2;
			
			@Config.Comment("The minimum height for amethyst ore veins to spawn.")
			@Config.RangeInt(min = 0, max = 255)
			public int amethystMin = 10;
			
			@Config.Comment("The maximum height for amethyst ore veins to spawn.")
			@Config.RangeInt(min = 0, max = 255)
			public int amethystMax = 42;
			
			
			@Config.Comment("The size of garnet ore veins.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int garnetSize = 6;
			
			@Config.Comment("The chance for garnet ore veins to spawn. 0 to disable.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int garnetChance = 2;
			
			@Config.Comment("The minimum height for garnet ore veins to spawn.")
			@Config.RangeInt(min = 0, max = 255)
			public int garnetMin = 12;
			
			@Config.Comment("The maximum height for garnet ore veins to spawn.")
			@Config.RangeInt(min = 0, max = 255)
			public int garnetMax = 42;
			
			
			@Config.Comment("The size of opal ore veins.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int opalSize = 6;
			
			@Config.Comment("The chance for opal ore veins to spawn. 0 to disable.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int opalChance = 2;
			
			@Config.Comment("The minimum height for opal ore veins to spawn.")
			@Config.RangeInt(min = 0, max = 255)
			public int opalMin = 16;
			
			@Config.Comment("The maximum height for opal ore veins to spawn.")
			@Config.RangeInt(min = 0, max = 255)
			public int opalMax = 42;
		}
	}
	
	public static class MobSpawns {
		public final Lizard lizard = new Lizard();
		public final Owl owl = new Owl();
		public final Raven raven = new Raven();
		public final Snek snake = new Snek();
		public final Toad toad = new Toad();
		public final BlackDog blackDog = new BlackDog();
		public final Ghost ghost = new Ghost();
		public final Hellhound hellhound = new Hellhound();
		public final Feuerwurm feuerwurm = new Feuerwurm();
		public final Druden druden = new Druden();
		
		public static class Lizard {
			@Config.Comment("The list of BiomeDictionary types that the lizard will spawn in.")
			public String[] lizardBiomes = {Type.FOREST.getName()};
			
			@Config.Comment("The weight chance for lizards to spawn.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int lizardWeight = 20;
			
			@Config.Comment("The minimum amount of lizards to spawn at once.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int lizardMin = 1;
			
			@Config.Comment("The maximum amount of lizards to spawn at once.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int lizardMax = 4;
		}
		
		public static class Owl {
			@Config.Comment("The list of BiomeDictionary types that the owl will spawn in.")
			public String[] owlBiomes = {Type.FOREST.getName(), Type.DENSE.getName()};
			
			@Config.Comment("The weight chance for owls to spawn.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int owlWeight = 20;
			
			@Config.Comment("The minimum amount of owls to spawn at once.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int owlMin = 1;
			
			@Config.Comment("The maximum amount of owls to spawn at once.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int owlMax = 4;
		}
		
		public static class Raven {
			@Config.Comment("The list of BiomeDictionary types that the raven will spawn in.")
			public String[] ravenBiomes = {Type.PLAINS.getName(), Type.WASTELAND.getName()};
			
			@Config.Comment("The weight chance for ravens to spawn.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int ravenWeight = 20;
			
			@Config.Comment("The minimum amount of ravens to spawn at once.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int ravenMin = 1;
			
			@Config.Comment("The maximum amount of ravens to spawn at once.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int ravenMax = 4;
		}
		
		public static class Snek {
			@Config.Comment("The list of BiomeDictionary types that the snake will spawn in.")
			public String[] snakeBiomes = {Type.PLAINS.getName(), Type.HILLS.getName()};
			
			@Config.Comment("The weight chance for snakes to spawn.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int snakeWeight = 20;
			
			@Config.Comment("The minimum amount of snakes to spawn at once.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int snakeMin = 1;
			
			@Config.Comment("The maximum amount of snakes to spawn at once.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int snakeMax = 4;
		}
		
		public static class Toad {
			@Config.Comment("The list of BiomeDictionary types that the toad will spawn in.")
			public String[] toadBiomes = {Type.SWAMP.getName()};
			
			@Config.Comment("The weight chance for toads to spawn.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int toadWeight = 20;
			
			@Config.Comment("The minimum amount of toads to spawn at once.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int toadMin = 1;
			
			@Config.Comment("The maximum amount of toads to spawn at once.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int toadMax = 4;
		}
		
		public static class BlackDog {
			@Config.Comment("The list of BiomeDictionary types that the black dog will spawn in.")
			public String[] blackDogBiomes = {Type.PLAINS.getName(), Type.WASTELAND.getName(), Type.FOREST.getName(), Type.SPOOKY.getName()};
			
			@Config.Comment("The weight chance for black dogs to spawn.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int blackDogWeight = 6;
			
			@Config.Comment("The minimum amount of black dogs to spawn at once.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int blackDogMin = 1;
			
			@Config.Comment("The maximum amount of black dogs to spawn at once.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int blackDogMax = 4;
		}
		
		
		public static class Ghost {
			@Config.Comment("The list of BiomeDictionary types that the ghost will spawn in.")
			public String[] ghostBiomes = {Type.PLAINS.getName(), Type.WASTELAND.getName(), Type.FOREST.getName(), Type.SPOOKY.getName()};
			
			@Config.Comment("The weight chance for ghosts to spawn.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int ghostWeight = 6;
			
			@Config.Comment("The minimum amount of ghosts to spawn at once.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int ghostMin = 1;
			
			@Config.Comment("The maximum amount of ghosts to spawn at once.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int ghostMax = 8;
		}
		
		public static class Hellhound {
			@Config.Comment("The list of BiomeDictionary types that the hellhound will spawn in.")
			public String[] hellhoundBiomes = {Type.NETHER.getName()};
			
			@Config.Comment("The weight chance for hellhounds to spawn.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int hellhoundWeight = 6;
			
			@Config.Comment("The minimum amount of hellhounds to spawn at once.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int hellhoundMin = 1;
			
			@Config.Comment("The maximum amount of hellhounds to spawn at once.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int hellhoundMax = 4;
		}
		
		public static class Feuerwurm {
			@Config.Comment("The list of BiomeDictionary types that the feuerwurm will spawn in.")
			public String[] feuerwurmBiomes = {Type.NETHER.getName()};
			
			@Config.Comment("The weight chance for feuerwurms to spawn.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int feuerwurmWeight = 6;
			
			@Config.Comment("The minimum amount of feuerwurms to spawn at once.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int feuerwurmMin = 1;
			
			@Config.Comment("The maximum amount of feuerwurms to spawn at once.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int feuerwurmMax = 4;
		}
		
		public static class Druden {
			@Config.Comment("The list of BiomeDictionary types that the druden will spawn in.")
			public String[] drudenBiomes = {Type.FOREST.getName(), Type.DENSE.getName()};
			
			@Config.Comment("The weight chance for druden to spawn.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int drudenWeight = 1;
			
			@Config.Comment("The minimum amount of druden to spawn at once.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int drudenMin = 0;
			
			@Config.Comment("The maximum amount of druden to spawn at once.")
			@Config.RangeInt(min = 0, max = Byte.MAX_VALUE)
			public int drudenMax = 1;
		}
	}
	
	public static class Maymays {
		@Config.RequiresMcRestart
		@Config.Comment("Decides whether or not the cats and dogs will be enabled.")
		public boolean enableCatsAndDogsFortune = false;
		
		@Config.RequiresMcRestart
		@Config.Comment("it is wednesday my dudes")
		public boolean wednesday = false;
	}

	public static class Compat {
		@Config.RequiresMcRestart
		@Config.Comment("Enable replacing saplings with dynamic trees seeds upon planting (Must have DynamicTrees installed")
		public boolean replaceSapling = true;

		@Config.RequiresMcRestart
		@Config.Comment("Enable world gen of dynamic Bewitchment trees instead of the regular trees")
		public boolean genDynamic = true;
	}
	
	@Mod.EventBusSubscriber(modid = Bewitchment.MODID)
	private static class SyncConfig {
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Bewitchment.MODID)) {
				ConfigManager.sync(Bewitchment.MODID, net.minecraftforge.common.config.Config.Type.INSTANCE);
			}
		}
	}
}