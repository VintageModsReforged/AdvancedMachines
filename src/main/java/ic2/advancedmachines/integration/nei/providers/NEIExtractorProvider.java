package ic2.advancedmachines.integration.nei.providers;

import ic2.advancedmachines.blocks.gui.GuiAdvExtractor;
import ic2.api.recipe.Recipes;
import ic2.neiIntegration.core.MachineRecipeHandler;

import java.util.Map;

public class NEIExtractorProvider extends MachineRecipeHandler {

    @Override
    public Class getGuiClass() {
        return GuiAdvExtractor.class;
    }

    @Override
    public String getRecipeName() {
        return "Extractor";
    }

    @Override
    public String getRecipeId() {
        return "ic2.extractor";
    }

    @Override
    public String getGuiTexture() {
        return "/ic2/sprites/GUIExtractor.png";
    }

    @Override
    public String getOverlayIdentifier() {
        return "extractor";
    }

    @Override
    public Map getRecipeList() {
        return Recipes.extractor.getRecipes();
    }
}
