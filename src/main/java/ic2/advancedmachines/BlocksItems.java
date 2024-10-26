package ic2.advancedmachines;

import cpw.mods.fml.common.registry.GameRegistry;
import ic2.advancedmachines.blocks.BlockAdvancedMachines;
import ic2.advancedmachines.blocks.tiles.TileEntityAdvancedInduction;
import ic2.advancedmachines.blocks.tiles.TileEntityCentrifugeExtractor;
import ic2.advancedmachines.blocks.tiles.TileEntityRotaryMacerator;
import ic2.advancedmachines.blocks.tiles.TileEntitySingularityCompressor;
import ic2.advancedmachines.items.ItemAdvancedMachine;
import ic2.advancedmachines.items.ItemBase;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class BlocksItems {

    public static Block ADVANCED_MACHINE_BLOCK;
    public static Item REDSTONE_UPGRADE;

    public static void init() {
        REDSTONE_UPGRADE = new ItemBase(AdvancedMachinesConfig.REDSTONE_UPGRADE_ID, "redstone.inverter");
        ADVANCED_MACHINE_BLOCK = new BlockAdvancedMachines(AdvancedMachinesConfig.ADV_MACHINE_ID);

        GameRegistry.registerItem(REDSTONE_UPGRADE, "redstone.inverter");
        GameRegistry.registerBlock(ADVANCED_MACHINE_BLOCK, ItemAdvancedMachine.class, "blockAdvMachine");

        GameRegistry.registerTileEntity(TileEntityRotaryMacerator.class, "Rotary Macerator");
        GameRegistry.registerTileEntity(TileEntitySingularityCompressor.class, "Singularity Compressor");
        GameRegistry.registerTileEntity(TileEntityCentrifugeExtractor.class, "Centrifuge Extractor");
        GameRegistry.registerTileEntity(TileEntityAdvancedInduction.class, "Advanced Induction");
    }
}
