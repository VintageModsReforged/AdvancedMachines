package ic2.advancedmachines.integration.nei.providers.furnaces;

import codechicken.nei.recipe.FurnaceRecipeHandler;

import java.awt.*;

public abstract class NEIFurnaceProvider extends FurnaceRecipeHandler {

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(74, 23, 24, 18), "smelting"));
    }
}
