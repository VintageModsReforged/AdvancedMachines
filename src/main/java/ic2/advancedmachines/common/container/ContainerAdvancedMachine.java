package ic2.advancedmachines.common.container;

import ic2.advancedmachines.common.slot.SlotFiltered;
import ic2.advancedmachines.common.tiles.base.TileEntityAdvancedMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ContainerAdvancedMachine extends Container {

    TileEntityAdvancedMachine TILE;
    public int PROGRESS = 0;
    public int ENERGY = 0;
    public int SPEED = 0;

    public ContainerAdvancedMachine(InventoryPlayer playerInv, TileEntityAdvancedMachine tile) {
        this.TILE = tile;
        this.addSlotToContainer(SlotFiltered.batterySlot(tile, 0, 56, 53)); // energy
        int[] upgradeSlots = tile.getUpgradeSlots();
        for (int i = 0; i < upgradeSlots.length; i++) { // add upgrade slots
            this.addSlotToContainer(SlotFiltered.upgradeSlot(tile, upgradeSlots[i], 152, 6 + 18 * i));
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
            if (this.PROGRESS != this.TILE.progress) {
                icrafting.sendProgressBarUpdate(this, 0, this.TILE.progress);
            }

            if (this.ENERGY != this.TILE.energy) {
                icrafting.sendProgressBarUpdate(this, 1, this.TILE.energy & '\uffff');
                icrafting.sendProgressBarUpdate(this, 2, this.TILE.energy >>> 16);
            }

            if (this.SPEED != this.TILE.speed) {
                icrafting.sendProgressBarUpdate(this, 3, this.TILE.speed);
            }
        }

        this.PROGRESS = this.TILE.progress;
        this.ENERGY = this.TILE.energy;
        this.SPEED = this.TILE.speed;
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
                this.TILE.speed = (short)j;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return this.TILE.isUseableByPlayer(entityPlayer);
    }

    @Override
    public final ItemStack transferStackInSlot(EntityPlayer player, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i < this.TILE.getSizeInventory()) {
                this.mergeItemStack(itemstack1, this.TILE.getSizeInventory(), this.inventorySlots.size(), false);
            } else if (i >= this.TILE.getSizeInventory() && i < this.inventorySlots.size()) {
                for(int j = 0; j < this.TILE.getSizeInventory(); ++j) {
                    Slot slot2 = this.getSlot(j);
                    if (slot2 != null && slot2.isItemValid(itemstack1) && this.mergeItemStack(itemstack1, j, j + 1, false)) {
                        break;
                    }
                }
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }
            slot.onPickupFromSlot(player, itemstack1);
        }
        return itemstack;
    }
}
