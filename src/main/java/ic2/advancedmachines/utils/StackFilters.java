package ic2.advancedmachines.utils;

import ic2.advancedmachines.BlocksItems;
import ic2.advancedmachines.items.IUpgradeItem;
import ic2.api.IElectricItem;
import ic2.api.Ic2Recipes;
import ic2.api.Items;
import ic2.core.item.ItemBatterySU;
import ic2.core.item.ItemUpgradeModule;
import ic2.core.util.StackUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class StackFilters {

    public static final IStackFilter ANYTHING = new IStackFilter() {
        @Override
        public boolean match(ItemStack stack) {
            return stack != null;
        }
    };

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
            return stack != null && Ic2Recipes.getExtractorOutputFor(stack, false) != null || StackUtil.isStackEqual(stack, Items.getItem("scrapBox"));
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
            return stack != null && (stack.getItem() instanceof ItemUpgradeModule || stack.getItem() instanceof IUpgradeItem);
        }
    };

    public static final IStackFilter ELECTROLYZER_IN = new IStackFilter() {
        @Override
        public boolean match(ItemStack stack) {
            return stack != null && (AdvMachinesRecipeManager.getPowerElectrolyzerOutputFor(stack, false) != null || AdvMachinesRecipeManager.getDrainElectrolyzerOutputFor(stack, false) != null);
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
