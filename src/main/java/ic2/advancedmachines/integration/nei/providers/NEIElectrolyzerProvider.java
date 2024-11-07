package ic2.advancedmachines.integration.nei.providers;

import codechicken.nei.PositionedStack;
import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.recipe.TemplateRecipeHandler;
import ic2.advancedmachines.blocks.gui.GuiAdvancedElectrolyzer;
import ic2.advancedmachines.blocks.tiles.machines.TileEntityChargedElectrolyzer;
import ic2.advancedmachines.utils.Refs;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;
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
        return GuiAdvancedElectrolyzer.class;
    }

    public void drawBackground(GuiContainerManager gui, int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        gui.bindTexture(this.getGuiTexture());
        gui.drawTexturedModalRect(48, 18, 53, 34, 76, 18);
    }

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
        this.transferRects.add(new RecipeTransferRect(new Rectangle(70, 23, 32, 17), "ic2.electrolyzer"));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("ic2.electrolyzer")) {
            Map recipesDrain = TileEntityChargedElectrolyzer.electrolyzer_drain.getRecipes();
            for (Object object : recipesDrain.entrySet()) {
                Map.Entry recipe = (Map.Entry) object;
                this.arecipes.add(new CachedIORecipe(recipe));
            }

            Map recipesPower = TileEntityChargedElectrolyzer.electrolyzer_power.getRecipes();
            for (Object object : recipesPower.entrySet()) {
                Map.Entry recipe = (Map.Entry) object;
                this.arecipes.add(new CachedIORecipe(recipe));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    public void loadCraftingRecipes(ItemStack result) {
        Map recipesDrain = TileEntityChargedElectrolyzer.electrolyzer_drain.getRecipes();
        for (Object object : recipesDrain.entrySet()) {
            Map.Entry recipe = (Map.Entry) object;
            if (StackUtil.isStackEqual((ItemStack) recipe.getValue(), result)) {
                this.arecipes.add(new CachedIORecipe(recipe));
            }
        }
        Map recipesPower = TileEntityChargedElectrolyzer.electrolyzer_power.getRecipes();
        for (Object object : recipesPower.entrySet()) {
            Map.Entry recipe = (Map.Entry) object;
            if (StackUtil.isStackEqual((ItemStack) recipe.getValue(), result)) {
                this.arecipes.add(new CachedIORecipe(recipe));
            }
        }
    }

    public void loadUsageRecipes(ItemStack ingredient) {
        Map recipesDrain = TileEntityChargedElectrolyzer.electrolyzer_drain.getRecipes();
        for (Object object : recipesDrain.entrySet()) {
            Map.Entry recipe = (Map.Entry) object;
            if (StackUtil.isStackEqual((ItemStack) recipe.getKey(), ingredient)) {
                this.arecipes.add(new CachedIORecipe(recipe));
            }
        }
        Map recipesPower = TileEntityChargedElectrolyzer.electrolyzer_power.getRecipes();
        for (Object object : recipesPower.entrySet()) {
            Map.Entry recipe = (Map.Entry) object;
            if (StackUtil.isStackEqual((ItemStack) recipe.getKey(), ingredient)) {
                this.arecipes.add(new CachedIORecipe(recipe));
            }
        }
    }

    public class CachedIORecipe extends CachedRecipe {
        public PositionedStack input;
        public PositionedStack output;

        public PositionedStack getIngredient() {
            return this.input;
        }

        public PositionedStack getResult() {
            return this.output;
        }

        public CachedIORecipe(ItemStack input, ItemStack output) {
            this.input = new PositionedStack(input, 49, 19);
            this.output = new PositionedStack(output, 107, 19);
        }

        public CachedIORecipe(Map.Entry recipe) {
            this((ItemStack) recipe.getKey(), (ItemStack) recipe.getValue());
        }
    }
}
