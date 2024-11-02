package ic2.core.gui;

import ic2.advancedmachines.blocks.tiles.container.ContainerAdvancedMachine;
import ic2.advancedmachines.utils.Refs;

public class GuiAdvCompressor extends GuiAdvancedMachine {
    public GuiAdvCompressor(ContainerAdvancedMachine container) {
        super(container, Refs.getGuiPath("compressor"));
    }
}
