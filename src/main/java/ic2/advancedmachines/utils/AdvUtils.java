package ic2.advancedmachines.utils;

import ic2.api.Direction;
import ic2.api.IElectricItem;
import ic2.core.block.personal.IPersonalBlock;
import ic2.core.item.ElectricItem;
import ic2.core.util.StackUtil;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdvUtils {

    private static final Direction[] directions = Direction.values();

    public static String getDisplayTier(int tier) {
        switch (tier) {
            case 1: return "LV";
            case 2: return "MV";
            case 3: return "HV";
            case 4: return "EV";
            case 5: return "IV";
            case 6: return "LuV";
            default: return String.valueOf(tier);
        }
    }

    public static int getCharge(ItemStack stack) {
        NBTTagCompound tag = StackUtil.getOrCreateNbtData(stack);
        return tag.getInteger("charge");
    }

    public static ItemStack getCharged(Item item, int charge) {
        if (!(item instanceof IElectricItem)) {
            throw new IllegalArgumentException(item + " must be an instance of IElectricItem!");
        } else {
            ItemStack ret = new ItemStack(item);
            ElectricItem.charge(ret, charge, Integer.MAX_VALUE, true, false);
            return ret;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void addChargeVariants(Item item, List list) {
        list.add(getCharged(item, 0));
        list.add(getCharged(item, Integer.MAX_VALUE));
    }

    public static int getPowerFromTier(int tier) {
        return 8 << Math.min(tier, 13) * 2;
    }

    public static int extractToInventory(IInventory inventory, ItemStack stack, boolean simulate) {
        int transferred = 0;
        for(int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack itemStack = inventory.getStackInSlot(i);
            if (itemStack != null && itemStack.isItemEqual(stack)) {
                int transfer = Math.min(stack.stackSize - transferred, itemStack.getMaxStackSize() - itemStack.stackSize);
                if (!simulate) {
                    itemStack.stackSize += transfer;
                }

                transferred += transfer;
                if (transferred == stack.stackSize) {
                    return transferred;
                }
            }
        }

        for(int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack itemStack = inventory.getStackInSlot(i);
            if (itemStack == null) {
                int transfer = Math.min(stack.stackSize - transferred, stack.getMaxStackSize());
                if (!simulate) {
                    ItemStack dest = StackUtil.copyWithSize(stack, transfer);
                    inventory.setInventorySlotContents(i, dest);
                }

                transferred += transfer;
                if (transferred == stack.stackSize) {
                    return transferred;
                }
            }
        }

        return transferred;
    }

    public static int distribute(TileEntity source, ItemStack itemStack, boolean simulate) {
        int transferred = 0;

        for(IInventory inventory : getAdjacentInventories(source)) {
            int amount = extractToInventory(inventory, itemStack, simulate);
            transferred += amount;
            itemStack.stackSize -= amount;
            if (itemStack.stackSize == 0) {
                break;
            }
        }

        itemStack.stackSize += transferred;
        return transferred;
    }

    public static List<IInventory> getAdjacentInventories(TileEntity source) {
        List<IInventory> inventories = new ArrayList<IInventory>();

        for(Direction direction : directions) {
            TileEntity target = direction.applyToTileEntity(source);
            if (target instanceof IInventory) {
                IInventory inventory = (IInventory)target;
                if (target instanceof TileEntityChest) {
                    for(Direction direction2 : directions) {
                        if (direction2 != Direction.YN && direction2 != Direction.YP) {
                            TileEntity target2 = direction2.applyToTileEntity(target);
                            if (target2 instanceof TileEntityChest) {
                                inventory = new InventoryLargeChest("", inventory, (IInventory)target2);
                                break;
                            }
                        }
                    }
                }

                if (!(target instanceof IPersonalBlock)) {
                    inventories.add(inventory);
                }
            }
        }

        Collections.sort(inventories, new Comparator<IInventory>() {
            @Override
            public int compare(IInventory a, IInventory b) {
                if (!(a instanceof IPersonalBlock) && b instanceof IPersonalBlock) {
                    return !(b instanceof IPersonalBlock) && a instanceof IPersonalBlock ? b.getSizeInventory() - a.getSizeInventory() : 1;
                } else {
                    return -1;
                }
            }
        });
        return inventories;
    }
}
