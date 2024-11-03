package ic2.advancedmachines.integration.nei.providers;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.recipe.TemplateRecipeHandler;
import ic2.advancedmachines.utils.AdvMachinesRecipeManager;
import ic2.advancedmachines.utils.Refs;
import ic2.core.gui.GuiAdvElectrolyzer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
public class NEIElectrolyzerProvider extends TemplateRecipeHandler {

    int ticks;

    @Override
    public String getGuiTexture() {
        return Refs.getGuiPath("charged");
    }

    @Override
    public String getRecipeName() {
        return "Electrolyzer";
    }

    @Override
    public Class getGuiClass() {
        return GuiAdvElectrolyzer.class;
    }

    @Override
    public void drawBackground(GuiContainerManager gui, int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        gui.bindTextureByName(this.getGuiTexture());
        gui.drawTexturedModalRect(48, 18, 53, 34, 76, 18);
    }

    @Override
    public void drawExtras(GuiContainerManager gui, int recipe) {
        float f = this.ticks >= 20 ? (float) ((this.ticks - 20) % 20) / 20.0F : 0.0F;
        this.drawProgressBar(gui, 74, 18, 176, 14, 24, 17, f, 0);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        ++this.ticks;
    }

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(70, 23, 32, 17), "ic2.electrolyzer", new Object[0]));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("ic2.electrolyzer")) {
            List recipesDrain = AdvMachinesRecipeManager.getDrainElectrolyzerRecipes();
            Iterator recipesDrainIter = recipesDrain.iterator();
            while (recipesDrainIter.hasNext()) {
                Map.Entry recipe = (Map.Entry) recipesDrainIter.next();
                this.arecipes.add(new CachedIORecipe(recipe));
            }
            List recipesPower = AdvMachinesRecipeManager.getPowerElectrolyzerRecipes();
            Iterator recipesPowerIter = recipesPower.iterator();
            while (recipesPowerIter.hasNext()) {
                Map.Entry recipe = (Map.Entry) recipesPowerIter.next();
                this.arecipes.add(new CachedIORecipe(recipe));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        List recipesDrain = AdvMachinesRecipeManager.getDrainElectrolyzerRecipes();
        Iterator recipesDrainIter = recipesDrain.iterator();
        while (recipesDrainIter.hasNext()) {
            Map.Entry recipe = (Map.Entry) recipesDrainIter.next();
            if (NEIServerUtils.areStacksSameTypeCrafting((ItemStack) recipe.getValue(), result)) {
                this.arecipes.add(new CachedIORecipe(recipe));
            }
        }
        List recipesPower = AdvMachinesRecipeManager.getPowerElectrolyzerRecipes();
        Iterator recipesPowerIter = recipesPower.iterator();
        while (recipesPowerIter.hasNext()) {
            Map.Entry recipe = (Map.Entry) recipesPowerIter.next();
            if (NEIServerUtils.areStacksSameTypeCrafting((ItemStack) recipe.getValue(), result)) {
                this.arecipes.add(new CachedIORecipe(recipe));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        List recipesDrain = AdvMachinesRecipeManager.getDrainElectrolyzerRecipes();
        Iterator recipesDrainIter = recipesDrain.iterator();
        while (recipesDrainIter.hasNext()) {
            Map.Entry recipe = (Map.Entry) recipesDrainIter.next();
            if (NEIServerUtils.areStacksSameTypeCrafting((ItemStack) recipe.getKey(), ingredient)) {
                this.arecipes.add(new CachedIORecipe(recipe));
            }
        }
        List recipesPower = AdvMachinesRecipeManager.getPowerElectrolyzerRecipes();
        Iterator recipesPowerIter = recipesPower.iterator();
        while (recipesPowerIter.hasNext()) {
            Map.Entry recipe = (Map.Entry) recipesPowerIter.next();
            if (NEIServerUtils.areStacksSameTypeCrafting((ItemStack) recipe.getKey(), ingredient)) {
                this.arecipes.add(new CachedIORecipe(recipe));
            }
        }
    }

    public class CachedIORecipe extends TemplateRecipeHandler.CachedRecipe {
        public PositionedStack input;
        public PositionedStack output;

        public PositionedStack getIngredient() {
            return this.input;
        }

        public PositionedStack getResult() {
            return this.output;
        }

        public CachedIORecipe(ItemStack itemstack, ItemStack itemstack1) {
            this.input = new PositionedStack(itemstack, 49, 19);
            this.output = new PositionedStack(itemstack1, 107, 19);
        }

        public CachedIORecipe(Map.Entry recipe) {
            this((ItemStack) recipe.getKey(), (ItemStack) recipe.getValue());
        }
    }
}
