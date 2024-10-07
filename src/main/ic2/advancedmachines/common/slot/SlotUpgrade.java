package ic2.advancedmachines.common.slot;

import ic2.advancedmachines.common.AdvancedMachines;
import ic2.core.item.ItemUpgradeModule;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotUpgrade extends Slot {

    public SlotUpgrade(IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition) {
        super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof ItemUpgradeModule || stack.getItem() == AdvancedMachines.redstoneUpgrade;
    }
}
