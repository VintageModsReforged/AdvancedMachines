package ic2.advancedmachines.blocks.container;

import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.core.block.machine.ContainerElectricMachine;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

import java.util.List;

public class ContainerAdvancedMachine extends ContainerElectricMachine {

    TileEntityAdvancedMachine TILE;
    private short lastProgress = -1;
    private short lastSpeed = -1;

    public ContainerAdvancedMachine(EntityPlayer entityPlayer, TileEntityAdvancedMachine tile) {
        super(entityPlayer, tile, 166, 56, 53);
        this.TILE = tile;
        List<SlotInvSlot> slots = tile.getSlots();
        for (SlotInvSlot slot : slots) {
            this.addSlotToContainer(slot);
        }
        for (int i = 0; i < 2; ++i) { // add upgrade slots
            this.addSlotToContainer(new SlotInvSlot(tile.upgradeSlot, i, 152, 8 + 18 * i));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (Object crafter : this.crafters) {
            ICrafting icrafting = (ICrafting) crafter;
            if (this.TILE.progress != this.lastProgress) {
                icrafting.sendProgressBarUpdate(this, 2, this.TILE.progress);
            }

            if (this.TILE.speed != this.lastSpeed) {
                icrafting.sendProgressBarUpdate(this, 3, this.TILE.speed);
            }
        }

        this.lastProgress = this.TILE.progress;
        this.lastSpeed = this.TILE.speed;
    }

    @Override
    public void updateProgressBar(int index, int value) {
        super.updateProgressBar(index, value);
        switch (index) {
            case 2:
                this.TILE.progress = (short) value;
                break;
            case 3:
                this.TILE.speed = (short) value;
        }
    }
}
