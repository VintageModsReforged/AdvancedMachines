package ic2.advancedmachines.utils;

import ic2.api.recipe.IMachineRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import java.util.Map;

@SuppressWarnings({"rawtypes"})
public class AdvFurnaceManager implements IMachineRecipeManager {

    @Override
    public void addRecipe(ItemStack itemStack, Object o) {}

    @Override
    public Object getOutputFor(ItemStack input, boolean adjustOutput) {
        ItemStack result = FurnaceRecipes.smelting().getSmeltingResult(input);
        if (adjustOutput && result != null) {
            --input.stackSize;
        }
        return result;
    }

    @Override
    public Map getRecipes() {
        return FurnaceRecipes.smelting().getSmeltingList();
    }
}
