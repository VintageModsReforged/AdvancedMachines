package ic2.advancedmachines.blocks.tiles.container;

import ic2.advancedmachines.blocks.tiles.TileEntityChargedElectrolyzer;
import ic2.advancedmachines.utils.AdvSlot;
import ic2.advancedmachines.utils.StackFilters;
import ic2.core.ContainerIC2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;

public class ContainerChargedElectrolyzer extends ContainerIC2 {

    public TileEntityChargedElectrolyzer TILE;
    public short energy = -1;

    public ContainerChargedElectrolyzer(InventoryPlayer playerInv, TileEntityChargedElectrolyzer tile) {
        this.TILE = tile;
        // player inventory
        this.addSlotToContainer(AdvSlot.filtered(tile, 0, 53, 35, StackFilters.ELECTROLYZER_IN));
        this.addSlotToContainer(AdvSlot.filtered(tile, 1, 112, 35, StackFilters.ELECTROLYZER_OUT));
        int i;
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (Object crafter : this.crafters) {
            ICrafting icrafting = (ICrafting) crafter;
            if (this.energy != this.TILE.energy) {
                icrafting.sendProgressBarUpdate(this, 0, this.TILE.energy);
            }
        }
        this.energy = this.TILE.energy;
    }

    @Override
    public void updateProgressBar(int i, int j) {
        if (i == 0) {
            this.TILE.energy = (short) j;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.TILE.isUseableByPlayer(player);
    }

    @Override
    public int guiInventorySize() {
        return 2;
    }

    @Override
    public int getInput() {
        return 0;
    }
}
