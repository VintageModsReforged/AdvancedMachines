package ic2.advancedmachines.blocks.tiles;

import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.advancedmachines.AdvancedMachinesConfig;
import ic2.advancedmachines.utils.LangHelper;
import ic2.api.recipe.Recipes;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotProcessableGeneric;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TileEntitySingularityCompressor extends TileEntityAdvancedMachine {
    public TileEntitySingularityCompressor() {
        super(LangHelper.format("block.advanced.compressor.name"), 3);
    }

    @Override
    public void addSlots() {
        // inputs
        this.inputs.add(new InvSlotProcessableGeneric(this, "inputA", 1, 1, Recipes.compressor));
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
    public ItemStack getResultFor(ItemStack input, boolean adjustOutput) {
        return (ItemStack) Recipes.compressor.getOutputFor(input, adjustOutput);
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
