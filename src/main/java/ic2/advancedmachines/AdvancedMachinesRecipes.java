package ic2.advancedmachines;

import ic2.advancedmachines.blocks.AdvEnergyBlocks;
import ic2.advancedmachines.blocks.AdvMachines;
import ic2.advancedmachines.blocks.tiles.machines.TileEntityChargedElectrolyzer;
import ic2.advancedmachines.utils.ScrapBoxUtils;
import ic2.api.item.Items;
import ic2.api.recipe.Recipes;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AdvancedMachinesRecipes {

    public static void init() {
        ScrapBoxUtils.init();
        TileEntityChargedElectrolyzer.electrolyzer_drain.addRecipe(Items.getItem("waterCell"), Items.getItem("electrolyzedWaterCell"));
        TileEntityChargedElectrolyzer.electrolyzer_drain.addRecipe(BlocksItems.MAGNET_DEAD, BlocksItems.MAGNET_COMPONENT);
        TileEntityChargedElectrolyzer.electrolyzer_power.addRecipe(Items.getItem("electrolyzedWaterCell"), Items.getItem("waterCell"));
        Recipes.compressor.addRecipe(BlocksItems.MAGNET_CHUNK, BlocksItems.MAGNET_DEAD);
        Recipes.extractor.addRecipe(Items.getItem("scrapBox"), new ItemStack(Item.stick));

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
                'C', Items.getItem("advancedCircuit"));

        Recipes.advRecipes.addRecipe(AdvMachines.RECYCLER.STACK,
                "RRR", "RMR", "RAR",
                'R', Block.pistonBase,
                'M', Items.getItem("recycler"),
                'A', Items.getItem("advancedMachine"));

        Recipes.advRecipes.addRecipe(AdvMachines.ELECTROLYZER.STACK,
                "RRR", "RMR", "RAR",
                'R', Items.getItem("lvTransformer"),
                'M', Items.getItem("electrolyzer"),
                'A', Items.getItem("advancedMachine"));

        Recipes.advRecipes.addRecipe(BlocksItems.REDSTONE_INVERTER,
                "T T", " R ", "T T",
                'T', "ingotTin",
                'R', Block.torchRedstoneActive);

        Recipes.advRecipes.addRecipe(BlocksItems.COBBLESTONE_GENERATOR,
                "GPG", "L W", "G#G",
                'G', "blockGlass",
                'P', Item.pickaxeGold,
                'L', "lava",
                'W', "water",
                '#', Items.getItem("electronicCircuit"));

        Recipes.advRecipes.addRecipe(BlocksItems.MAGNET_CHUNK,
                "CCC", "CRC", "CCC",
                'C', Items.getItem("clayDust"),
                'R', Items.getItem("refinedIronIngot"));

        Recipes.advRecipes.addRecipe(BlocksItems.CIRCUIT_COMPLEX,
                "CCC", "M#M", "CCC",
                'C', Items.getItem("doubleInsulatedGoldCableItem"),
                'M', BlocksItems.MAGNET_COMPONENT,
                '#', Items.getItem("advancedCircuit"));

        Recipes.advRecipes.addRecipe(BlocksItems.CIRCUIT_COMPLEX,
                "CMC", "C#C", "CMC",
                'C', Items.getItem("doubleInsulatedGoldCableItem"),
                'M', BlocksItems.MAGNET_COMPONENT,
                '#', Items.getItem("advancedCircuit"));

        Recipes.advRecipes.addRecipe(BlocksItems.IRIDIUM_CORE,
                "MCM", "CIC", "MCM",
                'C', Items.getItem("carbonPlate"),
                'M', BlocksItems.MAGNET_COMPONENT,
                'I', Items.getItem("iridiumOre"));

        Recipes.advRecipes.addRecipe(new ItemStack(BlocksItems.GLOWTRONIC_CRYSTAL),
                "G#G", "GCG", "G#G",
                'G', Item.lightStoneDust,
                '#', Items.getItem("advancedCircuit"),
                'C', Items.getItem("lapotronCrystal"));

        Recipes.advRecipes.addRecipe(new ItemStack(BlocksItems.UNIVERSAL_CRYSTAL),
                "G#G", "GCG", "G#G",
                'G', Items.getItem("carbonPlate"),
                '#', BlocksItems.CIRCUIT_COMPLEX,
                'C', BlocksItems.GLOWTRONIC_CRYSTAL);

        Recipes.advRecipes.addRecipe(new ItemStack(BlocksItems.PLASMA_CRYSTAL),
                "G#G", "GCG", "G#G",
                'G', Items.getItem("carbonPlate"),
                '#', Items.getItem("iridiumPlate"),
                'C', BlocksItems.UNIVERSAL_CRYSTAL);

        Recipes.advRecipes.addRecipe(AdvEnergyBlocks.ESU.STACK,
                "C#C", "CSC", "CMC",
                'C', BlocksItems.GLOWTRONIC_CRYSTAL,
                '#', BlocksItems.CIRCUIT_COMPLEX,
                'S', Items.getItem("mfsUnit"),
                'M', Items.getItem("advancedMachine"));

        Recipes.advRecipes.addRecipe(AdvEnergyBlocks.ISU.STACK,
                "C#C", "CSC", "CMC",
                'C', BlocksItems.UNIVERSAL_CRYSTAL,
                '#', BlocksItems.CIRCUIT_COMPLEX,
                'S', AdvEnergyBlocks.ESU.STACK,
                'M', Items.getItem("advancedMachine"));

        Recipes.advRecipes.addRecipe(AdvEnergyBlocks.PESU.STACK,
                "C#C", "CSC", "CMC",
                'C', BlocksItems.PLASMA_CRYSTAL,
                '#', BlocksItems.IRIDIUM_CORE,
                'S', AdvEnergyBlocks.ISU.STACK,
                'M', Items.getItem("advancedMachine"));

        Recipes.advRecipes.addRecipe(AdvEnergyBlocks.TRANSFORMER_EV.STACK,
                " C ", "#TX", " C ",
                'C', Items.getItem("glassFiberCableItem"),
                '#', Items.getItem("advancedCircuit"),
                'T', Items.getItem("hvTransformer"),
                'X', Items.getItem("lapotronCrystal"));

        Recipes.advRecipes.addRecipe(AdvEnergyBlocks.TRANSFORMER_IV.STACK,
                " C ", "#TX", " C ",
                'C', Items.getItem("trippleInsulatedIronCableItem"),
                '#', BlocksItems.CIRCUIT_COMPLEX,
                'T', AdvEnergyBlocks.TRANSFORMER_EV.STACK,
                'X', BlocksItems.GLOWTRONIC_CRYSTAL);
    }
}
