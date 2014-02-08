package net.fossilsarch;
//all java files for minecraft should have this.
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.network.IChatListener;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.fossilsarch.client.DinoSoundHandler;
import net.fossilsarch.client.model.*;
import net.fossilsarch.client.render.*;
import net.fossilsarch.common.FossilOptions;
import net.fossilsarch.common.IDException;
import net.fossilsarch.common.block.*;
import net.fossilsarch.common.entity.*;
import net.fossilsarch.common.handlers.FossilBlockRenderHandler;
import net.fossilsarch.common.handlers.FossilGuiHandler;
import net.fossilsarch.common.handlers.FossilMessageHandler;
import net.fossilsarch.common.tileentity.TileEntityAnalyzer;
import net.fossilsarch.common.tileentity.TileEntityCultivate;
import net.fossilsarch.common.tileentity.TileEntityDrum;
import net.fossilsarch.common.tileentity.TileEntityFeeder;
import net.fossilsarch.common.tileentity.TileEntityWorktable;
import net.fossilsarch.common.worldgen.WorldGenAcademy;
import net.fossilsarch.common.worldgen.WorldGenShipWreck;
import net.fossilsarch.common.worldgen.WorldGenWeaponShopA;
import net.fossilsarch.common.io.EnumDinoType;
import net.fossilsarch.common.io.EnumAnimalType;
import net.fossilsarch.common.io.EnumOrderType;
import net.fossilsarch.common.io.EnumEmbyos;
import net.fossilsarch.common.item.*;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderPig;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.resources.Locale;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraft.stats.Achievement;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

