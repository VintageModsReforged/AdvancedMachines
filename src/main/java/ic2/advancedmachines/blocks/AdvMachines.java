package ic2.advancedmachines.blocks;

import ic2.advancedmachines.BlocksItems;
import net.minecraft.item.ItemStack;

public enum AdvMachines{
    MACERATOR(0),
    COMPRESSOR(1),
    EXTRACTOR(2),
    INDUCTION(3),
    RECYCLER(4);

    public ItemStack STACK;

    AdvMachines(int meta) {
        this.STACK = new ItemStack(BlocksItems.ADVANCED_MACHINE_BLOCK, 1, meta);
    }
}