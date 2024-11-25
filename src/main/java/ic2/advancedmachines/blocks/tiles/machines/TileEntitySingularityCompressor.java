package ic2.advancedmachines.blocks.tiles.machines;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.AdvancedMachinesConfig;
import ic2.advancedmachines.blocks.gui.GuiAdvCompressor;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.advancedmachines.utils.InvSlotFiltered;
import ic2.advancedmachines.utils.StackFilters;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.slot.SlotInvSlot;
import core.helpers.LangHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class TileEntitySingularityCompressor extends TileEntityAdvancedMachine {
    public TileEntitySingularityCompressor() {
        super(LangHelper.format("block.advanced.compressor.name"), 3, StackFilters.COMPRESSOR_FILTER);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiScreen getGui(EntityPlayer player, boolean b) {
        return new GuiAdvCompressor(player.inventory, this);
    }

    @Override
    public void addSlots() {
        // inputs
        this.inputs.add(new InvSlotFiltered(this, "inputA", 1, 1, this.inputFilter));
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
    public String getStartSoundFile() {
        return AdvancedMachinesConfig.COMPRESSOR_WORK_SOUND;
    }

    @Override
    public String getSpeedName() {
        return LangHelper.format("inv.speed.compressor");
    }
}
