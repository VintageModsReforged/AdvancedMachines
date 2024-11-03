package ic2.advancedmachines.integration.nei.providers;

import ic2.api.Ic2Recipes;
import ic2.core.gui.GuiAdvCompressor;
import ic2.core.gui.GuiAdvMacerator;
import ic2.neiIntegration.core.MachineRecipeHandler;

import java.util.List;

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
        return "/ic2/sprites/GUICompressor.png";
    }

    @Override
    public String getOverlayIdentifier() {
        return "compressor";
    }

    @Override
    public List getRecipeList() {
        return Ic2Recipes.getCompressorRecipes();
    }
}
