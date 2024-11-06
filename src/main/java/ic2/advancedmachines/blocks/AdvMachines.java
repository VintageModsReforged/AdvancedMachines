package ic2.advancedmachines.blocks;

import ic2.advancedmachines.BlocksItems;
import net.minecraft.item.ItemStack;

public enum AdvMachines{
    MACERATOR,
    COMPRESSOR,
    EXTRACTOR,
    INDUCTION,
    RECYCLER/*,
    ELECTROLYZER*/;

    public final ItemStack STACK;
    public static final AdvMachines[] VALUES = values();

    AdvMachines() {
        this.STACK = new ItemStack(BlocksItems.ADVANCED_MACHINE_BLOCK, 1, ordinal());
    }
}
