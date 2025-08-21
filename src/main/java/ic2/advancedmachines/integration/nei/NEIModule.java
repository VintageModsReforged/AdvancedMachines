package ic2.advancedmachines.integration.nei;

import ic2.advancedmachines.BlocksItems;
import ic2.advancedmachines.blocks.AdvEnergyBlocks;
import ic2.advancedmachines.blocks.AdvMachines;
import mods.vintage.core.helpers.nei.NEIHelper;

import java.util.ArrayList;
import java.util.List;

public class NEIModule {

    public static void init() {
        String items = "IC2.AdvancedMachines.Items";
        String blocks = "IC2.AdvancedMachines.Blocks";
        List<Integer> blockIds = new ArrayList<Integer>();
        for (AdvMachines machine : AdvMachines.VALUES) {
            blockIds.add(machine.STACK.itemID);
        }

        for (AdvEnergyBlocks energyBlock : AdvEnergyBlocks.VALUES) {
            blockIds.add(energyBlock.STACK.itemID);
        }
        NEIHelper.addCategory(blocks, blockIds.toArray(new Integer[blockIds.size()]));

        NEIHelper.addCategory(items,
                BlocksItems.GLOWTRONIC_CRYSTAL.itemID,
                BlocksItems.UNIVERSAL_CRYSTAL.itemID,
                BlocksItems.PLASMA_CRYSTAL.itemID,
                BlocksItems.REDSTONE_INVERTER.itemID,
                BlocksItems.COBBLESTONE_GENERATOR.itemID,
                BlocksItems.MAGNET_CHUNK.itemID,
                BlocksItems.MAGNET_DEAD.itemID,
                BlocksItems.MAGNET_COMPONENT.itemID,
                BlocksItems.CIRCUIT_COMPLEX.itemID,
                BlocksItems.IRIDIUM_CORE.itemID
        );
    }
}
