package ic2.advancedmachines;

import cpw.mods.fml.common.registry.GameRegistry;
import ic2.advancedmachines.blocks.BlockAdvancedMachine;
import ic2.advancedmachines.blocks.tiles.machines.TileEntityAdvancedInduction;
import ic2.advancedmachines.blocks.tiles.machines.TileEntityCentrifugeExtractor;
import ic2.advancedmachines.blocks.tiles.machines.TileEntityRotaryMacerator;
import ic2.advancedmachines.blocks.tiles.machines.TileEntitySingularityCompressor;
import ic2.advancedmachines.items.ItemAdvUpgrade;
import ic2.advancedmachines.items.ItemAdvancedMachine;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlocksItems {

    public static Block ADVANCED_MACHINE_BLOCK;
    public static Item REDSTONE_UPGRADE;

    public static Item ADVANCED_UPGRADE;
    public static ItemStack REDSTONE_INVERTER;
    public static ItemStack COBBLESTONE_GENERATOR;

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

        ADVANCED_MACHINE_BLOCK = new BlockAdvancedMachine(AdvancedMachinesConfig.ADV_MACHINE_ID);

        GameRegistry.registerBlock(ADVANCED_MACHINE_BLOCK, ItemAdvancedMachine.class, "blockAdvMachine");

        GameRegistry.registerTileEntity(TileEntityRotaryMacerator.class, "Rotary Macerator");
        GameRegistry.registerTileEntity(TileEntitySingularityCompressor.class, "Singularity Compressor");
        GameRegistry.registerTileEntity(TileEntityCentrifugeExtractor.class, "Centrifuge Extractor");
        GameRegistry.registerTileEntity(TileEntityAdvancedInduction.class, "Advanced Induction");
    }

    public static  <T extends Item> T registerItem(T item, String regName) {
        GameRegistry.registerItem(item, regName);
        return item;
    }
}
