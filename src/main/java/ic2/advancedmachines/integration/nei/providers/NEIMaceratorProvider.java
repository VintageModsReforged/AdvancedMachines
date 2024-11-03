package ic2.advancedmachines.integration.nei.providers;

import ic2.api.Ic2Recipes;
import ic2.core.gui.GuiAdvMacerator;
import ic2.neiIntegration.core.MachineRecipeHandler;

import java.util.List;

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
        return "/ic2/sprites/GUIMacerator.png";
    }

    @Override
    public String getOverlayIdentifier() {
        return "macerator";
    }

    @Override
    public List getRecipeList() {
        return Ic2Recipes.getMaceratorRecipes();
    }
}
