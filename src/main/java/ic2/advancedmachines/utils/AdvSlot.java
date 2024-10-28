package ic2.advancedmachines.utils;

import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class AdvSlot extends Slot {

    IStackFilter FILTER;

    public AdvSlot(IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition, IStackFilter filter) {
        super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
        this.FILTER = filter;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return this.FILTER.match(stack);
    }

    public static AdvSlot filtered(IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition) {
        IStackFilter filter = StackFilters.ANYTHING;
        if (inventory instanceof TileEntityAdvancedMachine) {
            TileEntityAdvancedMachine machine = (TileEntityAdvancedMachine) inventory;
            filter = machine.inputFilter;
        }
        return new AdvSlot(inventory, slotIndex, xDisplayPosition, yDisplayPosition, filter);
    }

    public static AdvSlot filtered(IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition, IStackFilter filter) {
        return new AdvSlot(inventory, slotIndex, xDisplayPosition, yDisplayPosition, filter);
    }
}
