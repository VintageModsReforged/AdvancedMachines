package ic2.advancedmachines.integration.nei;

import codechicken.nei.MultiItemRange;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import ic2.advancedmachines.AdvancedMachines;
import ic2.advancedmachines.BlocksItems;
import ic2.advancedmachines.blocks.AdvEnergyBlocks;
import ic2.advancedmachines.blocks.AdvMachines;
import ic2.advancedmachines.blocks.gui.GuiAdvCompressor;
import ic2.advancedmachines.blocks.gui.GuiAdvExtractor;
import ic2.advancedmachines.blocks.gui.GuiAdvMacerator;
import ic2.advancedmachines.blocks.gui.GuiAdvancedElectrolyzer;
import ic2.advancedmachines.integration.nei.providers.NEICompressorProvider;
import ic2.advancedmachines.integration.nei.providers.NEIElectrolyzerProvider;
import ic2.advancedmachines.integration.nei.providers.NEIExtractorProvider;
import ic2.advancedmachines.integration.nei.providers.NEIMaceratorProvider;
import ic2.advancedmachines.integration.nei.providers.furnaces.NEIAdvInductionFurnaceProvider;
import ic2.advancedmachines.integration.nei.providers.furnaces.NEIElectricFurnaceProvider;
import ic2.advancedmachines.integration.nei.providers.furnaces.NEIInductionFurnaceProvider;
import ic2.advancedmachines.integration.nei.providers.furnaces.NEIIronFurnaceProvider;
import ic2.advancedmachines.utils.Refs;
import net.minecraft.item.ItemStack;

public class NEIAdvConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {
        AdvancedMachines.LOGGER.info("Loading NEI Plugin for AdvancedMachines!");
        API.registerRecipeHandler(new NEIMaceratorProvider());
        API.registerUsageHandler(new NEIMaceratorProvider());
        API.registerRecipeHandler(new NEICompressorProvider());
        API.registerUsageHandler(new NEICompressorProvider());
        API.registerRecipeHandler(new NEIExtractorProvider());
        API.registerUsageHandler(new NEIExtractorProvider());

        API.registerRecipeHandler(new NEIIronFurnaceProvider());
        API.registerUsageHandler(new NEIIronFurnaceProvider());
        API.registerRecipeHandler(new NEIElectricFurnaceProvider());
        API.registerUsageHandler(new NEIElectricFurnaceProvider());
        API.registerRecipeHandler(new NEIInductionFurnaceProvider());
        API.registerUsageHandler(new NEIInductionFurnaceProvider());
        API.registerRecipeHandler(new NEIAdvInductionFurnaceProvider());
        API.registerUsageHandler(new NEIAdvInductionFurnaceProvider());

        API.registerRecipeHandler(new NEIElectrolyzerProvider());
        API.registerUsageHandler(new NEIElectrolyzerProvider());

        API.registerGuiOverlay(GuiAdvMacerator.class, "macerator", 5, 11);
        API.registerGuiOverlay(GuiAdvCompressor.class, "compressor", 5, 11);
        API.registerGuiOverlay(GuiAdvExtractor.class, "extractor", 5, 11);
        API.registerGuiOverlay(GuiAdvancedElectrolyzer.class, "electrolyzer", 0, 0);

        MultiItemRange neiCat = new MultiItemRange();
        for (AdvMachines machine : AdvMachines.VALUES) {
            neiCat.add(machine.STACK.itemID);
        }
        for (AdvEnergyBlocks energyBlock : AdvEnergyBlocks.VALUES) {
            neiCat.add(energyBlock.STACK.itemID);
        }
        neiCat.add(new ItemStack(BlocksItems.GLOWTRONIC_CRYSTAL).itemID);
        neiCat.add(new ItemStack(BlocksItems.UNIVERSAL_CRYSTAL).itemID);
        neiCat.add(new ItemStack(BlocksItems.PLASMA_CRYSTAL).itemID);
        neiCat.add(BlocksItems.REDSTONE_INVERTER.itemID);
        neiCat.add(BlocksItems.COBBLESTONE_GENERATOR.itemID);
        neiCat.add(BlocksItems.MAGNET_CHUNK.itemID);
        neiCat.add(BlocksItems.MAGNET_DEAD.itemID);
        neiCat.add(BlocksItems.MAGNET_COMPONENT.itemID);
        neiCat.add(BlocksItems.CIRCUIT_COMPLEX.itemID);
        neiCat.add(BlocksItems.IRIDIUM_CORE.itemID);
        API.addSetRange(Refs.ID, neiCat);
        AdvancedMachines.LOGGER.info("NEI Plugin for AdvancedMachines Loaded!");
    }

    @Override
    public String getName() {
        return Refs.NAME;
    }

    @Override
    public String getVersion() {
        return Refs.VERSION;
    }
}
