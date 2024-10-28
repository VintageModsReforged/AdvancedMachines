package ic2.advancedmachines;

import ic2.advancedmachines.blocks.AdvMachines;
import ic2.api.Ic2Recipes;
import ic2.api.Items;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class AdvancedMachinesRecipes {

    public static void init() {
        Ic2Recipes.addCraftingRecipe(AdvMachines.MACERATOR.STACK,
                "RRR", "RMR", "RAR",
                'R', Items.getItem("refinedIronIngot"),
                'M', Items.getItem("macerator"),
                'A', Items.getItem("advancedMachine"));

        Ic2Recipes.addCraftingRecipe(AdvMachines.COMPRESSOR.STACK,
                "RRR", "RMR", "RAR",
                'R', Block.obsidian,
                'M', Items.getItem("compressor"),
                'A', Items.getItem("advancedMachine"));

        Ic2Recipes.addCraftingRecipe(AdvMachines.EXTRACTOR.STACK,
                "RRR", "RMR", "RAR",
                'R', Items.getItem("electrolyzedWaterCell"),
                'M', Items.getItem("extractor"),
                'A', Items.getItem("advancedMachine"));

        Ic2Recipes.addCraftingRecipe(AdvMachines.INDUCTION.STACK,
                " C ", "RMR",
                'R', Item.redstone,
                'M', Items.getItem("inductionFurnace"),
                'C', Items.getItem("advancedCircuit"));

        Ic2Recipes.addCraftingRecipe(AdvMachines.RECYCLER.STACK,
                "RRR", "RMR", "RAR",
                'R', Block.pistonBase,
                'M', Items.getItem("recycler"),
                'A', Items.getItem("advancedMachine"));

        Ic2Recipes.addCraftingRecipe(BlocksItems.REDSTONE_INVERTER,
                "T T", " R ", "T T",
                'T', "ingotTin",
                'R', Block.torchRedstoneActive);

        Ic2Recipes.addCraftingRecipe(BlocksItems.COBBLESTONE_GENERATOR,
                "GPG", "L W", "G#G",
                'G', "blockGlass",
                'P', Item.pickaxeGold,
                'L', "lava",
                'W', "water",
                '#', Items.getItem("electronicCircuit"));
    }
}
