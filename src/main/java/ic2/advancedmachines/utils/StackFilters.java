package ic2.advancedmachines.utils;

import ic2.advancedmachines.BlocksItems;
import ic2.advancedmachines.items.upgrades.ISimpleUpgrade;
import ic2.api.item.IElectricItem;
import ic2.api.item.Items;
import ic2.api.recipe.Recipes;
import ic2.core.item.ItemBatterySU;
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
            return stack != null && Recipes.macerator.getOutputFor(stack, false) != null;
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
            return stack != null && Recipes.compressor.getOutputFor(stack, false) != null;
        }
    };

    public static final IStackFilter EXTRACTOR_FILTER = new IStackFilter() {
        @Override
        public boolean match(ItemStack stack) {
            return stack != null && Recipes.extractor.getOutputFor(stack, false) != null || StackUtil.isStackEqual(stack, Items.getItem("scrapBox"));
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
            return stack != null && (stack.getItem() instanceof ic2.core.item.IUpgradeItem || stack.getItem() instanceof ISimpleUpgrade);
        }
    };

    public static final IStackFilter ELECTROLYZER_IN = new IStackFilter() {
        @Override
        public boolean match(ItemStack stack) {
            return stack != null && (AdvMachinesRecipes.electrolyzer_power.getOutputFor(stack, false) != null || AdvMachinesRecipes.electrolyzer_drain.getOutputFor(stack, false) != null);
        }
    };

    public static final IStackFilter ELECTROLYZER_OUT = new IStackFilter() {
        @Override
        public boolean match(ItemStack stack) {
            return StackUtil.isStackEqual(stack, Items.getItem("electrolyzedWaterCell")) || StackUtil.isStackEqual(stack, BlocksItems.MAGNET_COMPONENT);
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
                    return iee.canProvideEnergy(stack) && iee.getTier(stack) <= 2;
                }
                return false;
            } else {
                return true;
            }
        }
    };
}
