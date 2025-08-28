package ic2.advancedmachines.integration.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import ic2.advancedmachines.AdvancedMachines;
import ic2.advancedmachines.integration.nei.providers.NEICompressorProvider;
import ic2.advancedmachines.integration.nei.providers.NEIElectrolyzerProvider;
import ic2.advancedmachines.integration.nei.providers.NEIExtractorProvider;
import ic2.advancedmachines.integration.nei.providers.NEIMaceratorProvider;
import ic2.advancedmachines.integration.nei.providers.furnaces.NEIAdvInductionFurnaceProvider;
import ic2.advancedmachines.integration.nei.providers.furnaces.NEIElectricFurnaceProvider;
import ic2.advancedmachines.integration.nei.providers.furnaces.NEIInductionFurnaceProvider;
import ic2.advancedmachines.integration.nei.providers.furnaces.NEIIronFurnaceProvider;
import ic2.advancedmachines.utils.Refs;
import ic2.core.gui.GuiAdvCompressor;
import ic2.core.gui.GuiAdvElectrolyzer;
import ic2.core.gui.GuiAdvExtractor;
import ic2.core.gui.GuiAdvMacerator;

public class NEIAdvConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {
        AdvancedMachines.LOGGER.info("Loading NEI Plugin for AdvancedMachines!");
        API.registerUsageHandler(new NEIMaceratorProvider());
        API.registerUsageHandler(new NEICompressorProvider());
        API.registerUsageHandler(new NEIExtractorProvider());

        API.registerUsageHandler(new NEIIronFurnaceProvider());
        API.registerUsageHandler(new NEIElectricFurnaceProvider());
        API.registerUsageHandler(new NEIInductionFurnaceProvider());
        API.registerUsageHandler(new NEIAdvInductionFurnaceProvider());

        API.registerUsageHandler(new NEIElectrolyzerProvider());

        API.registerGuiOverlay(GuiAdvMacerator.class, "macerator", 5, 11);
        API.registerGuiOverlay(GuiAdvCompressor.class, "compressor", 5, 11);
        API.registerGuiOverlay(GuiAdvExtractor.class, "extractor", 5, 11);
        API.registerGuiOverlay(GuiAdvElectrolyzer.class, "electrolyzer", 0, 0);
        AdvancedMachines.LOGGER.info("NEI Plugin for AdvancedMachines Loaded!");
    }

    @Override
    public String getName() {
        return Refs.NAME;
    }

    @Override
    public String getVersion() {
        return "1.4.7-1.0.0";
    }
}
