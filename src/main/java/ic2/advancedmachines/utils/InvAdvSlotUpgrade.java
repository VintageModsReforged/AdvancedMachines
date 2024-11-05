package ic2.advancedmachines.utils;

import ic2.advancedmachines.items.upgrades.ISimpleUpgrade;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlotUpgrade;
import net.minecraft.item.ItemStack;

public class InvAdvSlotUpgrade extends InvSlotUpgrade {

    public InvAdvSlotUpgrade(TileEntityInventory base, String name, int oldStartIndex, int count) {
        super(base, name, oldStartIndex, count);
    }

    @Override
    public boolean accepts(ItemStack stack) {
        if (stack != null) {
            return super.accepts(stack) || stack.getItem() instanceof ISimpleUpgrade;
        } else return false;
    }
}
