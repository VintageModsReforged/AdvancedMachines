package ic2.advancedmachines.utils;

import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlotProcessable;
import net.minecraft.item.ItemStack;

public class InvSlotFiltered extends InvSlotProcessable {

    IStackFilter FILTER;

    public InvSlotFiltered(TileEntityInventory base, String name, int oldStartIndex, int count, IStackFilter filter) {
        super(base, name, oldStartIndex, count);
        this.FILTER = filter;
    }

    @Override
    public boolean accepts(ItemStack stack) {
        return stack != null && this.FILTER.match(stack);
    }

    @Override
    public ItemStack process(boolean simulate) {
        ItemStack input = this.get();
        if (input == null) {
            return null;
        } else {
            ItemStack result = this.FILTER.getOutputFor(input, !simulate);
            if (result == null) {
                return null;
            } else return result.copy();
        }
    }
}
