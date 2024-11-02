package ic2.core.gui;

import ic2.advancedmachines.blocks.tiles.container.ContainerAdvancedMachine;
import ic2.advancedmachines.utils.Refs;

public class GuiAdvInduction extends GuiAdvancedMachine {
    public GuiAdvInduction(ContainerAdvancedMachine container) {
        super(container, Refs.getGuiPath("induction"));
    }
}
