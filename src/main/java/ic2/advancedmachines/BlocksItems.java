package ic2.advancedmachines;

import cpw.mods.fml.common.registry.GameRegistry;
import ic2.advancedmachines.blocks.BlockAdvEnergyBlock;
import ic2.advancedmachines.blocks.BlockAdvancedMachine;
import ic2.advancedmachines.blocks.tiles.energy.*;
import ic2.advancedmachines.blocks.tiles.machines.*;
import ic2.advancedmachines.items.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlocksItems {

    public static Block ADVANCED_MACHINE_BLOCK;
    public static Block ADVANCED_ENERGY_BLOCK;

    public static Item ADVANCED_UPGRADE;
    public static ItemStack REDSTONE_INVERTER;
    public static ItemStack COBBLESTONE_GENERATOR;

    public static Item GLOWTRONIC_CRYSTAL;
    public static Item UNIVERSAL_CRYSTAL;
    public static Item PLASMA_CRYSTAL;

    public static Item COMPONENT;
    public static ItemStack MAGNET_CHUNK;
    public static ItemStack MAGNET_DEAD;
    public static ItemStack MAGNET_COMPONENT;
    public static ItemStack CIRCUIT_COMPLEX;
    public static ItemStack IRIDIUM_CORE;

    public static void init() {
        ADVANCED_UPGRADE = registerItem(new ItemAdvUpgrade(), "advanced.upgrade");
        REDSTONE_INVERTER = new ItemStack(ADVANCED_UPGRADE, 1, 0);
        COBBLESTONE_GENERATOR = new ItemStack(ADVANCED_UPGRADE, 1, 1);

        COMPONENT = registerItem(new ItemMisc(), "component");
        MAGNET_CHUNK = new ItemStack(COMPONENT, 1, 0);
        MAGNET_DEAD = new ItemStack(COMPONENT, 1, 1);
        MAGNET_COMPONENT = new ItemStack(COMPONENT, 1, 2);
        CIRCUIT_COMPLEX = new ItemStack(COMPONENT, 1, 3);
        IRIDIUM_CORE = new ItemStack(COMPONENT, 1, 4);

        GLOWTRONIC_CRYSTAL = new ItemAdvBattery(AdvancedMachinesConfig.GLOWTRONIC_CRYSTAL_ID, "glowtronic_crystal", 3, 5000, 7500000);
        UNIVERSAL_CRYSTAL = new ItemAdvBattery(AdvancedMachinesConfig.UNIVERSAL_CRYSTAL_ID, "universal_crystal", 4, 10000, 17500000);
        PLASMA_CRYSTAL = new ItemAdvBattery(AdvancedMachinesConfig.PLASMA_CRYSTAL_ID, "plasma_crystal", 4, 25000, 50000000);

        ADVANCED_MACHINE_BLOCK = new BlockAdvancedMachine();
        ADVANCED_ENERGY_BLOCK = new BlockAdvEnergyBlock();

        GameRegistry.registerBlock(ADVANCED_MACHINE_BLOCK, ItemAdvancedMachine.class, "blockAdvMachine");
        GameRegistry.registerBlock(ADVANCED_ENERGY_BLOCK, ItemAdvancedEnergyBlock.class, "blockAdvEnergyBlock");

        GameRegistry.registerTileEntity(TileEntityRotaryMacerator.class, "Rotary Macerator");
        GameRegistry.registerTileEntity(TileEntitySingularityCompressor.class, "Singularity Compressor");
        GameRegistry.registerTileEntity(TileEntityCentrifugeExtractor.class, "Centrifuge Extractor");
        GameRegistry.registerTileEntity(TileEntityAdvancedInduction.class, "Advanced Induction");
        GameRegistry.registerTileEntity(TileEntityCompactingRecycler.class, "Compacting Recycler");

        GameRegistry.registerTileEntity(TileEntityESU.class, "ESU");
        GameRegistry.registerTileEntity(TileEntityISU.class, "ISU");
        GameRegistry.registerTileEntity(TileEntityPESU.class, "PESU");
        GameRegistry.registerTileEntity(TileEntityTranformerEV.class, "EV Transformer");
        GameRegistry.registerTileEntity(TileEntityTranformerIV.class, "IV Transformer");
    }

    public static  <T extends Item> T registerItem(T item, String regName) {
        GameRegistry.registerItem(item, regName);
        return item;
    }
}
