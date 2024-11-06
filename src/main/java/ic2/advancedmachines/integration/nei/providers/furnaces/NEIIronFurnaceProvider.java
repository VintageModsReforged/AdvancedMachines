package ic2.advancedmachines.integration.nei.providers.furnaces;

import ic2.core.block.machine.gui.GuiIronFurnace;

public class NEIIronFurnaceProvider extends NEIFurnaceProvider {

    @Override
    public Class getGuiClass() {
        return GuiIronFurnace.class;
    }
}
