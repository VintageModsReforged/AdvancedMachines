package ic2.advancedmachines.blocks.tiles.machines;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.AdvancedMachinesConfig;
import ic2.advancedmachines.blocks.gui.GuiAdvExtractor;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.advancedmachines.utils.InvSlotFiltered;
import ic2.advancedmachines.utils.StackFilters;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.slot.SlotInvSlot;
import mods.vintage.core.platform.lang.Translator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class TileEntityCentrifugeExtractor extends TileEntityAdvancedMachine {
    public TileEntityCentrifugeExtractor() {
        super(Translator.format("block.advanced.extractor.name"), 5, StackFilters.EXTRACTOR_FILTER);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiScreen getGui(EntityPlayer player, boolean b) {
        return new GuiAdvExtractor(player.inventory, this);
    }

    @Override
    public void addSlots() {
        // inputs
        this.inputs.add(new InvSlotFiltered(this, "inputA", 1, 1, this.inputFilter));
        // outputs
        this.outputs.add(new InvSlotOutput(this, "outputA", 2, 1));
        this.outputs.add(new InvSlotOutput(this, "outputB", 3, 1));
        this.outputs.add(new InvSlotOutput(this, "outputC", 4, 1));
    }

    @Override
    public List<SlotInvSlot> getSlots() {
        List<SlotInvSlot> slots = new ArrayList<SlotInvSlot>();
        slots.add(new SlotInvSlot(this.inputs.get(0), 0, 56, 17));

        slots.add(new SlotInvSlot(this.outputs.get(0), 0, 115, 17));
        slots.add(new SlotInvSlot(this.outputs.get(1), 0, 115, 35));
        slots.add(new SlotInvSlot(this.outputs.get(2), 0, 115, 53));

        return slots;
    }

    @Override
    public String getStartSoundFile() {
        return AdvancedMachinesConfig.EXTRACTOR_WORK_SOUND;
    }

    @Override
    public String getSpeedName() {
        return Translator.format("inv.speed.extractor");
    }
}
