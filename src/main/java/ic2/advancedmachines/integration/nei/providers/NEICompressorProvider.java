package ic2.advancedmachines.integration.nei.providers;

import ic2.advancedmachines.blocks.gui.GuiAdvCompressor;
import ic2.api.recipe.Recipes;
import ic2.neiIntegration.core.MachineRecipeHandler;

import java.util.Map;

public class NEICompressorProvider extends MachineRecipeHandler {

    @Override
    public Class getGuiClass() {
        return GuiAdvCompressor.class;
    }

    @Override
    public String getRecipeName() {
        return "Compressor";
    }

    @Override
    public String getRecipeId() {
        return "ic2.compressor";
    }

    @Override
    public String getGuiTexture() {
        return "/mods/ic2/textures/gui/GUICompressor.png";
    }

    @Override
    public String getOverlayIdentifier() {
        return "compressor";
    }

    @Override
    public Map getRecipeList() {
        return Recipes.compressor.getRecipes();
    }
}
