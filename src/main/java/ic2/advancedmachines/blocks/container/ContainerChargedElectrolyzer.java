package ic2.advancedmachines.blocks.container;

import ic2.advancedmachines.blocks.tiles.machines.TileEntityChargedElectrolyzer;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

import java.util.List;

public class ContainerChargedElectrolyzer extends ContainerFullInv {

    public TileEntityChargedElectrolyzer tile;
    public short energy = -1;

    public ContainerChargedElectrolyzer(EntityPlayer player, TileEntityChargedElectrolyzer tile) {
        super(player, tile, 166);
        this.tile = tile;
        List<SlotInvSlot> slots = tile.getSlots();
        for (SlotInvSlot slot : slots) {
            this.addSlotToContainer(slot);
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (Object crafter : this.crafters) {
            ICrafting icrafting = (ICrafting) crafter;
            if (this.tile.energy != this.energy) {
                icrafting.sendProgressBarUpdate(this, 0, this.tile.energy);
            }
        }

        this.energy = this.tile.energy;
    }

    @Override
    public void updateProgressBar(int index, int value) {
        if (index == 0) {
            this.tile.energy = (short) value;
        }
    }
}
