package ic2.advancedmachines.integration.nei.providers.furnaces;

import ic2.core.block.machine.gui.GuiElecFurnace;

public class NEIElectricFurnaceProvider extends NEIFurnaceProvider {

    @Override
    public Class getGuiClass() {
        return GuiElecFurnace.class;
    }
}