//because we are using random values below.
@Mod(modid = "633",name="Fossil/Archeology",version="$VERSION$")
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class mod_Fossil {

    static {
    	
        PigbossOnEarth = (new Achievement(18000, "PigbossOnEarth", 0,0, new ItemStack(Item.dyePowder, 1, 4), null)).registerAchievement();
    }
    public static int blockRendererID=0;
    public static final String DEFAULT_LANG="en_US";    
    public static String LastLangSetting=DEFAULT_LANG; 
    public static Properties LangProps=new Properties(); 
	private static final File Langdir=new File("/Fossillang/");
    private static File Langfile=new File(Langdir,LastLangSetting+".lang");
    public static FossilGuiHandler GH=new FossilGuiHandler();
    public static Object INSTANCE;
    //public static FossilKeyHandler keyBindingService=new FossilKeyHandler();
    public static boolean DebugMode = true;
    public static IChatListener messagerHandler=new FossilMessageHandler();
    private static int[] BlockIDs = {1137,1138,1139,1140,1141,1142,1143,1144,1145,1146,1147,1148,1149,1151,1152,1153};
    
    private static String[] BlockName = {
        "block Fossil",
        "block Skull",
        "block SkullLantern",
        "block analyzerIdle",
        "block analyzerActive",
        "block cultivateIdle",
        "block cultivateActive",
        "block worktableIdle",
        "block worktableActive",
        "block fern",
        "block fernUpper",
        "block Drum",
        "block FeederIdle",
        "block FeederActive",
        "Block Permafrost",
        "Block IcedStone"
    };
    //110,111 for ferns
    //112 for dumps
    //113,114 for feeder
    private static int[] ItemIDs = {3000, 3001, 3002, 3003, 3004, 3005, 3006, 3007, 3008, 3009, 3010, 3011, 3012, 3013, 3014, 3015, 3016, 3017, 3018, 3019, 3020, 3021, 3022, 3023, 3024, 3025, 3026, 3027, 3028,3029,3030,3031,3032,3033,3034,3035,3036};
    private static String[] ItemName = {
        "Item biofossil",
        "Item relic",
        "Item stoneboard",
        "Item DNA",
        "Item Ancientegg",
        "Item AncientSword",
        "Item BrokenSword",
        "Item FernSeed",
        "Item AncientHelmet",
        "Item BrokenHelmet",
        "Item SkullStick",
        "Item Gen",
        "Item GenAxe",
        "Item GenPickaxe",
        "Item GenSword",
        "Item GenHoe",
        "Item GenShovel",
        "Item DinoPedia",
        "Item TRexTooth",
        "Item ToothDagger",
        "Item RawChickenSoup",
        "Item ChickenEss",
        "Item EmptyShell",
        "Item Magic conch",
        "Item SJL",
        "Item RawDinoMeat",
        "Item CookedDinoMeat",
        "Item EmbyoSyringe",
        "Item AnimalDNA",
        "Item IcedMeat",
        "Item WoodJavelin",
        "Item StonrJavelin",
        "Item IronJavelin",
        "Item GoldJavelin",
        "Item DiamondJavelin",
        "Item AncientJavelin",
        "Item Whip"
    };
    //607 for fern seed
    //608 for ancient helmet
    //609 for ancient broken helmet
    //610 for skull stick

    public static final int MutiCount = EnumDinoType.values().length * 3 + EnumAnimalType.values().length * 2 - 1+2;
    public static final int BlockCount = BlockIDs.length - 5;
    public static Block blockFossil;
    public static Block blockSkull;
    public static Block SkullLantern;
    //3.0 contents
    public static Item biofossil;
    public static Item relic;
    public static Item stoneboard;
    public static Item DNA;
    public static Item Ancientegg;
    public static Item AncientSword;
    public static Item BrokenSword;
    public static Item FernSeed;
    public static Item Ancienthelmet;
    public static Item Brokenhelmet;
    public static Block blockanalyzerIdle;
    public static Block blockanalyzerActive;
    public static Block blockcultivateIdle;
    public static Block blockcultivateActive;
    public static Block blockworktableIdle;
    public static Block blockworktableActive;
    public static Block Ferns;
    public static Block FernUpper;
    //4.0 contents
    public static Block Dump;
    public static Block FeederIdle;
    public static Block FeederActive;
    public static Item SkullStick;
    public static Item Gen;
    public static Item GenAxe;
    public static Item GenPickaxe;
    public static Item GenSword;
    public static Item GenHoe;
    public static Item GenShovel;
    public static Item DinoPedia;
    //5.0 contents
    public static Block blockPermafrost;
    public static Block blockIcedStone;
    public static Item ChickenSoup;
    public static Item ChickenEss;
    public static Item EmptyShell;
    public static Item SJL;
    public static Item MagicConch;
    public static Item RawDinoMeat;
    public static Item CookedDinoMeat;
    public static Item EmbyoSyringe;
    public static Item AnimalDNA;
    public static Item IcedMeat;
    public static Item Woodjavelin;
    public static Item Stonejavelin;
    public static Item Ironjavelin;
    public static Item Goldjavelin;
    public static Item Diamondjavelin;
    public static Item AncientJavelin;
    public static Item Whip;
    public static Achievement PigbossOnEarth;
    public static AchievementPage selfArcPage=new AchievementPage("FOSSIL / ARCHEOLOGY",PigbossOnEarth);
	public static final double MESSAGE_DISTANCE = 25.0d;
    public static ItemStack[] SingleTotalList;
    public static ItemStack[] MutiTotalList = new ItemStack[MutiCount];
    public static WorldGenWeaponShopA WeaponShopA=new WorldGenWeaponShopA();
   public static WorldGenShipWreck ShipA=new WorldGenShipWreck();
    public static WorldGenAcademy AcademyA=new WorldGenAcademy();
	private IWorldGenerator FossilGenerator=new net.fossilsarch.common.worldgen.FossilGenerator();
    public mod_Fossil() {
    }

    public static void fillCreativeList() {
        int i, j, pt = 0;
        for (i = 0; i < EnumDinoType.values().length; i++) {
            MutiTotalList[pt++] = new ItemStack(DNA, 1, i);
        }
        for (i = 0; i < EnumDinoType.values().length; i++) {
            MutiTotalList[pt++] = new ItemStack(Ancientegg, 1, i);
        }
        for (i = 0; i < EnumDinoType.values().length; i++) {
            MutiTotalList[pt++] = new ItemStack(RawDinoMeat, 1, i);
        }
        for (j = 0; j < EnumAnimalType.values().length; j++) {
            MutiTotalList[pt++] = new ItemStack(AnimalDNA, 1, j);
        }
        for (j = 0; j < EnumEmbyos.values().length; j++) {
            MutiTotalList[pt++] = new ItemStack(EmbyoSyringe, 1, j);
        }
        for (j = 0; j < 2; j++) {
            MutiTotalList[pt++] = new ItemStack(ChickenSoup, 1, j);
        }
        SingleTotalList = new ItemStack[]{
                new ItemStack(blockFossil, 1, 0),
                new ItemStack(blockSkull, 1, 0),
                new ItemStack(SkullLantern, 1, 0),
                new ItemStack(blockanalyzerIdle, 1, 0),
                new ItemStack(blockcultivateIdle, 1, 0),
                new ItemStack(blockworktableIdle, 1, 0),
                new ItemStack(Ferns, 1, 0),
                new ItemStack(Dump, 1, 0),
                new ItemStack(FeederIdle, 1, 0),
                new ItemStack(blockPermafrost, 1, 0),
                new ItemStack(blockIcedStone, 1, 0),};
    }

    public String getVersion() //every mod for modloader need this function,too.
    //or modloader will refuse to load this mod.
    {
        return "v6.8 Patch-1";
    }
    @SideOnly(Side.CLIENT)
    public void registingRenderer(){  
    	RenderingRegistry.registerEntityRenderingHandler(EntityStoneboard.class, new RenderStoneboard());
        RenderingRegistry.registerEntityRenderingHandler(EntityTriceratops.class, new RenderTriceratops(new ModelTriceratops(), new ModelTriceratops(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityRaptor.class, new RenderRaptor(new ModelRaptor(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityTRex.class, new RenderTRex(new ModelTRex(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityFailuresaurus.class, new RenderFailuresaurus(new ModelFailuresaurus(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityPigBoss.class, new RenderPigBoss(new ModelPigBoss(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityFriendlyPigZombie.class, new RenderBiped(new ModelZombie(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityPterosaur.class, new RenderPterosaur(new ModelPterosaurGround(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityNautilus.class, new RenderNautilus(new ModelNautilus(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityPlesiosaur.class, new RenderPlesiosaur(0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityMosasaurus.class, new RenderMosasaurus(0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityStegosaurus.class, new RenderStegosaurus(new ModelStegosaurus(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityDinoEgg.class, new RenderDinoEgg(1.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityPregnantPig.class, new RenderPig(new ModelPig(), new ModelPig(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(Entitydil.class, new Renderdil(new Modeldil(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntitySaberCat.class, new RenderSaberCat(new ModelSaberCat(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityJavelin.class, new RenderJavelin());
        RenderingRegistry.registerEntityRenderingHandler(EntityAncientJavelin.class, new RenderJavelin());
        RenderingRegistry.registerEntityRenderingHandler(EntityBones.class, new RenderBones());
        RenderingRegistry.registerEntityRenderingHandler(EntityBrachiosaurus.class, new RenderBrachiosaurus(0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityMammoth.class, new RenderMammoth(new ModelMammoth(), 0.5F));
    }
    @SideOnly(Side.CLIENT)
    public void addRenderer(Map map) {
    	RenderingRegistry.instance().registerBlockHandler(new FossilBlockRenderHandler());
    	
        map.put(EntityStoneboard.class, new RenderStoneboard());
        map.put(EntityTriceratops.class, new RenderTriceratops(new ModelTriceratops(), new ModelTriceratops(), 0.5F));
        map.put(EntityRaptor.class, new RenderRaptor(new ModelRaptor(), 0.5F));
        map.put(EntityTRex.class, new RenderTRex(new ModelTRex(), 0.5F));
        map.put(EntityFailuresaurus.class, new RenderFailuresaurus(new ModelFailuresaurus(), 0.5F));
        map.put(EntityPigBoss.class, new RenderPigBoss(new ModelPigBoss(), 0.5F));
        map.put(EntityFriendlyPigZombie.class, new RenderBiped(new ModelZombie(), 0.5F));
        map.put(EntityPterosaur.class, new RenderPterosaur(new ModelPterosaurGround(), 0.5F));
        map.put(EntityNautilus.class, new RenderNautilus(new ModelNautilus(), 0.5F));
        map.put(EntityPlesiosaur.class, new RenderPlesiosaur(0.5F));
        map.put(EntityMosasaurus.class, new RenderMosasaurus(0.5F));
        map.put(EntityStegosaurus.class, new RenderStegosaurus(new ModelStegosaurus(), 0.5F));
        map.put(EntityDinoEgg.class, new RenderDinoEgg(1.5F));
        map.put(EntityPregnantPig.class, new RenderPig(new ModelPig(), new ModelPig(), 0.5F));
        map.put(Entitydil.class, new Renderdil(new Modeldil(), 0.5F));
        map.put(EntitySaberCat.class, new RenderSaberCat(new ModelSaberCat(), 0.5F));
        map.put(EntityJavelin.class, new RenderJavelin());
        map.put(EntityAncientJavelin.class, new RenderJavelin());
        map.put(EntityBones.class, new RenderBiped(new ModelBiped(),0.5F));
        map.put(EntityBrachiosaurus.class, new RenderBrachiosaurus(0.5F));
        map.put(EntityMammoth.class, new RenderMammoth(new ModelMammoth(), 0.5F));
    }

    private void ReadOptions(File configFile) {
    	Configuration config = new Configuration(configFile);
    	
    	for (int i = 0; i < BlockIDs.length; i++) {
    		BlockIDs[i] = config.getBlock(BlockName[i], BlockIDs[i]).getInt(BlockIDs[i]);
    	}
    	
    	for (int i = 0; i < ItemIDs.length; i++) {
    		ItemIDs[i] = config.getItem(ItemName[i], ItemIDs[i]).getInt(ItemIDs[i]);
    	}
    	
    	FossilOptions.ShouldAnuSpawn = config.get("general", "SpawnAnu", true).getBoolean(true);
    	FossilOptions.SpawnShipwrecks = config.get("general", "SpawnShipwrekcs", true).getBoolean(true);
    	FossilOptions.SpawnWeaponShop = config.get("general", "SpawnWeaponShop", true).getBoolean(true);
    	FossilOptions.SpawnAcademy = config.get("general", "SpawnAcademy", true).getBoolean(true);
    	FossilOptions.DinoGrows = config.get("general", "DinosaursGrow", true).getBoolean(true);
    	FossilOptions.DinoHunger = config.get("general", "DinosaursHunger", true).getBoolean(true);
    	FossilOptions.TRexBreakingBlocks = config.get("general", "TRexBreaksBlocks", true).getBoolean(true);
    	FossilOptions.BraBreakingBlocks = config.get("general", "BrachiosaurBreaksBlocks", true).getBoolean(true);
    	
    	config.save();
    }

    public static void ShowMessage(String s,EntityPlayer targetPlayer) {
    	if (targetPlayer==null) return;
    	targetPlayer.addChatMessage(s);
    }

    public static void DebugMessage(String s) {
        //logger.fine((new StringBuilder(s);
        if (DebugMode) {
            System.out.println(s);
        }
    }

    public static int EnumToInt(EnumOrderType input) {
        return input.ToInt();
    }
    private void registBlocks(){
    	 GameRegistry.registerBlock(blockFossil);
         GameRegistry.registerBlock(blockSkull);
         GameRegistry.registerBlock(SkullLantern);
         GameRegistry.registerBlock(blockanalyzerIdle);
         GameRegistry.registerBlock(blockanalyzerActive);
         GameRegistry.registerBlock(blockcultivateIdle);
         GameRegistry.registerBlock(blockcultivateActive);
         GameRegistry.registerBlock(blockworktableIdle);
         GameRegistry.registerBlock(blockworktableActive);
         GameRegistry.registerBlock(Ferns);
         GameRegistry.registerBlock(FernUpper);
         GameRegistry.registerBlock(Dump);
         GameRegistry.registerBlock(FeederIdle);
         GameRegistry.registerBlock(FeederActive);
         GameRegistry.registerBlock(blockPermafrost);
         GameRegistry.registerBlock(blockIcedStone);
    }
    private void initBlockAndItems() throws IDException{
    	blockFossil = new BlockFossil(BlockIDs[0]).setHardness(3F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("fossil").setCreativeTab(CreativeTabs.tabBlock).setTextureName("fossilsarch:Fossil");
        blockSkull = new BlockFossilSkull(BlockIDs[1], 0, false).setHardness(1.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("Skull").setCreativeTab(CreativeTabs.tabBlock);
        SkullLantern = new BlockFossilSkull(BlockIDs[2], 0, true).setHardness(1.0F).setLightValue(0.9375F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("SkullLantern").setCreativeTab(CreativeTabs.tabBlock);
        blockanalyzerIdle = new BlockAnalyzer(BlockIDs[3], false).setHardness(3F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("analyzerIdle").setCreativeTab(CreativeTabs.tabDecorations);
        blockanalyzerActive = new BlockAnalyzer(BlockIDs[4], true).setLightValue(0.9375F).setHardness(3F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("analyzerActive");
        blockcultivateIdle = new BlockCultivate(BlockIDs[5], false).setLightValue(0.9375F).setHardness(0.3F).setStepSound(Block.soundGlassFootstep).setUnlocalizedName("cultivateIdle").setCreativeTab(CreativeTabs.tabDecorations);
        blockcultivateActive = new BlockCultivate(BlockIDs[6], true).setLightValue(0.9375F).setHardness(0.3F).setStepSound(Block.soundGlassFootstep).setUnlocalizedName("cultivateActive");
        blockworktableIdle = new BlockWorktable(BlockIDs[7], false).setHardness(2.5F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("worktableIdle").setCreativeTab(CreativeTabs.tabDecorations);
        blockworktableActive = new BlockWorktable(BlockIDs[8], true).setHardness(2.5F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("worktableActive");
        Ferns = new BlockFern(BlockIDs[9], false).setHardness(0.0F).setStepSound(Block.soundGrassFootstep).setCreativeTab(null);
        FernUpper = new BlockFern(BlockIDs[10], true).setHardness(0.0F).setStepSound(Block.soundGrassFootstep).setCreativeTab(null);
        Dump = new BlockDrum(BlockIDs[11]).setHardness(0.8F).setUnlocalizedName("drum").setCreativeTab(CreativeTabs.tabDecorations);
        FeederIdle = new BlockFeeder(BlockIDs[12], false).setHardness(3.5F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("Feeder").setCreativeTab(CreativeTabs.tabDecorations);
        FeederActive = new BlockFeeder(BlockIDs[13], false).setHardness(3.5F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("Feeder");
        
        biofossil = new ItemBioFossil(ItemIDs[0]).setUnlocalizedName("biofossil").setCreativeTab(CreativeTabs.tabMisc).setTextureName("fossilsarch:BioFossil");
        relic = new ItemRelic(ItemIDs[1]).setUnlocalizedName("relic").setCreativeTab(CreativeTabs.tabMisc).setTextureName("fossilsarch:Relic");
        stoneboard = new ItemStoneBoard(ItemIDs[2]).setUnlocalizedName("stoneboard").setCreativeTab(CreativeTabs.tabDecorations).setTextureName("fossilsarch:StoneBoard");
        DNA = new ItemDNA(ItemIDs[3]).setUnlocalizedName("DNA").setCreativeTab(CreativeTabs.tabMaterials);
        Ancientegg = new ItemAncientEgg(ItemIDs[4]).setCreativeTab(CreativeTabs.tabMaterials);
        AncientSword = new ItemAncientsword(ItemIDs[5]).setUnlocalizedName("ancientsword").setCreativeTab(CreativeTabs.tabCombat).setTextureName("fossilsarch:AncientSword");
        BrokenSword = new ItemBrokenSword(ItemIDs[6]).setUnlocalizedName("Brokensword").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("fossilsarch:BrokenSword");
        FernSeed = new ItemFernSeed(ItemIDs[7], 0).setUnlocalizedName("FernSeed").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("fossilsarch:FernSeed");
        if(FMLCommonHandler.instance().getSide().isClient()){
        	Ancienthelmet = new ForgeItemArmor(ItemIDs[8], EnumArmorMaterial.IRON, ModLoader.addArmor("Ancient"), 0).setUnlocalizedName("ancientHelmet").setCreativeTab(CreativeTabs.tabCombat).setTextureName("fossilsarch:AncientHelmet");
        }else{
        	Ancienthelmet = new ForgeItemArmor(ItemIDs[8], EnumArmorMaterial.IRON, 0, 0).setUnlocalizedName("ancientHelmet").setCreativeTab(CreativeTabs.tabCombat);
        }
        Brokenhelmet = new ItemBrokenHelmet(ItemIDs[9]).setUnlocalizedName("BrokenHelmet").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("fossilsarch:BrokenHelmet");
        SkullStick = new ForgeItem(ItemIDs[10]).setUnlocalizedName("SkullStick").setCreativeTab(CreativeTabs.tabMisc).setTextureName("fossilsarch:SkullStick");
        Gen = new ItemGen(ItemIDs[11]).setUnlocalizedName("Gen").setCreativeTab(CreativeTabs.tabMisc).setTextureName("fossilsarch:Gen");
        GenAxe = new ForgeItemAxe(ItemIDs[12], EnumToolMaterial.EMERALD).setUnlocalizedName("GenAxe").setTextureName("fossilsarch:GenAxe");
        GenPickaxe = new ForgeItemPickaxe(ItemIDs[13], EnumToolMaterial.EMERALD).setUnlocalizedName("GenPickaxe").setTextureName("fossilsarch:GenPickaxe");
        GenSword = new ForgeItemSword(ItemIDs[14], EnumToolMaterial.EMERALD).setUnlocalizedName("GenSword").setTextureName("fossilsarch:GenSword");
        GenHoe = new ForgeItemHoe(ItemIDs[15], EnumToolMaterial.EMERALD).setUnlocalizedName("GenHoe").setTextureName("fossilsarch:GenHoe");
        GenShovel = new ForgeItemSpade(ItemIDs[16], EnumToolMaterial.EMERALD).setUnlocalizedName("GenShovel").setTextureName("fossilsarch:GenShovel");

        DinoPedia = new ForgeItem(ItemIDs[17]).setUnlocalizedName("dinopedia").setCreativeTab(CreativeTabs.tabTools).setTextureName("fossilsarch:DinoPedia");
        ChickenSoup = new ItemChickenSoup(ItemIDs[20]).setUnlocalizedName("ChickenSoup").setMaxStackSize(1).setContainerItem(Item.bucketEmpty).setCreativeTab(CreativeTabs.tabFood);
        
        ChickenEss = new ForgeItemFood(ItemIDs[21], 10, 0, false).setUnlocalizedName("ChickenEss").setContainerItem(Item.glassBottle).setCreativeTab(CreativeTabs.tabFood).setTextureName("fossilsarch:ChickenEssence");
        EmptyShell = new ForgeItem(ItemIDs[22]).setUnlocalizedName("EmptyShell").setCreativeTab(CreativeTabs.tabMisc).setTextureName("fossilsarch:EmptyShell");
        SJL = new ForgeItemFood(ItemIDs[23], 8, 2.0F, false).setUnlocalizedName("SioChiuLe").setCreativeTab(CreativeTabs.tabFood).setTextureName("fossilsarch:SioChiuLe");
        MagicConch = new ItemMagicConch(ItemIDs[24]).setUnlocalizedName("MagicConch").setCreativeTab(CreativeTabs.tabTools).setTextureName("fossilsarch:MagicConch");
        RawDinoMeat = new ItemDinoMeat(ItemIDs[25], 3, 0.3F, true).setUnlocalizedName("DinoMeat").setCreativeTab(CreativeTabs.tabFood).setTextureName("fossilsarch:DinoMeat");
        CookedDinoMeat = new ForgeItemFood(ItemIDs[26], 8, 0.8F, true).setUnlocalizedName("CookedDinoMeat").setCreativeTab(CreativeTabs.tabFood).setTextureName("fossilsarch:CookedDinoMeat");
        EmbyoSyringe = new ItemEmbryoSyringe(ItemIDs[27]).setUnlocalizedName("EmbryoSyringe").setCreativeTab(CreativeTabs.tabMisc);
        AnimalDNA = new ItemNonDinoDNA(ItemIDs[28]).setUnlocalizedName("AnimalDNA").setCreativeTab(CreativeTabs.tabMisc);
        IcedMeat=new ItemIcedMeat(ItemIDs[29], EnumToolMaterial.EMERALD).setUnlocalizedName("IcedMeat").setCreativeTab(CreativeTabs.tabCombat).setTextureName("fossilsarch:IcedMeat");
        Woodjavelin=new ItemJavelin(ItemIDs[30],EnumToolMaterial.WOOD).setUnlocalizedName("WoodJavelin").setCreativeTab(CreativeTabs.tabCombat).setTextureName("fossilsarch:WoodenJavelin");
        Stonejavelin=new ItemJavelin(ItemIDs[31],EnumToolMaterial.STONE).setUnlocalizedName("StoneJavelin").setCreativeTab(CreativeTabs.tabCombat).setTextureName("fossilsarch:StoneJavelin");
        Ironjavelin=new ItemJavelin(ItemIDs[32],EnumToolMaterial.IRON).setUnlocalizedName("IronJavelin").setCreativeTab(CreativeTabs.tabCombat).setTextureName("fossilsarch:IronJavelin");
        Goldjavelin=new ItemJavelin(ItemIDs[33],EnumToolMaterial.GOLD).setUnlocalizedName("GoldJavelin").setCreativeTab(CreativeTabs.tabCombat).setTextureName("fossilsarch:GoldJavelin");
        Diamondjavelin=new ItemJavelin(ItemIDs[34],EnumToolMaterial.EMERALD).setUnlocalizedName("DiamondJavelin").setCreativeTab(CreativeTabs.tabCombat).setTextureName("fossilsarch:DiamondJavelin");
        AncientJavelin= ((ItemJavelin) new ItemJavelin(ItemIDs[35],EnumToolMaterial.IRON).setUnlocalizedName("AncientJavelin")).setAncient(true).setCreativeTab(CreativeTabs.tabCombat).setTextureName("fossilsarch:AncientJavelin");
        Whip=new ItemWhip(ItemIDs[36]).setUnlocalizedName("FossilWhip").setCreativeTab(CreativeTabs.tabTools).setTextureName("fossilsarch:Whip");
        blockPermafrost = new BlockPermafrost(BlockIDs[14], "fossilsarch:Permafrost").setHardness(0.5F).setLightOpacity(3).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("Permafrost").setCreativeTab(CreativeTabs.tabBlock).setTextureName("fossilsarch:Permafrost");
        blockIcedStone = new BlockIcedStone(BlockIDs[15]).setHardness(1.5F).setResistance(10F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("IcedStone").setCreativeTab(CreativeTabs.tabBlock).setTickRandomly(true);
    }
    @SideOnly(Side.CLIENT)
    private void forgeTextureSetUp(){
    	if(FMLCommonHandler.instance().getSide().isServer()) return;
    }
    private void forgeHarvestLevelSetUp(){
        MinecraftForge.setBlockHarvestLevel(blockFossil, 0, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(blockPermafrost, 0, "shovel", 2);
        MinecraftForge.setBlockHarvestLevel(blockIcedStone, 0, "pickaxe", 1);
        MinecraftForge.setBlockHarvestLevel(blockIcedStone, 1, "pickaxe", 1);
    }
    private void registTileEntitys(){
        GameRegistry.registerTileEntity(TileEntityCultivate.class, "Cultivate");
        GameRegistry.registerTileEntity(TileEntityAnalyzer.class, "Analyzer");
        GameRegistry.registerTileEntity(TileEntityWorktable.class, "Worktable");
        GameRegistry.registerTileEntity(TileEntityDrum.class, "Dump");
        GameRegistry.registerTileEntity(TileEntityFeeder.class, "Feeder");
    }
    private void registEntitys(){
    	int modEntityID=1;
    	EntityRegistry.registerModEntity(EntityStoneboard.class, "StoneBoard", modEntityID++,this,250,Integer.MAX_VALUE,false);
    	EntityRegistry.registerModEntity(EntityJavelin.class, "Javelin", modEntityID++,this,250,5,true);
        EntityRegistry.registerModEntity(EntityAncientJavelin.class, "AncientJavelin", modEntityID++,this,250,5,true);
        EntityRegistry.registerModEntity(EntityMLighting.class, "FriendlyLighting", modEntityID++,this,250,5,true);
        EntityRegistry.registerModEntity(EntityFailuresaurus.class, "Failuresaurus", modEntityID++,this,250,5,true);
        EntityRegistry.registerModEntity(EntityBones.class, "Bones", modEntityID++,this,250,5,true);
        EntityRegistry.registerModEntity(EntityNautilus.class, "Nautilus", modEntityID++,this,250,5,true);
        EntityRegistry.registerModEntity(EntityDinoEgg.class, "DinoEgg", modEntityID++,this,250,5,true);
        EntityRegistry.registerModEntity(EntityRaptor.class, "Raptor", modEntityID++,this,250,5,true);
        EntityRegistry.registerModEntity(EntityTriceratops.class, "Triceratops", modEntityID++,this,250,5,true);
        EntityRegistry.registerModEntity(EntityTRex.class, "Tyrannosaurus", modEntityID++,this,250,5,true);
        EntityRegistry.registerModEntity(EntityFriendlyPigZombie.class, "FriendlyPigZombie", modEntityID++,this,250,5,true);
        EntityRegistry.registerModEntity(EntityPigBoss.class, "PigBoss", modEntityID++,this,250,5,true);
        EntityRegistry.registerModEntity(EntityPterosaur.class, "Pterosaur", modEntityID++,this,250,5,true);
        EntityRegistry.registerModEntity(EntityPlesiosaur.class, "Plesiosaur", modEntityID++,this,250,5,true);
        EntityRegistry.registerModEntity(EntityMosasaurus.class, "Mosasaurus", modEntityID++,this,250,5,true);
        EntityRegistry.registerModEntity(EntityStegosaurus.class, "Stegosaurus", modEntityID++,this,250,5,true);

        EntityRegistry.registerModEntity(Entitydil.class, "Utahraptor", modEntityID++,this,250,5,true);
        EntityRegistry.registerModEntity(EntityPregnantSheep.class, "PregnantSheep", modEntityID++,this,250,5,true);
        EntityRegistry.registerModEntity(EntityPregnantCow.class, "PregnantCow", modEntityID++,this,250,5,true);
        EntityRegistry.registerModEntity(EntityPregnantPig.class, "PregnantPig", modEntityID++,this,250,5,true);
        EntityRegistry.registerModEntity(EntitySaberCat.class, "SaberCat", modEntityID++,this,250,5,true);

        EntityRegistry.registerModEntity(EntityBrachiosaurus.class, "Brachiosaurus", modEntityID++,this,250,5,true);
        EntityRegistry.registerModEntity(EntityMammoth.class, "Mammoth", modEntityID++,this,250,5,true);
    }

    private void testingReciptSetup(){
        GameRegistry.addRecipe(new ItemStack(this.AncientSword, 1), new Object[]{
        "X", Character.valueOf('X'), Block.dirt });
       
        GameRegistry.addRecipe(new ItemStack(this.Ancienthelmet,1), new
        Object[] { "X",Character.valueOf('X'), Block.sand });
       
       
        GameRegistry.addRecipe(new ItemStack(FeederIdle, 64), new Object[] {
        "XY",Character.valueOf('X'), Block.sand,Character.valueOf('Y'),
        Block.dirt
		});
    }
    private void reciptsSetup(){
    	GameRegistry.addRecipe(new ItemStack(SkullLantern, 1), new Object[]{
            "X", "Y", Character.valueOf('X'), blockSkull, Character.valueOf('Y'), Block.torchWood
        });
GameRegistry.addRecipe(new ItemStack(Item.dyePowder, 5, 15), new Object[]{
            "X", Character.valueOf('X'), blockSkull
        });
GameRegistry.addRecipe(new ItemStack(Item.dyePowder, 5, 15), new Object[]{
            "X", Character.valueOf('X'), SkullLantern
        });
//3.0 Contects
GameRegistry.addRecipe(new ItemStack(blockcultivateIdle, 1), new Object[]{
            "XYX", "XWX", "ZZZ", Character.valueOf('X'), Block.glass, Character.valueOf('Y'), new ItemStack(Item.dyePowder, 1, 2), Character.valueOf('W'), Item.bucketWater, Character.valueOf('Z'), Item.ingotIron
        });
GameRegistry.addRecipe(new ItemStack(blockanalyzerIdle, 1), new Object[]{
            "XYX", "XWX", Character.valueOf('X'), Item.ingotIron, Character.valueOf('Y'), relic, Character.valueOf('W'), biofossil
        });
GameRegistry.addRecipe(new ItemStack(blockworktableIdle, 1), new Object[]{
            "X", "Y", Character.valueOf('X'), Item.paper, Character.valueOf('Y'), Block.workbench
        });
GameRegistry.addRecipe(new ItemStack(Item.cake, 1), new Object[]{
            "AAA", "BEB", "CCC", Character.valueOf('A'), Item.bucketMilk, Character.valueOf('B'), Item.sugar, Character.valueOf('C'), Item.wheat, Character.valueOf('E'), new ItemStack(Ancientegg, 1, 0)
        });
GameRegistry.addRecipe(new ItemStack(Item.cake, 1), new Object[]{
            "AAA", "BEB", "CCC", Character.valueOf('A'), Item.bucketMilk, Character.valueOf('B'), Item.sugar, Character.valueOf('C'), Item.wheat, Character.valueOf('E'), new ItemStack(Ancientegg, 1, 1)
        });
GameRegistry.addRecipe(new ItemStack(Item.cake, 1), new Object[]{
            "AAA", "BEB", "CCC", Character.valueOf('A'), Item.bucketMilk, Character.valueOf('B'), Item.sugar, Character.valueOf('C'), Item.wheat, Character.valueOf('E'), new ItemStack(Ancientegg, 1, 2)
        });
GameRegistry.addRecipe(new ItemStack(Item.cake, 1), new Object[]{
            "AAA", "BEB", "CCC", Character.valueOf('A'), Item.bucketMilk, Character.valueOf('B'), Item.sugar, Character.valueOf('C'), Item.wheat, Character.valueOf('E'), new ItemStack(Ancientegg, 1, 3)
        });
GameRegistry.addRecipe(new ItemStack(Item.cake, 1), new Object[]{
            "AAA", "BEB", "CCC", Character.valueOf('A'), Item.bucketMilk, Character.valueOf('B'), Item.sugar, Character.valueOf('C'), Item.wheat, Character.valueOf('E'), new ItemStack(Ancientegg, 1, 5)
        });
GameRegistry.addRecipe(new ItemStack(Item.cake, 1), new Object[]{
            "AAA", "BEB", "CCC", Character.valueOf('A'), Item.bucketMilk, Character.valueOf('B'), Item.sugar, Character.valueOf('C'), Item.wheat, Character.valueOf('E'), new ItemStack(Ancientegg, 1, 6)
        });
GameRegistry.addRecipe(new ItemStack(Item.cake, 1), new Object[]{
            "AAA", "BEB", "CCC", Character.valueOf('A'), Item.bucketMilk, Character.valueOf('B'), Item.sugar, Character.valueOf('C'), Item.wheat, Character.valueOf('E'), new ItemStack(Ancientegg, 1, 7)
        });
GameRegistry.addRecipe(new ItemStack(Item.cake, 1), new Object[]{
            "AAA", "BEB", "CCC", Character.valueOf('A'), Item.bucketMilk, Character.valueOf('B'), Item.sugar, Character.valueOf('C'), Item.wheat, Character.valueOf('E'), new ItemStack(Ancientegg, 1, 8)
        });
//4.0 contents
GameRegistry.addRecipe(new ItemStack(SkullStick, 1), new Object[]{
            "X", "Y", Character.valueOf('X'), blockSkull, Character.valueOf('Y'), Item.stick
        });
GameRegistry.addRecipe(new ItemStack(Dump, 1), new Object[]{
            "ZZZ", "XYX", "XXX", Character.valueOf('X'), Block.planks, Character.valueOf('Y'), Item.redstone, Character.valueOf('Z'), Item.leather
        });
GameRegistry.addRecipe(new ItemStack(FeederIdle, 1), new Object[]{
            "XYX", "ZAB", "BBB", Character.valueOf('X'), Item.ingotIron, Character.valueOf('Y'), Block.glass, Character.valueOf('Z'), Block.stoneButton, Character.valueOf('A'), Item.bucketEmpty, Character.valueOf('B'), Block.stone
        });
ModLoader.addShapelessRecipe(new ItemStack(GenAxe), new Object[]{
            Item.axeWood, Gen
        });
ModLoader.addShapelessRecipe(new ItemStack(GenAxe), new Object[]{
            Item.axeStone, Gen
        });
ModLoader.addShapelessRecipe(new ItemStack(GenAxe), new Object[]{
            Item.axeIron, Gen
        });
ModLoader.addShapelessRecipe(new ItemStack(GenAxe), new Object[]{
            Item.axeGold, Gen
        });
ModLoader.addShapelessRecipe(new ItemStack(GenAxe), new Object[]{
            Item.axeDiamond, Gen
        });


ModLoader.addShapelessRecipe(new ItemStack(GenPickaxe), new Object[]{
            Item.pickaxeWood, Gen
        });
ModLoader.addShapelessRecipe(new ItemStack(GenPickaxe), new Object[]{
            Item.pickaxeStone, Gen
        });
ModLoader.addShapelessRecipe(new ItemStack(GenPickaxe), new Object[]{
            Item.pickaxeIron, Gen
        });
ModLoader.addShapelessRecipe(new ItemStack(GenPickaxe), new Object[]{
            Item.pickaxeGold, Gen
        });
ModLoader.addShapelessRecipe(new ItemStack(GenPickaxe), new Object[]{
            Item.pickaxeDiamond, Gen
        });


ModLoader.addShapelessRecipe(new ItemStack(GenHoe), new Object[]{
            Item.hoeWood, Gen
        });
ModLoader.addShapelessRecipe(new ItemStack(GenHoe), new Object[]{
            Item.hoeStone, Gen
        });
ModLoader.addShapelessRecipe(new ItemStack(GenHoe), new Object[]{
            Item.hoeIron, Gen
        });
ModLoader.addShapelessRecipe(new ItemStack(GenHoe), new Object[]{
            Item.hoeGold, Gen
        });
ModLoader.addShapelessRecipe(new ItemStack(GenHoe), new Object[]{
            Item.hoeDiamond, Gen
        });


ModLoader.addShapelessRecipe(new ItemStack(GenSword), new Object[]{
            Item.swordWood, Gen
        });
ModLoader.addShapelessRecipe(new ItemStack(GenSword), new Object[]{
            Item.swordStone, Gen
        });
ModLoader.addShapelessRecipe(new ItemStack(GenSword), new Object[]{
            Item.swordIron, Gen
        });
ModLoader.addShapelessRecipe(new ItemStack(GenSword), new Object[]{
            Item.swordGold, Gen
        });
ModLoader.addShapelessRecipe(new ItemStack(GenSword), new Object[]{
            Item.swordDiamond, Gen
        });

ModLoader.addShapelessRecipe(new ItemStack(GenShovel), new Object[]{
            Item.shovelWood, Gen
        });
ModLoader.addShapelessRecipe(new ItemStack(GenShovel), new Object[]{
            Item.shovelStone, Gen
        });
ModLoader.addShapelessRecipe(new ItemStack(GenShovel), new Object[]{
            Item.shovelIron, Gen
        });
ModLoader.addShapelessRecipe(new ItemStack(GenShovel), new Object[]{
            Item.shovelGold, Gen
        });
ModLoader.addShapelessRecipe(new ItemStack(GenShovel), new Object[]{
            Item.shovelDiamond, Gen
        });

ModLoader.addShapelessRecipe(new ItemStack(DinoPedia), new Object[]{
            Item.book, new ItemStack(DNA, 1, 0)
        });
ModLoader.addShapelessRecipe(new ItemStack(DinoPedia), new Object[]{
            Item.book, new ItemStack(DNA, 1, 1)
        });
ModLoader.addShapelessRecipe(new ItemStack(DinoPedia), new Object[]{
            Item.book, new ItemStack(DNA, 1, 2)
        });
ModLoader.addShapelessRecipe(new ItemStack(DinoPedia), new Object[]{
            Item.book, new ItemStack(DNA, 1, 3)
        });
ModLoader.addShapelessRecipe(new ItemStack(DinoPedia), new Object[]{
            Item.book, new ItemStack(DNA, 1, 4)
        });
ModLoader.addShapelessRecipe(new ItemStack(DinoPedia), new Object[]{
            Item.book, new ItemStack(DNA, 1, 5)
        });
ModLoader.addShapelessRecipe(new ItemStack(DinoPedia), new Object[]{
            Item.book, new ItemStack(DNA, 1, 6)
        });
ModLoader.addShapelessRecipe(new ItemStack(DinoPedia), new Object[]{
            Item.book, new ItemStack(DNA, 1, 7)
        });
ModLoader.addShapelessRecipe(new ItemStack(DinoPedia), new Object[]{
            Item.book, new ItemStack(DNA, 1, 8)
        });
ModLoader.addShapelessRecipe(new ItemStack(ChickenSoup,1,0), new Object[]{
            Item.bucketEmpty, Item.chickenRaw
        });
ModLoader.addShapelessRecipe(new ItemStack(MagicConch, 1, 1), new Object[]{
            new ItemStack(MagicConch, 1, 0)
        });
ModLoader.addShapelessRecipe(new ItemStack(MagicConch, 1, 2), new Object[]{
            new ItemStack(MagicConch, 1, 1)
        });
ModLoader.addShapelessRecipe(new ItemStack(MagicConch, 1, 0), new Object[]{
            new ItemStack(MagicConch, 1, 2)
        });
GameRegistry.addRecipe(new ItemStack(ChickenEss, 8), new Object[]{
    "XXX", "XYX", "XXX", Character.valueOf('X'), Item.glassBottle, Character.valueOf('Y'), new ItemStack(ChickenSoup,1,1)
});
    }
    private void smeltingSetup(){
    	final float xp=3.0f;
        GameRegistry.addSmelting(ChickenSoup.itemID, new ItemStack(ChickenSoup,1,1),xp);
        
        GameRegistry.addSmelting(RawDinoMeat.itemID, new ItemStack(CookedDinoMeat),xp);
        GameRegistry.addSmelting(IcedMeat.itemID, new ItemStack(Item.beefCooked),xp);
        try{
             FurnaceRecipes.smelting().addSmelting(Ancientegg.itemID,EnumDinoType.Nautilus.ordinal(), new ItemStack(SJL), 0);
        }catch(Throwable e){}

    }
    private void spawningSetup(){
    	final BiomeGenBase[] avaliableBiomes=new BiomeGenBase[22];
    	for (int i=0;i<avaliableBiomes.length;i++){
    		avaliableBiomes[i]=BiomeGenBase.biomeList[i];
    	}
        //EntityRegistry.addSpawn(EntityPigBoss.class, 1, 1, 1, EnumCreatureType.creature, new BiomeGenBase[]{BiomeGenBase.hell});
        EntityRegistry.addSpawn(EntityNautilus.class, 5, 4, 14, EnumCreatureType.waterCreature,avaliableBiomes);
    	//EntityRegistry.addSpawn(EntityFailuresaurus.class, 10, 4, 4, EnumCreatureType.monster,avaliableBiomes);
    }

    @Mod.EventHandler
    public void PreLoad(FMLPreInitializationEvent event){
    	if(FMLCommonHandler.instance().getSide().isClient()){
    	MinecraftForge.EVENT_BUS.register(new DinoSoundHandler());
    	}
    	
        ReadOptions(event.getSuggestedConfigurationFile());
    }
    
    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
    	if(FMLCommonHandler.instance().getSide().isClient()){
    		    		  LastLangSetting = ModLoader.getMinecraftInstance().gameSettings.language;
    		              registingRenderer();
    		              forgeTextureSetUp();
    		        	//KeyBindingRegistry.registerKeyBinding(keyBindingService);
    	}
    	NetworkRegistry.instance().registerChatListener(messagerHandler);
    	GameRegistry.registerWorldGenerator(FossilGenerator);
    	GameRegistry.registerWorldGenerator(AcademyA);
    	GameRegistry.registerWorldGenerator(ShipA);
    	GameRegistry.registerWorldGenerator(WeaponShopA);

          initBlockAndItems();
          registBlocks();       
          /*
           * creative function
           */
          fillCreativeList();

          /*
           * Forge Function
           */
          //MinecraftForgeClient.registerSoundHandler(new DinoSoundHandler());

          forgeHarvestLevelSetUp();
          registTileEntitys();
          registEntitys();
          //Quick recipe for testing
          //testingReciptSetup();
          //Actual using recipes
          reciptsSetup();
          smeltingSetup();
          spawningSetup();
          GUIHandlerSetup();

          INSTANCE=this;
    }

    private void GUIHandlerSetup() {
    	NetworkRegistry.instance().registerGuiHandler(this,GH);
	}
    
    public static void UpdateLangProp()
    	throws IOException{
    	String loadLang=LastLangSetting;
    	try{
	    	Langfile=new File((mod_Fossil.class).getResource("/Fossillang/"+LastLangSetting+".lang").getFile());
    	}
        catch(Throwable e){
        	loadLang=DEFAULT_LANG;
        }
    	finally{

        	UTF8Reader(LangProps,loadLang); 
        	//ChkRevLang(loadLang);
        }
    }
    private static void UTF8Reader(Properties properties,String LangName)
    	    throws IOException
    	    {
    	        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader((mod_Fossil.class).getResourceAsStream((new StringBuilder()).append("/Fossillang/").append(LangName).append(".lang").toString()), "UTF-8"));
    	        for (String s1 = bufferedreader.readLine(); s1 != null; s1 = bufferedreader.readLine())
    	        {
    	            s1 = s1.trim();
    	            if (s1.startsWith("#"))
    	            {
    	                continue;
    	            }
    	            String as[] = s1.split("=");
    	            if (as != null && as.length == 2)
    	            {
    	                properties.setProperty(as[0], as[1]);
    	            }
    	        }
    	    }

	public static void callGUI(EntityPlayer par5EntityPlayer,
			int GuiId, World par1World, int par2, int par3, int par4) {
		//FMLCommonHandler.instance().findContainerFor(mod)
    	//if(FMLCommonHandler.instance().getSide().isClient()){
		FMLNetworkHandler.openGui(par5EntityPlayer,INSTANCE , GuiId, par1World, par2,par3,par4);
    	//}
		
	}

	
}