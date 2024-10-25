package ic2.advancedmachines.common.tiles.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public abstract class TileEntityMachine extends TileEntityBlock implements IInventory {
    public ItemStack[] inventory;

    public TileEntityMachine(int invSize) {
        super();
        this.inventory = new ItemStack[invSize];
    }

    @Override
    public int getSizeInventory() {
        return this.inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex) {
        return this.inventory[slotIndex];
    }

    @Override
    public ItemStack decrStackSize(int slot, int stackSize) {
        ItemStack stack = this.getStackInSlot(slot);
        if (stack != null) {
            if (stack.stackSize <= stackSize) {
                this.setInventorySlotContents(slot, (ItemStack) null);
            } else {
                stack = stack.splitStack(stackSize);
                if (stack.stackSize == 0) {
                    this.setInventorySlotContents(slot, (ItemStack) null);
                }
            }
        }
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        this.inventory[slot] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this
                && player.getDistance((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public abstract String getInvName();

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        NBTTagList tagList = tagCompound.getTagList("Items");
        this.inventory = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound nbtTagCompound = (NBTTagCompound) tagList.tagAt(i);
            byte slot = nbtTagCompound.getByte("Slot");
            if (slot >= 0 && slot < this.inventory.length) {
                this.inventory[slot] = ItemStack.loadItemStackFromNBT(nbtTagCompound);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        NBTTagList tagList = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i) {
            if (this.inventory[i] != null) {
                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                nbtTagCompound.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(nbtTagCompound);
                tagList.appendTag(nbtTagCompound);
            }
        }
        tagCompound.setTag("Items", tagList);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
    }

    @Override
    public void openChest() {}

    @Override
    public void closeChest() {}

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack = this.getStackInSlot(slot);
        if (stack != null) {
            this.setInventorySlotContents(slot, (ItemStack)null);
        }
        return stack;
    }
}
