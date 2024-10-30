package ic2.advancedmachines.blocks.tiles.container;

import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedEnergyBlock;
import ic2.advancedmachines.utils.SlotArmor;
import ic2.core.ContainerIC2;
import ic2.core.slot.SlotCharge;
import ic2.core.slot.SlotDischarge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;

public class ContainerAdvancedElectricBlock extends ContainerIC2 {

    public TileEntityAdvancedEnergyBlock TILE;
    public int energy = -1;

    public ContainerAdvancedElectricBlock(InventoryPlayer playerInv, TileEntityAdvancedEnergyBlock tile) {
        this.TILE = tile;
        this.addSlotToContainer(new SlotCharge(tile, tile.tier, 0, 56, 17));
        this.addSlotToContainer(new SlotDischarge(tile, tile.tier, 1, 56, 53));

        // player main inv
        int j;
        for(j = 0; j < 3; ++j) {
            for(int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(playerInv, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
            }
        }
        // player hotbar
        for(j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(playerInv, j, 8 + j * 18, 142));
        }
        // player armor inv
        for(j = 0; j < 4; ++j) {
            this.addSlotToContainer(new SlotArmor(playerInv, j, 8, 8 + j * 18));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (Object crafter : this.crafters) {
            ICrafting icrafting = (ICrafting) crafter;
            if (this.energy != this.TILE.energy) {
                icrafting.sendProgressBarUpdate(this, 0, this.TILE.energy & '\uffff');
                icrafting.sendProgressBarUpdate(this, 1, this.TILE.energy >>> 16);
            }
        }
        this.energy = this.TILE.energy;
    }

    @Override
    public void updateProgressBar(int i, int j) {
        switch (i) {
            case 0:
                this.TILE.energy = this.TILE.energy & -65536 | j;
                break;
            case 1:
                this.TILE.energy = this.TILE.energy & '\uffff' | j << 16;
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
