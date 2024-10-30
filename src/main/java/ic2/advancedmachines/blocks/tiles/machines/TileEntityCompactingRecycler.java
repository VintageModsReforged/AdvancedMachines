package ic2.advancedmachines.blocks.tiles.machines;

import ic2.advancedmachines.AdvancedMachinesConfig;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.advancedmachines.utils.AdvSlot;
import ic2.advancedmachines.utils.LangHelper;
import ic2.advancedmachines.utils.StackFilters;
import ic2.api.Items;
import ic2.core.block.machine.tileentity.TileEntityRecycler;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TileEntityCompactingRecycler extends TileEntityAdvancedMachine {

    public TileEntityCompactingRecycler() {
        super(LangHelper.format("block.advanced.recycler.name"), new int[]{1}, new int[]{1}, StackFilters.RECYCLER_FILTER);
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
            boolean blackListed = TileEntityRecycler.getIsItemBlacklisted(this.inventory[1]);
            --this.inventory[1].stackSize;
            if (this.inventory[1].stackSize <= 0) {
                this.inventory[1] = null;
            }

            if (this.worldObj.rand.nextInt(8) == 0 && !blackListed) {
                if (this.inventory[2] == null) {
                    this.inventory[2] = Items.getItem("scrap").copy();
                } else ++this.inventory[2].stackSize;
            }
        }
    }

    @Override
    protected boolean canOperate() {
        ItemStack scrap = Items.getItem("scrap");
        if (this.inventory[1] == null) {
            return false;
        } else return this.inventory[2] == null || this.inventory[2].isItemEqual(scrap) && this.inventory[2].stackSize < scrap.getMaxStackSize();
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
        return LangHelper.format("inv.speed.recycler");
    }

    @Override
    public ItemStack getResultFor(ItemStack input, boolean adjustOutput) {
        return null;
    }
}
