package ic2.advancedmachines.common;

import ic2.api.item.Items;
import ic2.api.recipe.Recipes;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "AdvancedMachines", name = "IC2 Advanced Machines Addon", version = "1.5.2-5.2b", dependencies = "required-after:IC2")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class AdvancedMachines implements IGuiHandler, IProxy {
    public static AdvancedMachines instance;

    public static Configuration config;

    public static Block blockAdvancedMachine;
    public static ItemStack stackRotaryMacerator;
    public static ItemStack stackSingularityCompressor;
    public static ItemStack stackCentrifugeExtractor;
    public static ItemStack stackAdvancedInduction;


    private int refIronID;
    private int redstoneUpgradeID;
    public static Item refinedIronDust;
    public static Item redstoneUpgrade;

    public static int guiIdRotary;
    public static int guiIdSingularity;
    public static int guiIdCentrifuge;
    public static int guiIdInduction;

    public static String advMaceName = "Rotary Macerator";
    public static String advCompName = "Singularity Compressor";
    public static String advExtcName = "Centrifuge Extractor";
    public static String advIndName = "Induction Furnace MK2";
    public static String refIronDustName = "Refined Iron Dust";
    public static String redstoneUpgradeName = "Redstone Upgrade";

    public static ItemStack overClockerStack;
    public static ItemStack transformerStack;
    public static ItemStack energyStorageUpgradeStack;

    public static String advMaceSound = "Machines/MaceratorOp.ogg";
    public static String advCompSound = "Machines/CompressorOp.ogg";
    public static String advExtcSound = "Machines/ExtractorOp.ogg";
    public static String interruptSound = "Machines/InterruptOne.ogg";

    public static int defaultEnergyConsume = 3;
    public static int defaultAcceleration = 2;
    public static int overClockSpeedBonus = 500;
    public static double overClockEnergyRatio = 2.0D;
    public static double overClockAccelRatio = 1.6D;
    public static double overLoadInputRatio = 4.0D;

    @SidedProxy(clientSide = "ic2.advancedmachines.client.AdvancedMachinesClient", serverSide = "ic2.advancedmachines.common.AdvancedMachines")
    public static IProxy proxy;

    @PreInit
    public void preInit(FMLPreInitializationEvent evt) {
        instance = this;
        config = new Configuration(evt.getSuggestedConfigurationFile());
        config.load();
    }

    @Init
    public void load(FMLInitializationEvent evt) {
        blockAdvancedMachine = new BlockAdvancedMachines(config.getBlock("IDs", "AdvancedMachineBlock", 188).getInt()).setUnlocalizedName("blockAdvMachine");
        refIronID = config.getItem("IDs", "refIronID", 29775).getInt(29775);
        redstoneUpgradeID = config.getItem("IDs", "redstoneUpgradeID", 29776).getInt(29776);

        guiIdRotary = config.get("IDs", "guiIdRotary", 40).getInt();
        guiIdSingularity = config.get("IDs", "guiIdSingularity", 41).getInt();
        guiIdCentrifuge = config.get("IDs", "guiIdCentrifuge", 42).getInt();
        guiIdInduction = config.get("IDs", "guiIdInduction", 43).getInt();

        Property prop = config.get("Sounds", "advCompressorSound", advCompSound);
        prop.comment = "Sound files to use on operation. Remember to use '/' instead of backslashes and the Sound directory starts on ic2/sounds. Set empty to disable.";
        advCompSound = prop.getString();
        advExtcSound = config.get("Sounds", "advExtractorSound", advExtcSound).getString();
        advMaceSound = config.get("Sounds", "advMaceratorSound", advMaceSound).getString();

        prop = config.get("Sounds", "interuptSound", interruptSound);
        prop.comment = "Sound played when a machine process is interrupted";
        interruptSound = prop.getString();

        prop = config.get("translation", "nameAdvCompressor", advCompName);
        prop.comment = "Item names. This will also affect their GUI";
        advCompName = prop.getString();
        advExtcName = config.get("translation", "nameAdvExtractor", advExtcName).getString();
        advMaceName = config.get("translation", "nameAdvMacerator", advMaceName).getString();
        advIndName = config.get("translation", "nameAdvInduction", advIndName).getString();
        refIronDustName = config.get("translation", "nameAdvRefIronDust", refIronDustName).getString();

        prop = config.get("OPness control", "baseEnergyConsumption", "3");
        prop.comment = "Base power draw per work tick.";
        defaultEnergyConsume = prop.getInt(3);

        prop = config.get("OPness control", "baseMachineAcceleration", "2");
        prop.comment = "Base machine speedup each work tick.";
        defaultAcceleration = prop.getInt(2);

        prop = config.get("OPness control", "overClockSpeedBonus", "500");
        prop.comment = "How much additional max speed does each Overclocker grant.";
        overClockSpeedBonus = prop.getInt(500);

        prop = config.get("OPness control", "overClockEnergyRatio", "2.0");
        prop.comment = "Exponent of Overclocker energy consumption function. 2.0 equals a squared rise of power draw as you add Overclockers.";
        overClockEnergyRatio = Double.valueOf(prop.getString());

        prop = config.get("OPness control", "overClockAccelRatio", "1.6");
        prop.comment = "Exponent of Overclocker acceleration function. 1.6 equals a less-than-squared rise of acceleration speed as you add Overclockers.";
        overClockAccelRatio = Double.valueOf(prop.getString());

        prop = config.get("OPness control", "overLoadInputRatio", "4.0");
        prop.comment = "Exponent of Transformer input function. Determines rise of maximum power intake as you add Transformers.";
        overLoadInputRatio = Double.valueOf(prop.getString());

        if (config != null) {
            config.save();
        }

        proxy.load();

        GameRegistry.registerBlock(blockAdvancedMachine, ItemAdvancedMachine.class, "blockAdvMachine");

        GameRegistry.registerTileEntity(TileEntityRotaryMacerator.class, "Rotary Macerator");
        GameRegistry.registerTileEntity(TileEntitySingularityCompressor.class, "Singularity Compressor");
        GameRegistry.registerTileEntity(TileEntityCentrifugeExtractor.class, "Centrifuge Extractor");
        GameRegistry.registerTileEntity(TileAdvancedInduction.class, "Advanced Induction Furnace");

        NetworkRegistry.instance().registerGuiHandler(this, this);
    }

    @PostInit
    public void afterModsLoaded(FMLPostInitializationEvent evt) {
        stackRotaryMacerator = new ItemStack(blockAdvancedMachine, 1, 0);
        Recipes.advRecipes.addRecipe(stackRotaryMacerator,
                "RRR", "RMR", "RAR",
                'R', Items.getItem("refinedIronIngot"),
                'M', Items.getItem("macerator"),
                'A', Items.getItem("advancedMachine"));
        LanguageRegistry.addName(stackRotaryMacerator, advMaceName);

        stackSingularityCompressor = new ItemStack(blockAdvancedMachine, 1, 1);
        Recipes.advRecipes.addRecipe(stackSingularityCompressor,
                "RRR", "RMR", "RAR",
                'R', Block.obsidian,
                'M', Items.getItem("compressor"),
                'A', Items.getItem("advancedMachine"));
        LanguageRegistry.addName(stackSingularityCompressor, advCompName);

        stackCentrifugeExtractor = new ItemStack(blockAdvancedMachine, 1, 2);
        Recipes.advRecipes.addRecipe(stackCentrifugeExtractor,
                "RRR", "RMR", "RAR",
                'R', Items.getItem("electrolyzedWaterCell"),
                'M', Items.getItem("extractor"),
                'A', Items.getItem("advancedMachine"));
        LanguageRegistry.addName(stackCentrifugeExtractor, advExtcName);

        stackAdvancedInduction = new ItemStack(blockAdvancedMachine, 1, 3);
        Recipes.advRecipes.addRecipe(stackAdvancedInduction,
                " C ", "RMR",
                'R', Item.redstone,
                'M', Items.getItem("inductionFurnace"),
                'C', Items.getItem("electronicCircuit"));
        LanguageRegistry.addName(stackAdvancedInduction, advIndName);

        overClockerStack = Items.getItem("overclockerUpgrade");
        transformerStack = Items.getItem("transformerUpgrade");
        energyStorageUpgradeStack = Items.getItem("energyStorageUpgrade");

        refinedIronDust = new ItemBase(refIronID, "refinedIronDust");
        LanguageRegistry.addName(refinedIronDust, refIronDustName);
        GameRegistry.addSmelting(refinedIronDust.itemID, Items.getItem("refinedIronIngot"), 1.0f);

        redstoneUpgrade = new ItemBase(redstoneUpgradeID, "redstoneUpgrade");
        LanguageRegistry.addName(redstoneUpgrade, redstoneUpgradeName);

        Recipes.advRecipes.addRecipe(new ItemStack(redstoneUpgrade),
                "T T", " L ", "T T",
                'T', "ingotTin",
                'L', Block.lever);
    }

    public static boolean explodeMachineAt(World world, int x, int y, int z) {
        try {
            Class<?> mainIC2Class = Class.forName("IC2");
            mainIC2Class.getMethod("explodeMachineAt", World.class, Integer.TYPE, Integer.TYPE, Integer.TYPE).invoke(null, world, x, y, z);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getBlockTileEntity(x, y, z);

        if (te != null) {
            if (te instanceof TileEntityRotaryMacerator) {
                return ((TileEntityRotaryMacerator) te).getGuiContainer(player.inventory);
            } else if (te instanceof TileEntityCentrifugeExtractor) {
                return ((TileEntityCentrifugeExtractor) te).getGuiContainer(player.inventory);
            } else if (te instanceof TileEntitySingularityCompressor) {
                return ((TileEntitySingularityCompressor) te).getGuiContainer(player.inventory);
            } else if (te instanceof TileAdvancedInduction) {
                return ((TileAdvancedInduction) te).getGuiContainer(player.inventory);
            }
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return proxy.getGuiElementForClient(ID, player, world, x, y, z);
    }

    // PROXY SERVERSIDE NOOP METHODS
    @Override
    public void load() {
        // NOOP
    }

    @Override
    public Object getGuiElementForClient(int ID, EntityPlayer player, World world, int x, int y, int z) {
        // NOOP
        return null;
    }
}
