package ic2.advancedmachines.utils;

import net.minecraft.item.ItemStack;

public interface IStackFilter {

    boolean match(ItemStack stack);
}
