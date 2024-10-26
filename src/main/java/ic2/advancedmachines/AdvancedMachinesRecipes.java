package ic2.advancedmachines;

import ic2.advancedmachines.blocks.BlockAdvancedMachines;
import ic2.api.item.Items;
import ic2.api.recipe.Recipes;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AdvancedMachinesRecipes {

    public static void init() {
        Recipes.advRecipes.addRecipe(BlockAdvancedMachines.AdvMachines.MACERATOR.STACK,
                "RRR", "RMR", "RAR",
                'R', Items.getItem("refinedIronIngot"),
                'M', Items.getItem("macerator"),
                'A', Items.getItem("advancedMachine"));

        Recipes.advRecipes.addRecipe(BlockAdvancedMachines.AdvMachines.COMPRESSOR.STACK,
                "RRR", "RMR", "RAR",
                'R', Block.obsidian,
                'M', Items.getItem("compressor"),
                'A', Items.getItem("advancedMachine"));

        Recipes.advRecipes.addRecipe(BlockAdvancedMachines.AdvMachines.EXTRACTOR.STACK,
                "RRR", "RMR", "RAR",
                'R', Items.getItem("electrolyzedWaterCell"),
                'M', Items.getItem("extractor"),
                'A', Items.getItem("advancedMachine"));

        Recipes.advRecipes.addRecipe(BlockAdvancedMachines.AdvMachines.INDUCTION.STACK,
                " C ", "RMR",
                'R', Item.redstone,
                'M', Items.getItem("inductionFurnace"),
                'C', Items.getItem("electronicCircuit"));

        Recipes.advRecipes.addRecipe(new ItemStack(BlocksItems.REDSTONE_UPGRADE),
                "T T", " L ", "T T",
                'T', "ingotTin",
                'L', Block.lever);
    }
}
