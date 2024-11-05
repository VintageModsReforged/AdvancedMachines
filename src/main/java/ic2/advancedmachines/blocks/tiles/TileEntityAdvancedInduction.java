package ic2.advancedmachines.blocks.tiles;

import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.advancedmachines.AdvancedMachinesConfig;
import ic2.advancedmachines.utils.LangHelper;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotProcessable;
import ic2.core.block.invslot.InvSlotProcessableSmelting;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import java.util.ArrayList;
import java.util.List;

public class TileEntityAdvancedInduction extends TileEntityAdvancedMachine {

    public TileEntityAdvancedInduction() {
        super(LangHelper.format("block.advanced.induction.name"), 5);
    }

    @Override
    public void addSlots() {
        // inputs
        this.inputs.add(new InvSlotProcessableSmelting(this, "inputA", 1, 1));
        this.inputs.add(new InvSlotProcessableSmelting(this, "inputB", 2, 1));
        // outputs
        this.outputs.add(new InvSlotOutput(this, "outputA", 3, 1));
        this.outputs.add(new InvSlotOutput(this, "outputB", 4, 1));
    }

    @Override
    public List<SlotInvSlot> getSlots() {
        List<SlotInvSlot> slots = new ArrayList<SlotInvSlot>();
        slots.add(new SlotInvSlot(this.inputs.get(0), 0, 47, 17));
        slots.add(new SlotInvSlot(this.inputs.get(1), 0, 63, 17));

        slots.add(new SlotInvSlot(this.outputs.get(0), 0, 113, 35));
        slots.add(new SlotInvSlot(this.outputs.get(1), 0, 131, 35));

        return slots;
    }

    @Override
    public ItemStack getResultFor(ItemStack input, boolean adjustOutput) {
        ItemStack result = FurnaceRecipes.smelting().getSmeltingResult(input);
        if (adjustOutput && result != null) {
            --input.stackSize;
        }
        return result;
    }

    @Override
    public String getStartSoundFile() {
        return AdvancedMachinesConfig.INDUCTION_WORK_SOUND;
    }

    @Override
    public String getSpeedName() {
        return LangHelper.format("inv.speed.induction");
    }

    @Override
    public void operate() {
        InvSlotProcessable inputA = this.inputs.get(0);
        InvSlotProcessable inputB = this.inputs.get(1);
        InvSlotOutput outputA = this.outputs.get(0);
        InvSlotOutput outputB = this.outputs.get(1);

        boolean inputAEmpty = this.inputs.get(0).isEmpty();
        boolean inputBEmpty = this.inputs.get(1).isEmpty();

        boolean canProcessAA = canOperate(inputA, outputA);
        boolean canProcessAB = canOperate(inputA, outputB);
        boolean canProcessBB = canOperate(inputB, outputB);
        boolean canProcessBA = canOperate(inputB, outputA);

        // inputA
        if (!inputAEmpty) { // if input A isn't empty
            boolean inputBNotProcessable = inputBEmpty || (!canProcessBB && !canProcessBA); // if input B is empty OR we can't process it into either one of the outputs
            if (inputBNotProcessable) {
                if (canProcessAA) {
                    operate(inputA, outputA); // process input A into output A
                    operate(inputA, outputA); // process input A into output A
                } else if (canProcessAB) {
                    operate(inputA, outputB); // process input A into output B
                    operate(inputA, outputB); // process input A into output B
                }
            }
        }

        // inputB
        if (!inputBEmpty) { // if input B isn't empty
            boolean inputANotProcessable = inputAEmpty || (!canProcessAA && !canProcessAB); // if input A is empty OR we can't process it into either one of the outputs
            if (inputANotProcessable) {
                if (canProcessBB) {
                    operate(inputB, outputB); // process input B into output B
                    operate(inputB, outputB); // process input B into output B
                } else if (canProcessBA) {
                    operate(inputB, outputA); // process input B into output A
                    operate(inputB, outputA); // process input B into output A
                }
            }
        }

        if (!inputAEmpty && !inputBEmpty) { // if both inputs aren't empty
            if (canProcessAA && canProcessBB) { // if we can process A-A and B-B
                operate(inputA, outputA); // process input A into output A
                operate(inputB, outputB); // process input B into output B

            } else if (canProcessAB && canProcessBA) { // if we can process A-B and B-A
                operate(inputA, outputB); // process input A into output B
                operate(inputB, outputA); // process input B into output A

            } else if (canProcessAA && canProcessBA) { // if we can process A-A and B-A
                operate(inputA, outputA); // process input A into output A
                operate(inputB, outputB); // process input B into output A

            } else if (canProcessAB && canProcessBB) { // if we can process A-B and B-B
                operate(inputA, outputB); // process input A into output B
                operate(inputB, outputB); // process input B into output B
            }
        }
    }

    public void operate(InvSlotProcessable inputSlot, InvSlotOutput outputSlot) {
        if (this.canOperate(inputSlot, outputSlot)) {
            outputSlot.add(inputSlot.process(false));
        }
    }

    @Override
    public boolean canOperate() {
        return canOperate(this.inputs.get(0), this.outputs.get(0)) || canOperate(this.inputs.get(0), this.outputs.get(1)) ||
                canOperate(this.inputs.get(1), this.outputs.get(0)) || canOperate(this.inputs.get(1), this.outputs.get(1));
    }

    public boolean canOperate(InvSlotProcessable inputSlot, InvSlotOutput outputSlot) {
        if (inputSlot.isEmpty()) {
            return false;
        } else {
            ItemStack processResult = inputSlot.process(true);
            return processResult != null && outputSlot.canAdd(processResult);
        }
    }
}
