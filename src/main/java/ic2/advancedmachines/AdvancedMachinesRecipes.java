package ic2.advancedmachines;

import ic2.advancedmachines.blocks.AdvMachines;
import ic2.advancedmachines.utils.AdvMachinesRecipes;
import ic2.advancedmachines.utils.ScrapBoxUtils;
import ic2.api.item.Items;
import ic2.api.recipe.Recipes;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class AdvancedMachinesRecipes {

    public static void init() {
        ScrapBoxUtils.init();
        AdvMachinesRecipes.electrolyzer_drain.addRecipe(Items.getItem("waterCell"), Items.getItem("electrolyzedWaterCell"));
        AdvMachinesRecipes.electrolyzer_drain.addRecipe(BlocksItems.MAGNET_DEAD, BlocksItems.MAGNET_COMPONENT);
        AdvMachinesRecipes.electrolyzer_power.addRecipe(Items.getItem("electrolyzedWaterCell"), Items.getItem("waterCell"));
        Recipes.compressor.addRecipe(BlocksItems.MAGNET_CHUNK, BlocksItems.MAGNET_DEAD);
        Recipes.extractor.addRecipe(Items.getItem("scrapBox"), Item.stick);

        Recipes.advRecipes.addRecipe(AdvMachines.MACERATOR.STACK,
                "RRR", "RMR", "RAR",
                'R', Items.getItem("refinedIronIngot"),
                'M', Items.getItem("macerator"),
                'A', Items.getItem("advancedMachine"));

        Recipes.advRecipes.addRecipe(AdvMachines.COMPRESSOR.STACK,
                "RRR", "RMR", "RAR",
                'R', Block.obsidian,
                'M', Items.getItem("compressor"),
                'A', Items.getItem("advancedMachine"));

        Recipes.advRecipes.addRecipe(AdvMachines.EXTRACTOR.STACK,
                "RRR", "RMR", "RAR",
                'R', Items.getItem("electrolyzedWaterCell"),
                'M', Items.getItem("extractor"),
                'A', Items.getItem("advancedMachine"));

        Recipes.advRecipes.addRecipe(AdvMachines.INDUCTION.STACK,
                " C ", "RMR",
                'R', Item.redstone,
                'M', Items.getItem("inductionFurnace"),
                'C', Items.getItem("electronicCircuit"));

        Recipes.advRecipes.addRecipe(BlocksItems.REDSTONE_INVERTER,
                "T T", " L ", "T T",
                'T', "ingotTin",
                'L', Block.lever);
    }
}
