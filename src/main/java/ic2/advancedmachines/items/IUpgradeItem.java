package ic2.advancedmachines.items;

import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import net.minecraft.item.ItemStack;

public interface IUpgradeItem {

    boolean canTick(ItemStack upgrade);

    boolean onTick(TileEntityAdvancedMachine machine, ItemStack upgrade);
}
