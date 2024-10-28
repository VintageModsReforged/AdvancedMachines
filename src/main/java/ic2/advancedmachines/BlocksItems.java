package ic2.advancedmachines;

import cpw.mods.fml.common.registry.GameRegistry;
import ic2.advancedmachines.blocks.BlockAdvMachine;
import ic2.advancedmachines.blocks.tiles.*;
import ic2.advancedmachines.items.ItemAdvancedMachine;
import ic2.advancedmachines.items.upgrades.ItemAdvUpgrade;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlocksItems {

    public static Block ADVANCED_MACHINE_BLOCK;
    public static Item ADVANCED_UPGRADE;

    public static ItemStack REDSTONE_INVERTER;
    public static ItemStack COBBLESTONE_GENERATOR;

    public static void init() {
        ADVANCED_UPGRADE = registerItem(new ItemAdvUpgrade(), "advanced.upgrade");
        ADVANCED_MACHINE_BLOCK = new BlockAdvMachine(AdvancedMachinesConfig.ADV_MACHINE_ID);
        REDSTONE_INVERTER = new ItemStack(ADVANCED_UPGRADE, 1, 0);
        COBBLESTONE_GENERATOR = new ItemStack(ADVANCED_UPGRADE, 1, 1);

        GameRegistry.registerBlock(ADVANCED_MACHINE_BLOCK, ItemAdvancedMachine.class, "blockAdvMachine");

        GameRegistry.registerTileEntity(TileEntityRotaryMacerator.class, "Rotary Macerator");
        GameRegistry.registerTileEntity(TileEntitySingularityCompressor.class, "Singularity Compressor");
        GameRegistry.registerTileEntity(TileEntityCentrifugeExtractor.class, "Centrifuge Extractor");
        GameRegistry.registerTileEntity(TileEntityAdvancedInduction.class, "Advanced Induction");
        GameRegistry.registerTileEntity(TileEntityCompactedRecycler.class, "Compacting Recycler");
    }

    public static  <T extends Item> T registerItem(T item, String regName) {
        GameRegistry.registerItem(item, regName);
        return item;
    }
}
