package ic2.advancedmachines.utils;

import ic2.advancedmachines.BlocksItems;
import ic2.api.IElectricItem;
import ic2.api.Ic2Recipes;
import ic2.core.item.ItemBatterySU;
import ic2.core.item.ItemUpgradeModule;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class SlotFiltered extends Slot {

    IStackFilter FILTER;

    public SlotFiltered(IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition, IStackFilter filter) {
        super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
        this.FILTER = filter;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return this.FILTER.match(stack);
    }

    public static SlotFiltered maceratorSlot(IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition) {
        return new SlotFiltered(inventory, slotIndex, xDisplayPosition, yDisplayPosition, MACERATOR_FILTER);
    }

    public static SlotFiltered furnaceSlot(IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition) {
        return new SlotFiltered(inventory, slotIndex, xDisplayPosition, yDisplayPosition, FURNACE_FILTER);
    }

    public static SlotFiltered compressorSlot(IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition) {
        return new SlotFiltered(inventory, slotIndex, xDisplayPosition, yDisplayPosition, COMPRESSOR_FILTER);
    }

    public static SlotFiltered extractorSlot(IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition) {
        return new SlotFiltered(inventory, slotIndex, xDisplayPosition, yDisplayPosition, EXTRACTOR_FILTER);
    }

    public static SlotFiltered recyclerSlot(IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition) {
        return new SlotFiltered(inventory, slotIndex, xDisplayPosition, yDisplayPosition, RECYCLER_FILTER);
    }

    public static SlotFiltered upgradeSlot(IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition) {
        return new SlotFiltered(inventory, slotIndex, xDisplayPosition, yDisplayPosition, UPGRADE_FILTER);
    }

    public static SlotFiltered batterySlot(IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition) {
        return new SlotFiltered(inventory, slotIndex, xDisplayPosition, yDisplayPosition, BATTERY_FILTER);
    }

    public static final IStackFilter MACERATOR_FILTER = new IStackFilter() {
        @Override
        public boolean match(ItemStack stack) {
            return stack != null && Ic2Recipes.getMaceratorOutputFor(stack, false) != null;
        }
    };

    public static final IStackFilter FURNACE_FILTER = new IStackFilter() {
        @Override
        public boolean match(ItemStack stack) {
            return stack != null && FurnaceRecipes.smelting().getSmeltingResult(stack) != null;
        }
    };

    public static final IStackFilter COMPRESSOR_FILTER = new IStackFilter() {
        @Override
        public boolean match(ItemStack stack) {
            return stack != null && Ic2Recipes.getCompressorOutputFor(stack, false) != null;
        }
    };

    public static final IStackFilter EXTRACTOR_FILTER = new IStackFilter() {
        @Override
        public boolean match(ItemStack stack) {
            return stack != null && Ic2Recipes.getExtractorOutputFor(stack, false) != null;
        }
    };

    public static final IStackFilter RECYCLER_FILTER = new IStackFilter() {
        @Override
        public boolean match(ItemStack stack) {
            return !UPGRADE_FILTER.match(stack); // all, but upgrades
        }
    };

    public static final IStackFilter UPGRADE_FILTER = new IStackFilter() {
        @Override
        public boolean match(ItemStack stack) {
            return stack != null && (stack.getItem() instanceof ItemUpgradeModule || stack.isItemEqual(new ItemStack(BlocksItems.REDSTONE_UPGRADE)));
        }
    };

    public static final IStackFilter BATTERY_FILTER = new IStackFilter() {
        @Override
        public boolean match(ItemStack stack) {
            if (stack == null) {
                return false;
            } else if (stack.itemID != Item.redstone.itemID && !(stack.getItem() instanceof ItemBatterySU)) {
                if (stack.getItem() instanceof IElectricItem) {
                    IElectricItem iee = (IElectricItem) stack.getItem();
                    return iee.canProvideEnergy() && iee.getTier() <= 2;
                }
                return false;
            } else {
                return true;
            }
        }
    };
}
