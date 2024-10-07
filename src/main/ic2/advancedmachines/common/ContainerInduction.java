package ic2.advancedmachines.common;

import ic2.advancedmachines.common.slot.SlotSmelter;
import ic2.advancedmachines.common.slot.SlotUpgrade;
import ic2.api.item.IElectricItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class ContainerInduction extends Container {

    public TileAdvancedInduction tile;

    public int progress = 0;
    public int energy = 0;
    public int heat = 0;

    public ContainerInduction(InventoryPlayer playerInv, TileAdvancedInduction tile) {
//        ic2.core.block.machine.ContainerInduction.class;
        this.tile = tile;
        this.addSlotToContainer(new Slot(tile, 0, 56, 53)); // energy
        this.addSlotToContainer(new SlotSmelter(tile, tile.inputAIndex, 47, 17)); // input 1
        this.addSlotToContainer(new SlotSmelter(tile, tile.inputBIndex, 63, 17)); // input 2
        this.addSlotToContainer(new SlotFurnace(playerInv.player, tile, tile.outputAIndex, 113, 35)); // output
        this.addSlotToContainer(new SlotFurnace(playerInv.player, tile, tile.outputBIndex, 131, 35)); // output

        this.addSlotToContainer(new SlotUpgrade(tile, 5, 152, 6));
        this.addSlotToContainer(new SlotUpgrade(tile, 6, 152, 24));
        this.addSlotToContainer(new SlotUpgrade(tile, 7, 152, 42));
        this.addSlotToContainer(new SlotUpgrade(tile, 8, 152, 60));

        int var3;
        for (var3 = 0; var3 < 3; ++var3) {
            for (int var4 = 0; var4 < 9; ++var4) {
                this.addSlotToContainer(new Slot(playerInv, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3) {
            this.addSlotToContainer(new Slot(playerInv, var3, 8 + var3 * 18, 142));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int var1 = 0; var1 < this.crafters.size(); ++var1) {
            ICrafting var2 = (ICrafting) this.crafters.get(var1);
            if (this.tile.progress != this.progress) {
                var2.sendProgressBarUpdate(this, 0, this.tile.progress);
            }

            if (this.tile.energy != this.energy) {
                var2.sendProgressBarUpdate(this, 1, this.tile.energy & '\uffff');
                var2.sendProgressBarUpdate(this, 2, this.tile.energy >>> 16);
            }

            if (this.tile.speed != this.heat) {
                var2.sendProgressBarUpdate(this, 3, this.tile.speed);
            }
        }

        this.progress = this.tile.progress;
        this.energy = this.tile.energy;
        this.heat = this.tile.speed;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slot) {
        ItemStack tempStack = null;
        Slot localslot = (Slot) this.inventorySlots.get(slot);
        if (localslot != null && localslot.getHasStack()) {
            ItemStack localstack = localslot.getStack();
            tempStack = localstack.copy();
            if (slot < 9) {
                this.mergeItemStack(localstack, 9, 38, false);
            } else {
                if (localstack.itemID == AdvancedMachines.overClockerStack.itemID
                        || localstack.itemID == AdvancedMachines.transformerStack.itemID
                        || localstack.itemID == AdvancedMachines.energyStorageUpgradeStack.itemID) {
                    this.mergeItemStack(localstack, 5, 6, false);
                } else if (localstack.getItem() instanceof IElectricItem) {
                    if (((Slot) inventorySlots.get(0)).getStack() == null) {
                        ((Slot) inventorySlots.get(0)).putStack(localstack);
                        localslot.putStack(null);
                    }
                } else if (FurnaceRecipes.smelting().getSmeltingResult(localstack) != null) {
                    this.mergeItemStack(localstack, 1, 3, false);
                }
            }

            if (localstack.stackSize == 0) {
                localslot.putStack(null);
            } else {
                localslot.onSlotChanged();
            }

            if (localstack.stackSize == tempStack.stackSize) {
                return null;
            }

            localslot.putStack(localstack);
        }

        return tempStack;
    }

    @Override
    public void updateProgressBar(int var1, int var2) {
        switch (var1) {
            case 0:
                this.tile.progress = (short) var2;
                break;
            case 1:
                this.tile.energy = this.tile.energy & -65536 | var2;
                break;
            case 2:
                this.tile.energy = this.tile.energy & '\uffff' | var2 << 16;
                break;
            case 3:
                this.tile.speed = (short) var2;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return this.tile.isUseableByPlayer(entityPlayer);
    }
}
