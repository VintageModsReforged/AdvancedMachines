package ic2.advancedmachines.utils;

import ic2.advancedmachines.BlocksItems;
import ic2.core.block.TileEntityInventory;
import net.minecraft.item.ItemStack;

public class InvAdvSlotUpgrade extends ic2.core.block.invslot.InvSlotUpgrade {

    public InvAdvSlotUpgrade(TileEntityInventory base, String name, int oldStartIndex, int count) {
        super(base, name, oldStartIndex, count);
    }

    @Override
    public boolean accepts(ItemStack stack) {
        if (stack != null) {
            return super.accepts(stack) || stack.isItemEqual(new ItemStack(BlocksItems.REDSTONE_UPGRADE));
        } else return false;
    }
}
