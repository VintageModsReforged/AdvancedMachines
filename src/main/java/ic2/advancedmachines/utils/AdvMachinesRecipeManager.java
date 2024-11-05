package ic2.advancedmachines.utils;

import ic2.api.recipe.IMachineRecipeManager;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public class AdvMachinesRecipeManager implements IMachineRecipeManager {

    private final Map recipes = new HashMap();

    @Override
    public void addRecipe(ItemStack input, Object output) {
        this.recipes.put(input, output);
    }

    @Override
    public Object getOutputFor(ItemStack input, boolean adjustInput) {
        if (input == null) {
            return null;
        } else {
            Iterator recipes = this.recipes.entrySet().iterator();
            Map.Entry entry;
            do {
                if (!recipes.hasNext()) {
                    return null;
                }

                entry = (Map.Entry) recipes.next();
            } while (!((ItemStack) entry.getKey()).isItemEqual(input) || input.stackSize < ((ItemStack) entry.getKey()).stackSize);

            if (adjustInput) {
                input.stackSize -= ((ItemStack) entry.getKey()).stackSize;
            }
            return ((ItemStack) entry.getValue()).copy();
        }
    }

    @Override
    public Map getRecipes() {
        return this.recipes;
    }
}
