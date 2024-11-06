package ic2.advancedmachines.integration.nei.providers.furnaces;

import ic2.core.block.machine.gui.GuiInduction;

public class NEIInductionFurnaceProvider extends NEIFurnaceProvider {

    @Override
    public Class getGuiClass() {
        return GuiInduction.class;
    }
}
