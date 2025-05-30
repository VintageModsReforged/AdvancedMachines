package ic2.advancedmachines.blocks.tiles.machines;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.AdvancedMachinesConfig;
import ic2.advancedmachines.blocks.gui.GuiAdvRecycler;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.advancedmachines.utils.InvSlotFiltered;
import ic2.advancedmachines.utils.StackFilters;
import ic2.api.item.Items;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotProcessable;
import ic2.core.block.machine.tileentity.TileEntityRecycler;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

public class TileEntityCompactingRecycler extends TileEntityAdvancedMachine {
    public TileEntityCompactingRecycler() {
        super(StatCollector.translateToLocal("block.advanced.recycler.name"), 3, StackFilters.RECYCLER_FILTER);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiScreen getGui(EntityPlayer player, boolean b) {
        return new GuiAdvRecycler(player.inventory, this);
    }

    @Override
    public void addSlots() {
        // inputs
        this.inputs.add(new InvSlotFiltered(this, "inputA", 1, 1, StackFilters.RECYCLER_FILTER));
        // outputs
        this.outputs.add(new InvSlotOutput(this, "outputA", 2, 1));
    }

    @Override
    public List<SlotInvSlot> getSlots() {
        List<SlotInvSlot> slots = new ArrayList<SlotInvSlot>();
        slots.add(new SlotInvSlot(this.inputs.get(0), 0, 56, 17));
        slots.add(new SlotInvSlot(this.outputs.get(0), 0, 115, 35));
        return slots;
    }

    @Override
    protected void operate() {
        if (canOperate()) {
            InvSlotProcessable input = this.inputs.get(0);
            InvSlotOutput output = this.outputs.get(0);
            ItemStack scrap = Items.getItem("scrap");
            boolean blackListed = TileEntityRecycler.getIsItemBlacklisted(input.get());
            --input.get().stackSize;
            if (input.get().stackSize <= 0) input.clear();

            if (this.worldObj.rand.nextInt(8) == 0 && !blackListed) {
                if (output.isEmpty()) {
                    output.put(scrap.copy());
                } else ++output.get().stackSize;
            }
        }
    }

    @Override
    public boolean canOperate() {
        InvSlotProcessable input = this.inputs.get(0);
        InvSlotOutput output = this.outputs.get(0);
        ItemStack scrap = Items.getItem("scrap");
        if (input.isEmpty()) {
            return false;
        } else return output.isEmpty() || output.get().isItemEqual(scrap) && output.get().stackSize < scrap.getMaxStackSize();
    }

    @Override
    public String getStartSoundFile() {
        return AdvancedMachinesConfig.RECYCLER_WORK_SOUND;
    }

    @Override
    public String getSpeedName() {
        return StatCollector.translateToLocal("inv.speed.recycler");
    }
}
