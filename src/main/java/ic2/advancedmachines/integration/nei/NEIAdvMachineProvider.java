package ic2.advancedmachines.integration.nei;

import ic2.neiIntegration.core.MachineRecipeHandler;

import java.util.List;

public class NEIAdvMachineProvider extends MachineRecipeHandler {

    public Class machineClass;
    public String recipeName;
    public String recipeId;
    public String guiTexture;
    public String overlayId;
    public List recipeList;

    public NEIAdvMachineProvider(Class machineClass, String recipeName, String recipeId, String guiTexture, String overlayId, List recipeList) {
        this.machineClass = machineClass;
        this.recipeName = recipeName;
        this.recipeId = recipeId;
        this.guiTexture = guiTexture;
        this.overlayId = overlayId;
        this.recipeList = recipeList;
    }

    @Override
    public Class getGuiClass() {
        return this.machineClass;
    }

    @Override
    public String getRecipeName() {
        return this.recipeName;
    }

    @Override
    public String getRecipeId() {
        return this.recipeId;
    }

    @Override
    public String getGuiTexture() {
        return "/ic2/sprites/GUI" + this.guiTexture + ".png";
    }

    @Override
    public String getOverlayIdentifier() {
        return this.overlayId;
    }

    @Override
    public List getRecipeList() {
        return this.recipeList;
    }
}
