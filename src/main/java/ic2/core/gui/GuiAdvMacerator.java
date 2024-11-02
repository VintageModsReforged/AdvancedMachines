package ic2.core.gui;

import ic2.advancedmachines.blocks.tiles.container.ContainerAdvancedMachine;
import ic2.advancedmachines.utils.Refs;

public class GuiAdvMacerator extends GuiAdvancedMachine {
    public GuiAdvMacerator(ContainerAdvancedMachine container) {
        super(container, Refs.getGuiPath("macerator"));
    }
}
