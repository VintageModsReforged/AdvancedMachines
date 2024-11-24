package ic2.advancedmachines.integration.nei;

import codechicken.nei.MultiItemRange;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import ic2.advancedmachines.AdvancedMachines;
import ic2.advancedmachines.BlocksItems;
import ic2.advancedmachines.blocks.AdvEnergyBlocks;
import ic2.advancedmachines.blocks.AdvMachines;
import ic2.advancedmachines.integration.nei.providers.*;
import ic2.advancedmachines.integration.nei.providers.furnaces.NEIAdvInductionFurnaceProvider;
import ic2.advancedmachines.integration.nei.providers.furnaces.NEIElectricFurnaceProvider;
import ic2.advancedmachines.integration.nei.providers.furnaces.NEIInductionFurnaceProvider;
import ic2.advancedmachines.integration.nei.providers.furnaces.NEIIronFurnaceProvider;
import ic2.advancedmachines.utils.Refs;
import ic2.core.gui.*;
import net.minecraft.item.ItemStack;

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

        MultiItemRange neiCat = new MultiItemRange();
        for (AdvMachines machine : AdvMachines.VALUES) {
            neiCat.add(machine.STACK);
        }
        for (AdvEnergyBlocks energyBlock : AdvEnergyBlocks.VALUES) {
            neiCat.add(energyBlock.STACK);
        }
        neiCat.add(new ItemStack(BlocksItems.GLOWTRONIC_CRYSTAL));
        neiCat.add(new ItemStack(BlocksItems.UNIVERSAL_CRYSTAL));
        neiCat.add(new ItemStack(BlocksItems.PLASMA_CRYSTAL));
        neiCat.add(BlocksItems.REDSTONE_INVERTER);
        neiCat.add(BlocksItems.COBBLESTONE_GENERATOR);
        neiCat.add(BlocksItems.MAGNET_CHUNK);
        neiCat.add(BlocksItems.MAGNET_DEAD);
        neiCat.add(BlocksItems.MAGNET_COMPONENT);
        neiCat.add(BlocksItems.CIRCUIT_COMPLEX);
        neiCat.add(BlocksItems.IRIDIUM_CORE);
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
