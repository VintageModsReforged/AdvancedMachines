package ic2.core.gui;

import ic2.advancedmachines.blocks.tiles.container.ContainerAdvancedMachine;
import ic2.advancedmachines.utils.Refs;

public class GuiAdvRecycler extends GuiAdvancedMachine {
    public GuiAdvRecycler(ContainerAdvancedMachine container) {
        super(container, Refs.getGuiPath("recycler"));
    }
}
