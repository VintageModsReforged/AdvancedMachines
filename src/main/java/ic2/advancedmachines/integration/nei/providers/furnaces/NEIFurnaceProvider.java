package ic2.advancedmachines.integration.nei.providers.furnaces;

import codechicken.nei.recipe.FurnaceRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;

import java.awt.*;

public abstract class NEIFurnaceProvider extends FurnaceRecipeHandler {

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(74, 23, 24, 18), "smelting"));
    }
}
