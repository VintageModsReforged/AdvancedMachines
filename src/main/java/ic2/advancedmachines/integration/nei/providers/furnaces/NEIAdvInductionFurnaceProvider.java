package ic2.advancedmachines.integration.nei.providers.furnaces;

import ic2.core.gui.GuiAdvInduction;

public class NEIAdvInductionFurnaceProvider extends NEIFurnaceProvider {

    @Override
    public Class getGuiClass() {
        return GuiAdvInduction.class;
    }
}
