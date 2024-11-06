package ic2.advancedmachines.blocks;

import ic2.advancedmachines.BlocksItems;
import net.minecraft.item.ItemStack;

public enum AdvEnergyBlocks {
    ESU,
    ISU,
    PESU,
    TRANSFORMER_EV,
    TRANSFORMER_IV,
    TRANSFORMER_ADJ;

    public final ItemStack STACK;
    public static final AdvEnergyBlocks[] VALUES = values();

    AdvEnergyBlocks() {
        this.STACK = new ItemStack(BlocksItems.ADVANCED_ENERGY_BLOCK, 1, ordinal());
    }

}
