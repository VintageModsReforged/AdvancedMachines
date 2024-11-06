package ic2.advancedmachines.integration.nei.providers.furnaces;

import ic2.advancedmachines.blocks.gui.GuiAdvInduction;

public class NEIAdvInductionFurnaceProvider extends NEIFurnaceProvider {

    @Override
    public Class getGuiClass() {
        return GuiAdvInduction.class;
    }
}
