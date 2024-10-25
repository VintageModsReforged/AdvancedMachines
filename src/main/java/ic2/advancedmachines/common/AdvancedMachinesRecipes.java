package ic2.advancedmachines.common;

import ic2.api.Ic2Recipes;
import ic2.api.Items;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AdvancedMachinesRecipes {

    public static void init() {
        Ic2Recipes.addCraftingRecipe(BlockAdvancedMachines.AdvMachines.MACERATOR.STACK,
                "RRR", "RMR", "RAR",
                'R', Items.getItem("refinedIronIngot"),
                'M', Items.getItem("macerator"),
                'A', Items.getItem("advancedMachine"));

        Ic2Recipes.addCraftingRecipe(BlockAdvancedMachines.AdvMachines.COMPRESSOR.STACK,
                "RRR", "RMR", "RAR",
                'R', Block.obsidian,
                'M', Items.getItem("compressor"),
                'A', Items.getItem("advancedMachine"));

        Ic2Recipes.addCraftingRecipe(BlockAdvancedMachines.AdvMachines.EXTRACTOR.STACK,
                "RRR", "RMR", "RAR",
                'R', Items.getItem("electrolyzedWaterCell"),
                'M', Items.getItem("extractor"),
                'A', Items.getItem("advancedMachine"));

        Ic2Recipes.addCraftingRecipe(BlockAdvancedMachines.AdvMachines.INDUCTION.STACK,
                " C ", "RMR",
                'R', Item.redstone,
                'M', Items.getItem("inductionFurnace"),
                'C', Items.getItem("advancedCircuit"));

        Ic2Recipes.addCraftingRecipe(new ItemStack(BlocksItems.REDSTONE_UPGRADE),
                "T T", " R ", "T T",
                'T', "ingotTin",
                'R', Block.torchRedstoneActive);
    }
}
