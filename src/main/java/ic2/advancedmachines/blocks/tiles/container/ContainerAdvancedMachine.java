package ic2.advancedmachines.blocks.tiles.container;

import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.advancedmachines.utils.AdvSlot;
import ic2.advancedmachines.utils.StackFilters;
import ic2.core.ContainerIC2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;

import java.util.List;

public class ContainerAdvancedMachine extends ContainerIC2 {

    public TileEntityAdvancedMachine TILE;
    public short progress = -1;
    public int energy = -1;
    public short speed = -1;

    public ContainerAdvancedMachine(InventoryPlayer playerInv, TileEntityAdvancedMachine tile) {
        this.TILE = tile;
        this.addSlotToContainer(AdvSlot.filtered(tile, 0, 56, 53, StackFilters.BATTERY_FILTER)); // energy
        int[] upgradeSlots = tile.getUpgradeSlots();
        for (int i = 0; i < upgradeSlots.length; i++) { // add upgrade slots
            this.addSlotToContainer(AdvSlot.filtered(tile, upgradeSlots[i], 152, 6 + 18 * i, StackFilters.UPGRADE_FILTER));
        }
        List<Slot> slots = tile.getSlots(playerInv);
        for (Slot slot: slots) { // main slots
            this.addSlotToContainer(slot);
        }
        // player inventory
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

        for(int i = 0; i < this.crafters.size(); ++i) {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);
            if (this.progress != this.TILE.progress) {
                icrafting.sendProgressBarUpdate(this, 0, this.TILE.progress);
            }

            if (this.energy != this.TILE.energy) {
                icrafting.sendProgressBarUpdate(this, 1, this.TILE.energy & '\uffff');
                icrafting.sendProgressBarUpdate(this, 2, this.TILE.energy >>> 16);
            }

            if (this.speed != this.TILE.speed) {
                icrafting.sendProgressBarUpdate(this, 3, this.TILE.speed);
            }
        }

        this.progress = this.TILE.progress;
        this.energy = this.TILE.energy;
        this.speed = this.TILE.speed;
    }

    @Override
    public int guiInventorySize() {
        return this.TILE.getSizeInventory();
    }

    @Override
    public int getInput() {
        if (this.TILE.inputs.length > 1) {
            return this.TILE.getStackInSlot(this.TILE.inputs[1]) != null ? this.TILE.inputs[1] : this.TILE.inputs[0];
        } else {
            return this.TILE.inputs[0];
        }
    }

    @Override
    public void updateProgressBar(int i, int j) {
        switch (i) {
            case 0:
                this.TILE.progress = (short)j;
                break;
            case 1:
                this.TILE.energy = this.TILE.energy & -65536 | j;
                break;
            case 2:
                this.TILE.energy = this.TILE.energy & '\uffff' | j << 16;
                break;
            case 3:
                this.TILE.speed = (short) j;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return this.TILE.isUseableByPlayer(entityPlayer);
    }
}
