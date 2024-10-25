package ic2.advancedmachines.common.tiles;

import ic2.advancedmachines.common.AdvancedMachinesConfig;
import ic2.advancedmachines.common.slot.SlotFiltered;
import ic2.advancedmachines.common.tiles.base.TileEntityAdvancedMachine;
import ic2.advancedmachines.lang.LangHelper;
import ic2.core.util.StackUtil;
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
        super(LangHelper.format("block.advanced.induction.name"), new int[] {1, 2}, new int[] {3, 4});
    }

    @Override
    public List<Slot> getSlots(InventoryPlayer playerInv) {
        List<Slot> slots = new ArrayList<Slot>();
        slots.add(SlotFiltered.furnaceSlot(this, this.inputAIndex, 47, 17)); // input 1
        slots.add(SlotFiltered.furnaceSlot(this, this.inputBIndex, 63, 17)); // input 2
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
        return LangHelper.format("inv.speed.induction");
    }

    @Override
    public void operate() {
        if (this.inventory[this.inputAIndex] == null && this.inventory[this.inputBIndex] != null) {
            this.operate(this.inputBIndex, this.outputBIndex);
            this.operate(this.inputBIndex, this.outputBIndex);
        } else if (this.inventory[this.inputAIndex] != null && this.inventory[this.inputBIndex] == null) {
            this.operate(this.inputAIndex, this.outputAIndex);
            this.operate(this.inputAIndex, this.outputAIndex);
        } else {
            this.operate(this.inputAIndex, this.outputAIndex);
            this.operate(this.inputBIndex, this.outputBIndex);
        }
    }

    public void operate(int inputSlot, int outputSlot) {
        if (this.canOperate(inputSlot, outputSlot)) {
            add(process(inputSlot, false), outputSlot, false);
        }
    }

    @Override
    public boolean canOperate() {
        return canOperate(inputAIndex, outputAIndex) || canOperate(inputBIndex, outputBIndex);
    }

    public boolean canOperate(int inputSlot, int outputSlot) {
        if (this.inventory[inputSlot] == null) {
            return false;
        } else {
            ItemStack processResult = process(inputSlot, true);
            return add(processResult, outputSlot, true) == 0;
        }
    }

    public void put(int index, ItemStack content) {
        this.inventory[index] = content;
    }

    private int add(ItemStack itemStack, int outputIndex, boolean simulate) {
        if (itemStack == null) {
            return 0;
        } else {
            int amount = itemStack.stackSize;
            ItemStack existingItemStack = this.inventory[outputIndex];
            if (existingItemStack == null) {
                if (!simulate) {
                    this.put(outputIndex, itemStack);
                }

                return 0;
            }

            int space = existingItemStack.getMaxStackSize() - existingItemStack.stackSize;
            if (space > 0 && StackUtil.isStackEqual(itemStack, existingItemStack)) {
                if (space >= amount) {
                    if (!simulate) {
                        existingItemStack.stackSize += amount;
                    }

                    return 0;
                }

                amount -= space;
                if (!simulate) {
                    existingItemStack.stackSize += space;
                }
            }

            return amount;
        }
    }

    public ItemStack process(int slotIndex, boolean simulate) {
        ItemStack input = this.consume(slotIndex, 1, simulate, true);
        return input == null ? null : FurnaceRecipes.smelting().getSmeltingResult(input).copy();
    }

    public ItemStack consume(int slotIndex, int amount, boolean simulate, boolean consumeContainers) {
        ItemStack ret = null;
        if(amount != 0) {
            ItemStack itemStack = this.inventory[slotIndex];
            if (itemStack != null && FurnaceRecipes.smelting().getSmeltingResult(itemStack) != null && (ret == null || StackUtil.isStackEqual(itemStack, ret))) {
                int currentAmount = Math.min(amount, itemStack.stackSize);
                if (!simulate) {
                    if (itemStack.stackSize == currentAmount) {
                        if (!consumeContainers && itemStack.getItem().hasContainerItem()) {
                            this.put(slotIndex, itemStack.getItem().getContainerItemStack(itemStack));
                        } else {
                            this.put(slotIndex, null);
                        }
                    } else {
                        itemStack.stackSize -= currentAmount;
                    }
                }

                if (ret == null) {
                    ret = StackUtil.copyWithSize(itemStack, currentAmount);
                } else {
                    ret.stackSize += currentAmount;
                }
            }
        }
        return ret;
    }
}
