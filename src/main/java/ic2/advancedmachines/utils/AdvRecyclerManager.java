package ic2.advancedmachines.utils;

import ic2.api.recipe.IMachineRecipeManager;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class AdvRecyclerManager implements IMachineRecipeManager {

    @Override
    public void addRecipe(ItemStack itemStack, Object o) {}

    @Override
    public Object getOutputFor(ItemStack itemStack, boolean b) {
        return ScrapBoxUtils.getDrop();
    }

    @Override
    public Map getRecipes() {
        return null;
    }
}
