package ic2.advancedmachines.common;

import ic2.core.util.StackUtil;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class TileAdvancedInduction extends TileEntityAdvancedMachine {

    public int inputAIndex = 1;
    public int inputBIndex = 2;
    public int outputAIndex = 3;
    public int outputBIndex = 4;

    public TileAdvancedInduction() {
        super("Induction Furnace", "%2d HU", 10, new int[] {1, 2}, new int[] {3, 4});
        this.upgradeSlots = new int[] {5, 6, 7, 8};
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
    public boolean isStackValidForSlot(int slot, ItemStack stack) {
        if (slot == this.inputAIndex || slot == this.inputBIndex) {
            return FurnaceRecipes.smelting().getSmeltingResult(stack) != null;
        }
        return false;
    }

    @Override
    public Container getGuiContainer(InventoryPlayer player) {
        return new ContainerInduction(player, this);
    }

    @Override
    public int getUpgradeSlotsStartSlot() {
        return 5;
    }

    @Override
    public void operate() {
        if (this.inventory[this.inputAIndex] == null) {
            this.operate(this.inputBIndex, this.outputAIndex);
            this.operate(this.inputBIndex, this.outputBIndex);
        } else if (this.inventory[this.inputBIndex] == null) {
            this.operate(this.inputAIndex, this.outputAIndex);
            this.operate(this.inputAIndex, this.outputBIndex);
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
                            this.put(slotIndex, (ItemStack)null);
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
