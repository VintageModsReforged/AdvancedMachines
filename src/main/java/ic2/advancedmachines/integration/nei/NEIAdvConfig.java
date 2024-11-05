package ic2.advancedmachines.integration.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import ic2.advancedmachines.AdvancedMachines;
import ic2.advancedmachines.integration.nei.providers.*;
import ic2.advancedmachines.integration.nei.providers.furnaces.NEIAdvInductionFurnaceProvider;
import ic2.advancedmachines.integration.nei.providers.furnaces.NEIElectricFurnaceProvider;
import ic2.advancedmachines.integration.nei.providers.furnaces.NEIInductionFurnaceProvider;
import ic2.advancedmachines.integration.nei.providers.furnaces.NEIIronFurnaceProvider;
import ic2.advancedmachines.utils.Refs;
import ic2.core.gui.*;

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
        API.registerGuiOverlay(GuiAdvElectrolyzer.class, "electrolyzer", 0, 0);
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
