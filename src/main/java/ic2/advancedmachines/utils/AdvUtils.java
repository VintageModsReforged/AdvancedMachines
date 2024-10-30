package ic2.advancedmachines.utils;

import ic2.api.IElectricItem;
import ic2.core.item.ElectricItem;
import ic2.core.util.StackUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class AdvUtils {

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
}
