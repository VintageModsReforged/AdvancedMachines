package ic2.advancedmachines.utils;

import ic2.advancedmachines.items.upgrades.ISimpleUpgrade;
import ic2.api.item.Items;
import ic2.api.recipe.Recipes;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class StackFilters {

    public static final IStackFilter ANYTHING = new IStackFilter() {
        @Override
        public boolean match(ItemStack stack) {
            return stack != null;
        }

        @Override
        public ItemStack getOutputFor(ItemStack input, boolean adjustInput) {
            return null;
        }
    };

    public static final IStackFilter MACERATOR_FILTER = new IStackFilter() {
        @Override
        public boolean match(ItemStack stack) {
            return stack != null && getOutputFor(stack, false) != null;
        }

        @Override
        public ItemStack getOutputFor(ItemStack input, boolean adjustInput) {
            return (ItemStack) Recipes.macerator.getOutputFor(input, adjustInput);
        }
    };

    public static final IStackFilter FURNACE_FILTER = new IStackFilter() {
        @Override
        public boolean match(ItemStack stack) {
            return stack != null && getOutputFor(stack, false) != null;
        }

        @Override
        public ItemStack getOutputFor(ItemStack input, boolean adjustInput) {
            return FurnaceRecipes.smelting().getSmeltingResult(input);
        }
    };

    public static final IStackFilter COMPRESSOR_FILTER = new IStackFilter() {
        @Override
        public boolean match(ItemStack stack) {
            return stack != null && getOutputFor(stack, false) != null;
        }

        @Override
        public ItemStack getOutputFor(ItemStack input, boolean adjustInput) {
            return (ItemStack) Recipes.compressor.getOutputFor(input, adjustInput);
        }
    };

    public static final IStackFilter EXTRACTOR_FILTER = new IStackFilter() {
        @Override
        public boolean match(ItemStack stack) {
            return stack != null && getOutputFor(stack, false) != null || StackUtil.isStackEqual(stack, Items.getItem("scrapBox"));
        }

        @Override
        public ItemStack getOutputFor(ItemStack input, boolean adjustInput) {
            if (input.isItemEqual(Items.getItem("scrapBox"))) {
                return ScrapBoxUtils.getDrop();
            } else return (ItemStack) Recipes.extractor.getOutputFor(input, adjustInput);
        }
    };

    public static final IStackFilter RECYCLER_FILTER = new IStackFilter() {
        @Override
        public boolean match(ItemStack stack) {
            return !UPGRADE_FILTER.match(stack); // all, but upgrades
        }

        @Override
        public ItemStack getOutputFor(ItemStack input, boolean adjustInput) {
            return null;
        }
    };

    public static final IStackFilter UPGRADE_FILTER = new IStackFilter() {
        @Override
        public boolean match(ItemStack stack) {
            return stack != null && (stack.getItem() instanceof ic2.core.item.IUpgradeItem || stack.getItem() instanceof ISimpleUpgrade);
        }

        @Override
        public ItemStack getOutputFor(ItemStack input, boolean adjustInput) {
            return null;
        }
    };

    public static final IStackFilter ELECTROLYZER_IN = new IStackFilter() {
        @Override
        public boolean match(ItemStack stack) {
            return stack != null && (AdvMachinesRecipes.electrolyzer_power.getOutputFor(stack, false) != null || AdvMachinesRecipes.electrolyzer_drain.getOutputFor(stack, false) != null);
        }

        @Override
        public ItemStack getOutputFor(ItemStack input, boolean adjustInput) {
            return null;
        }
    };
}
