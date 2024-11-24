package ic2.advancedmachines.integration.nei.providers;

import ic2.advancedmachines.blocks.gui.GuiAdvMacerator;
import ic2.api.recipe.Recipes;
import ic2.neiIntegration.core.MachineRecipeHandler;

import java.util.Map;

public class NEIMaceratorProvider extends MachineRecipeHandler {

    @Override
    public Class getGuiClass() {
        return GuiAdvMacerator.class;
    }

    @Override
    public String getRecipeName() {
        return "Macerator";
    }

    @Override
    public String getRecipeId() {
        return "ic2.macerator";
    }

    @Override
    public String getGuiTexture() {
        return "/mods/ic2/textures/gui/GUIMacerator.png";
    }

    @Override
    public String getOverlayIdentifier() {
        return "macerator";
    }

    @Override
    public Map getRecipeList() {
        return Recipes.macerator.getRecipes();
    }
}
