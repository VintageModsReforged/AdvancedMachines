package ic2.advancedmachines.common;

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
import ic2.api.Ic2Recipes;
import ic2.api.Items;
import ic2.core.IC2;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

@Mod(modid = "AdvancedMachines", name = "IC2 Advanced Machines Addon", version = "4.7d", dependencies = "required-after:IC2")
@NetworkMod(clientSideRequired = true)
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
    public static int guiIdAdvInduction;

    public static String advMaceName = "Rotary Macerator";
    public static String advCompName = "Singularity Compressor";
    public static String advExtcName = "Centrifuge Extractor";
    public static String advIndName = "Induction Furnace MKII";
    public static String refIronDustName = "Refined Iron dust";
    public static String redstoneUpgradeName = "Redstone Inverter Upgrade";

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
        blockAdvancedMachine = new BlockAdvancedMachines(config.getBlock("IDs", "AdvancedMachineBlock", 188).getInt()).setBlockName("blockAdvMachine");
        refIronID = config.getItem("IDs", "refIronID", 29775).getInt(29775);
        redstoneUpgradeID = config.getItem("IDs", "redstoneUpgradeID", 29776).getInt(29776);

        guiIdRotary = config.get("IDs", "guiIdRotary", 40).getInt();
        guiIdSingularity = config.get("IDs", "guiIdSingularity", 41).getInt();
        guiIdCentrifuge = config.get("IDs", "guiIdCentrifuge", 42).getInt();
        guiIdAdvInduction = config.get("IDs", "guiIdAdvInduction", 43).getInt();

        Property prop = config.get("Sounds", "advCompressorSound", advCompSound);
        prop.comment = "Sound files to use on operation. Remember to use '/' instead of backslashes and the Sound directory starts on ic2/sounds. Set empty to disable.";
        advCompSound = prop.value;
        advExtcSound = config.get("Sounds", "advExtractorSound", advExtcSound).value;
        advMaceSound = config.get("Sounds", "advMaceratorSound", advMaceSound).value;

        prop = config.get("Sounds", "interuptSound", interruptSound);
        prop.comment = "Sound played when a machine process is interrupted";
        interruptSound = prop.value;

        prop = config.get("translation", "nameAdvCompressor", advCompName);
        prop.comment = "Item names. This will also affect their GUI";
        advCompName = prop.value;
        advExtcName = config.get("translation", "nameAdvExtractor", advExtcName).value;
        advMaceName = config.get("translation", "nameAdvMacerator", advMaceName).value;
        advIndName = config.get("translation", "nameAdvInduction", advIndName).value;
        refIronDustName = config.get("translation", "nameAdvRefIronDust", refIronDustName).value;
        redstoneUpgradeName = config.get("translation", "nameRedstoneUpgrade", redstoneUpgradeName).value;

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
        overClockEnergyRatio = Double.parseDouble(prop.value);

        prop = config.get("OPness control", "overClockAccelRatio", "1.6");
        prop.comment = "Exponent of Overclocker acceleration function. 1.6 equals a less-than-squared rise of acceleration speed as you add Overclockers.";
        overClockAccelRatio = Double.parseDouble(prop.value);

        prop = config.get("OPness control", "overLoadInputRatio", "4.0");
        prop.comment = "Exponent of Transformer input function. Determines rise of maximum power intake as you add Transformers.";
        overLoadInputRatio = Double.parseDouble(prop.value);

        if (config != null) {
            config.save();
        }

        proxy.load();

        GameRegistry.registerBlock(blockAdvancedMachine, ItemAdvancedMachine.class, "blockAdvMachine");

        GameRegistry.registerTileEntity(TileEntityRotaryMacerator.class, "Rotary Macerator");
        GameRegistry.registerTileEntity(TileEntitySingularityCompressor.class, "Singularity Compressor");
        GameRegistry.registerTileEntity(TileEntityCentrifugeExtractor.class, "Centrifuge Extractor");
        GameRegistry.registerTileEntity(TileEntityAdvancedInduction.class, "Advanced Induction");

        NetworkRegistry.instance().registerGuiHandler(this, this);
    }

    @PostInit
    public void afterModsLoaded(FMLPostInitializationEvent evt) {
        stackRotaryMacerator = new ItemStack(blockAdvancedMachine, 1, 0);
        Ic2Recipes.addCraftingRecipe(stackRotaryMacerator,
                "RRR", "RMR", "RAR",
                'R', Items.getItem("refinedIronIngot"),
                'M', Items.getItem("macerator"),
                'A', Items.getItem("advancedMachine"));
        LanguageRegistry.addName(stackRotaryMacerator, advMaceName);

        stackSingularityCompressor = new ItemStack(blockAdvancedMachine, 1, 1);
        Ic2Recipes.addCraftingRecipe(stackSingularityCompressor,
                "RRR", "RMR", "RAR",
                'R', Block.obsidian,
                'M', Items.getItem("compressor"),
                'A', Items.getItem("advancedMachine"));
        LanguageRegistry.addName(stackSingularityCompressor, advCompName);

        stackCentrifugeExtractor = new ItemStack(blockAdvancedMachine, 1, 2);
        Ic2Recipes.addCraftingRecipe(stackCentrifugeExtractor,
                "RRR", "RMR", "RAR",
                'R', Items.getItem("electrolyzedWaterCell"),
                'M', Items.getItem("extractor"),
                'A', Items.getItem("advancedMachine"));
        LanguageRegistry.addName(stackCentrifugeExtractor, advExtcName);

        stackAdvancedInduction = new ItemStack(blockAdvancedMachine, 1, 3);
        Ic2Recipes.addCraftingRecipe(stackAdvancedInduction,
                " C ", "RMR",
                'R', Item.redstone,
                'M', Items.getItem("inductionFurnace"),
                'C', Items.getItem("advancedCircuit"));
        LanguageRegistry.addName(stackAdvancedInduction, advIndName);

        overClockerStack = Items.getItem("overclockerUpgrade");
        transformerStack = Items.getItem("transformerUpgrade");
        energyStorageUpgradeStack = Items.getItem("energyStorageUpgrade");

        refinedIronDust = new ItemDust(refIronID).setItemName("refinedIronDust");
        LanguageRegistry.addName(refinedIronDust, refIronDustName);
        GameRegistry.addSmelting(refinedIronDust.itemID, Items.getItem("refinedIronIngot"), 1.0f);

        redstoneUpgrade = new Item(redstoneUpgradeID).setItemName("redstoneUpgrade").setTextureFile("ic2/advancedmachines/client/sprites/block_advmachine.png").setIconIndex(5).setCreativeTab(IC2.tabIC2);
        LanguageRegistry.addName(redstoneUpgrade, redstoneUpgradeName);
        Ic2Recipes.addCraftingRecipe(new ItemStack(redstoneUpgrade),
                "T T", " R ", "T T",
                'T', "ingotTin",
                'R', Block.torchRedstoneActive);
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
            } else if (te instanceof TileEntityAdvancedInduction) {
                return ((TileEntityAdvancedInduction) te).getGuiContainer(player.inventory);
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
