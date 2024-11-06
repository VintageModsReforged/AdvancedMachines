package ic2.advancedmachines.blocks.gui;

import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiAdvMacerator extends GuiAdvancedMachine {
    public GuiAdvMacerator(InventoryPlayer playerInv, TileEntityAdvancedMachine tile) {
        super(playerInv, tile, "macerator");
    }
}
