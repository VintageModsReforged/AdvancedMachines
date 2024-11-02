package ic2.advancedmachines;

import ic2.advancedmachines.blocks.AdvEnergyBlocks;
import ic2.advancedmachines.blocks.AdvMachines;
import ic2.advancedmachines.utils.AdvMachinesRecipeManager;
import ic2.api.Ic2Recipes;
import ic2.api.Items;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AdvancedMachinesRecipes {

    public static void init() {

        AdvMachinesRecipeManager.addDrainElectrolyzerRecipe(Items.getItem("waterCell"), Items.getItem("electrolyzedWaterCell"));
        AdvMachinesRecipeManager.addDrainElectrolyzerRecipe(BlocksItems.MAGNET_DEAD, BlocksItems.MAGNET_COMPONENT);
        AdvMachinesRecipeManager.addPowerElectrolyzerRecipe(Items.getItem("electrolyzedWaterCell"), Items.getItem("waterCell"));
        Ic2Recipes.addCompressorRecipe(BlocksItems.MAGNET_CHUNK, BlocksItems.MAGNET_DEAD);

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

        Ic2Recipes.addCraftingRecipe(AdvMachines.ELECTROLYZER.STACK,
                "RRR", "RMR", "RAR",
                'R', Items.getItem("lvTransformer"),
                'M', Items.getItem("electrolyzer"),
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

        Ic2Recipes.addCraftingRecipe(BlocksItems.MAGNET_CHUNK,
                "CCC", "CRC", "CCC",
                'C', Items.getItem("clayDust"),
                'R', Items.getItem("refinedIronIngot"));

        Ic2Recipes.addCraftingRecipe(BlocksItems.CIRCUIT_COMPLEX,
                "CCC", "M#M", "CCC",
                'C', Items.getItem("doubleInsulatedGoldCableItem"),
                'M', BlocksItems.MAGNET_COMPONENT,
                '#', Items.getItem("advancedCircuit"));

        Ic2Recipes.addCraftingRecipe(BlocksItems.CIRCUIT_COMPLEX,
                "CMC", "C#C", "CMC",
                'C', Items.getItem("doubleInsulatedGoldCableItem"),
                'M', BlocksItems.MAGNET_COMPONENT,
                '#', Items.getItem("advancedCircuit"));

        Ic2Recipes.addCraftingRecipe(BlocksItems.IRIDIUM_CORE,
                "MCM", "CIC", "MCM",
                'C', Items.getItem("carbonPlate"),
                'M', BlocksItems.MAGNET_COMPONENT,
                'I', Items.getItem("iridiumOre"));

        Ic2Recipes.addCraftingRecipe(new ItemStack(BlocksItems.GLOWTRONIC_CRYSTAL),
                "G#G", "GCG", "G#G",
                'G', Item.lightStoneDust,
                '#', Items.getItem("advancedCircuit"),
                'C', Items.getItem("lapotronCrystal"));

        Ic2Recipes.addCraftingRecipe(new ItemStack(BlocksItems.UNIVERSAL_CRYSTAL),
                "G#G", "GCG", "G#G",
                'G', Items.getItem("carbonPlate"),
                '#', BlocksItems.CIRCUIT_COMPLEX,
                'C', BlocksItems.GLOWTRONIC_CRYSTAL);

        Ic2Recipes.addCraftingRecipe(new ItemStack(BlocksItems.PLASMA_CRYSTAL),
                "G#G", "GCG", "G#G",
                'G', Items.getItem("carbonPlate"),
                '#', Items.getItem("iridiumPlate"),
                'C', BlocksItems.UNIVERSAL_CRYSTAL);

        Ic2Recipes.addCraftingRecipe(AdvEnergyBlocks.ESU.STACK,
                "C#C", "CSC", "CMC",
                'C', BlocksItems.GLOWTRONIC_CRYSTAL,
                '#', BlocksItems.CIRCUIT_COMPLEX,
                'S', Items.getItem("mfsUnit"),
                'M', Items.getItem("advancedMachine"));

        Ic2Recipes.addCraftingRecipe(AdvEnergyBlocks.ISU.STACK,
                "C#C", "CSC", "CMC",
                'C', BlocksItems.UNIVERSAL_CRYSTAL,
                '#', BlocksItems.CIRCUIT_COMPLEX,
                'S', AdvEnergyBlocks.ESU.STACK,
                'M', Items.getItem("advancedMachine"));

        Ic2Recipes.addCraftingRecipe(AdvEnergyBlocks.PESU.STACK,
                "C#C", "CSC", "CMC",
                'C', BlocksItems.PLASMA_CRYSTAL,
                '#', BlocksItems.IRIDIUM_CORE,
                'S', AdvEnergyBlocks.ISU.STACK,
                'M', Items.getItem("advancedMachine"));

        Ic2Recipes.addCraftingRecipe(AdvEnergyBlocks.TRANSFORMER_EV.STACK,
                " C ", "#TX", " C ",
                'C', Items.getItem("glassFiberCableItem"),
                '#', Items.getItem("advancedCircuit"),
                'T', Items.getItem("hvTransformer"),
                'X', Items.getItem("lapotronCrystal"));

        Ic2Recipes.addCraftingRecipe(AdvEnergyBlocks.TRANSFORMER_IV.STACK,
                " C ", "#TX", " C ",
                'C', Items.getItem("trippleInsulatedIronCableItem"),
                '#', BlocksItems.CIRCUIT_COMPLEX,
                'T', AdvEnergyBlocks.TRANSFORMER_EV.STACK,
                'X', BlocksItems.GLOWTRONIC_CRYSTAL);

        Ic2Recipes.addCraftingRecipe(AdvEnergyBlocks.TRANSFORMER_ADJ.STACK,
                "T", "#", "M",
                'T', Items.getItem("lvTransformer"),
                '#', BlocksItems.CIRCUIT_COMPLEX,
                'M', AdvEnergyBlocks.TRANSFORMER_EV.STACK);
    }
}
