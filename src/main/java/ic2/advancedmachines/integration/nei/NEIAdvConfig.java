package ic2.advancedmachines.integration.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import ic2.advancedmachines.AdvancedMachines;
import ic2.advancedmachines.integration.nei.providers.NEICompressorProvider;
import ic2.advancedmachines.integration.nei.providers.NEIExtractorProvider;
import ic2.advancedmachines.integration.nei.providers.NEIMaceratorProvider;
import ic2.advancedmachines.utils.Refs;
import ic2.core.gui.GuiAdvCompressor;
import ic2.core.gui.GuiAdvExtractor;
import ic2.core.gui.GuiAdvInduction;
import ic2.core.gui.GuiAdvMacerator;

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

        API.registerGuiOverlay(GuiAdvMacerator.class, "macerator", 5, 11);
        API.registerGuiOverlay(GuiAdvCompressor.class, "compressor", 5, 11);
        API.registerGuiOverlay(GuiAdvExtractor.class, "extractor", 5, 11);
        API.registerGuiOverlay(GuiAdvInduction.class, "smelting", -4, 11);
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
