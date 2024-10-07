package ic2.advancedmachines.common.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class SlotSmelter extends Slot {
    public SlotSmelter(IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition) {
        super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return FurnaceRecipes.smelting().getSmeltingResult(stack) != null;
    }
}
