package ic2.advancedmachines.blocks.tiles.machines;

import ic2.advancedmachines.AdvancedMachinesConfig;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.advancedmachines.utils.AdvSlot;
import ic2.advancedmachines.utils.StackFilters;
import mods.vintage.core.platform.lang.Translator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import java.util.ArrayList;
import java.util.List;

public class TileEntityAdvancedInduction extends TileEntityAdvancedMachine {

    public int inputAIndex = 1;
    public int inputBIndex = 2;
    public int outputAIndex = 3;
    public int outputBIndex = 4;

    public TileEntityAdvancedInduction() {
        super(Translator.format("block.advanced.induction.name"), new int[] {1, 2}, new int[] {3, 4}, StackFilters.FURNACE_FILTER, "Induction");
    }

    @Override
    public List<Slot> getSlots(InventoryPlayer playerInv) {
        List<Slot> slots = new ArrayList<Slot>();
        slots.add(AdvSlot.filtered(this, this.inputAIndex, 47, 17)); // input 1
        slots.add(AdvSlot.filtered(this, this.inputBIndex, 63, 17)); // input 2
        slots.add(new SlotFurnace(playerInv.player, this, this.outputAIndex, 113, 35)); // output
        slots.add(new SlotFurnace(playerInv.player, this, this.outputBIndex, 131, 35)); // output
        return slots;
    }

    @Override
    public int[] getUpgradeSlots() {
        return new int[] { 5, 6 };
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
        return Translator.format("inv.speed.induction");
    }

    @Override
    public void operate() {
        boolean inputAEmpty = this.inventory[this.inputAIndex] == null;
        boolean inputBEmpty = this.inventory[this.inputBIndex] == null;

        boolean canProcessAA = canOperate(inputAIndex, outputAIndex); // if we can process A-A
        boolean canProcessAB = canOperate(inputAIndex, outputBIndex); // if we can process A-B
        boolean canProcessBB = canOperate(inputBIndex, outputBIndex); // if we can process B-B
        boolean canProcessBA = canOperate(inputBIndex, outputAIndex); // if we can process B-A

        // inputA
        if (!inputAEmpty) { // if input A isn't empty
            boolean inputBNotProcessable = inputBEmpty || (!canProcessBB && !canProcessBA); // if input B is empty OR we can't process it into either one of the outputs
            if (inputBNotProcessable) {
                if (canProcessAA) {
                    operate(inputAIndex, outputAIndex); // process input A into output A
                    operate(inputAIndex, outputAIndex); // process input A into output A
                } else if (canProcessAB) {
                    operate(inputAIndex, outputBIndex); // process input A into output B
                    operate(inputAIndex, outputBIndex); // process input A into output B
                }
            }
        }

        if (!inputBEmpty) { // if input B isn't empty
            boolean inputANotProcessable = inputAEmpty || (!canProcessAA && !canProcessAB); // if input A is empty OR we can't process it into either one of the outputs
            if (inputANotProcessable) {
                if (canProcessBB) {
                    operate(inputBIndex, outputBIndex); // process input B into output B
                    operate(inputBIndex, outputBIndex); // process input B into output B
                } else if (canProcessBA) {
                    operate(inputBIndex, outputAIndex); // process input B into output A
                    operate(inputBIndex, outputAIndex); // process input B into output A
                }
            }
        }

        if (!inputAEmpty && !inputBEmpty) { // if both inputs aren't empty
            if (canProcessAA && canProcessBB) { // if we can process A-A and B-B
                operate(inputAIndex, outputAIndex); // process input A into output A
                operate(inputBIndex, outputBIndex); // process input B into output B

            } else if (canProcessAB && canProcessBA) { // if we can process A-B and B-A
                operate(inputAIndex, outputBIndex); // process input A into output B
                operate(inputBIndex, outputAIndex); // process input B into output A

            } else if (canProcessAA && canProcessBA) { // if we can process A-A and B-A
                operate(inputAIndex, outputAIndex); // process input A into output A
                operate(inputBIndex, outputAIndex); // process input B into output A

            } else if (canProcessAB && canProcessBB) { // if we can process A-B and B-B
                operate(inputAIndex, outputBIndex); // process input A into output B
                operate(inputBIndex, outputBIndex); // process input B into output B
            }
        }
    }

    @Override
    public boolean canOperate() {
        return canOperate(inputAIndex, outputAIndex) || canOperate(inputBIndex, outputBIndex) ||
                canOperate(inputAIndex, outputBIndex) || canOperate(inputBIndex, outputAIndex);
    }

    public boolean canOperate(int inputSlot, int outputSlot) {
        if (this.inventory[inputSlot] == null) {
            return false;
        } else {
            ItemStack result = this.getResultFor(this.inventory[inputSlot], false);
            if (result == null) {
                return false;
            } else return this.inventory[outputSlot] == null || this.inventory[outputSlot].isItemEqual(result) && this.inventory[outputSlot].stackSize + result.stackSize <= result.getMaxStackSize();
        }
    }

    public void operate(int inSlot, int outSlot) {
        if (this.canOperate(inSlot, outSlot)) {
            ItemStack result = this.getResultFor(this.inventory[inSlot], false);
            if (this.inventory[outSlot] == null) {
                this.inventory[outSlot] = result.copy();
            } else {
                ItemStack outputStack = this.inventory[outSlot];
                outputStack.stackSize += result.stackSize;
            }
            if (this.inventory[inSlot].getItem().hasContainerItem()) {
                this.inventory[inSlot] = this.inventory[inSlot].getItem().getContainerItemStack(this.inventory[inSlot]);
            } else {
                --this.inventory[inSlot].stackSize;
            }
            if (this.inventory[inSlot].stackSize <= 0) {
                this.inventory[inSlot] = null;
            }
        }
    }
}
