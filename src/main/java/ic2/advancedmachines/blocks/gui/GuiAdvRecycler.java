package ic2.advancedmachines.blocks.gui;

import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiAdvRecycler extends GuiAdvancedMachine {
    public GuiAdvRecycler(InventoryPlayer playerInv, TileEntityAdvancedMachine tile) {
        super(playerInv, tile, "recycler");
    }
}
