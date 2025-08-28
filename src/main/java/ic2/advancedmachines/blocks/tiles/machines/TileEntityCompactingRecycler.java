package ic2.advancedmachines.blocks.tiles.machines;

import ic2.advancedmachines.AdvancedMachinesConfig;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.advancedmachines.utils.AdvSlot;
import ic2.advancedmachines.utils.StackFilters;
import ic2.api.Items;
import ic2.core.block.machine.tileentity.TileEntityRecycler;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

public class TileEntityCompactingRecycler extends TileEntityAdvancedMachine {

    public TileEntityCompactingRecycler() {
        super(StatCollector.translateToLocal("block.advanced.recycler.name"), new int[]{1}, new int[]{2}, StackFilters.RECYCLER_FILTER, "Recycler");
    }

    @Override
    public List<Slot> getSlots(InventoryPlayer playerInv) {
        List<Slot> slots = new ArrayList<Slot>();
        slots.add(AdvSlot.filtered(this, 1, 56, 17));
        slots.add(new SlotFurnace(playerInv.player, this, 2, 115, 35));
        return slots;
    }

    @Override
    protected void operate() {
        if (canOperate()) {
            int inputIndex = this.inputs[0];
            int outputIndex = this.outputs[0];
            boolean blackListed = TileEntityRecycler.getIsItemBlacklisted(this.inventory[inputIndex]);
            --this.inventory[inputIndex].stackSize;
            if (this.inventory[inputIndex].stackSize <= 0) {
                this.inventory[inputIndex] = null;
            }

            if (this.worldObj.rand.nextInt(8) == 0 && !blackListed) {
                if (this.inventory[outputIndex] == null) {
                    this.inventory[outputIndex] = Items.getItem("scrap").copy();
                } else ++this.inventory[outputIndex].stackSize;
            }
        }
    }

    @Override
    protected boolean canOperate() {
        int inputIndex = this.inputs[0];
        int outputIndex = this.outputs[0];
        ItemStack scrap = Items.getItem("scrap");
        if (this.inventory[inputIndex] == null) {
            return false;
        } else return this.inventory[outputIndex] == null || this.inventory[outputIndex].isItemEqual(scrap) && this.inventory[outputIndex].stackSize < scrap.getMaxStackSize();
    }

    @Override
    public int[] getUpgradeSlots() {
        return new int[] { 3, 4 };
    }

    @Override
    public String getStartSoundFile() {
        return AdvancedMachinesConfig.RECYCLER_WORK_SOUND;
    }

    @Override
    public String getSpeedName() {
        return StatCollector.translateToLocal("inv.speed.recycler");
    }

    @Override
    public ItemStack getResultFor(ItemStack input, boolean adjustOutput) {
        return null;
    }
}
