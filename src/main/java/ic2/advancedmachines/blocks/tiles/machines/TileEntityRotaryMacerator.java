package ic2.advancedmachines.blocks.tiles.machines;

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

public class TileEntityRotaryMacerator extends TileEntityAdvancedMachine {

    public TileEntityRotaryMacerator() {
        super(LangHelper.format("block.advanced.macerator.name"), 4, Recipes.macerator);
    }

    @Override
    public void addSlots() {
        // inputs
        this.inputs.add(new InvSlotProcessableGeneric(this, "inputA", 1, 1, this.recipeFilter));
        // outputs
        this.outputs.add(new InvSlotOutput(this, "outputA", 2, 1));
        this.outputs.add(new InvSlotOutput(this, "outputB", 3, 1));
    }

    @Override
    public List<SlotInvSlot> getSlots() {
        List<SlotInvSlot> slots = new ArrayList<SlotInvSlot>();
        slots.add(new SlotInvSlot(this.inputs.get(0), 0, 56, 17));

        slots.add(new SlotInvSlot(this.outputs.get(0), 0, 115, 25));
        slots.add(new SlotInvSlot(this.outputs.get(1), 0, 115, 46));

        return slots;
    }

    @Override
    public ItemStack getResultFor(ItemStack macerated, boolean adjustOutput) {
        return (ItemStack) Recipes.macerator.getOutputFor(macerated, adjustOutput);
    }

    @Override
    public String getStartSoundFile() {
        return AdvancedMachinesConfig.MACERATOR_WORK_SOUND;
    }

    @Override
    public String getSpeedName() {
        return LangHelper.format("inv.speed.macerator");
    }
}
