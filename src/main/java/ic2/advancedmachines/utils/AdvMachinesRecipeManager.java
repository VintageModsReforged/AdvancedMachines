package ic2.advancedmachines.utils;

import ic2.advancedmachines.blocks.tiles.machines.TileEntityChargedElectrolyzer;
import ic2.api.Items;
import ic2.core.block.machine.tileentity.TileEntityExtractor;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
public class AdvMachinesRecipeManager {

    public static void addDrainElectrolyzerRecipe(ItemStack input, ItemStack output) {
        getDrainElectrolyzerRecipes().add(new AbstractMap.SimpleEntry(input, output));
    }

    public static ItemStack getDrainElectrolyzerOutputFor(ItemStack stack, boolean consume) {
        return getOutputFor(stack, consume, getDrainElectrolyzerRecipes());
    }

    public static List getDrainElectrolyzerRecipes() {
        return TileEntityChargedElectrolyzer.recipesDrain;
    }

    public static void addPowerElectrolyzerRecipe(ItemStack input, ItemStack output) {
        getPowerElectrolyzerRecipes().add(new AbstractMap.SimpleEntry(input, output));
    }

    public static ItemStack getPowerElectrolyzerOutputFor(ItemStack stack, boolean consume) {
        return getOutputFor(stack, consume, getPowerElectrolyzerRecipes());
    }

    public static List getPowerElectrolyzerRecipes() {
        return TileEntityChargedElectrolyzer.recipesPower;
    }

    public static ItemStack getOutputForExtractor(ItemStack input, boolean adjustInput) {
        return getOutputFor(input, adjustInput, TileEntityExtractor.recipes);
    }

    private static ItemStack getOutputFor(ItemStack input, boolean adjustInput, List recipeList) {
        if (input == null) {
            return null;
        } else if (StackUtil.isStackEqual(input, Items.getItem("scrapBox"))) {
            ItemStack drop = ScrapBoxUtils.getDrop();
            if (adjustInput) {
                --input.stackSize;
            }
            return drop;
        } else {
            Iterator recipes = recipeList.iterator();
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
}
