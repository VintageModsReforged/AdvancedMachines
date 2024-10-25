package ic2.advancedmachines.common.slot;

import net.minecraft.item.ItemStack;

public interface IStackFilter {

    boolean match(ItemStack stack);
}
