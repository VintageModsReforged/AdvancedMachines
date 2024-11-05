package ic2.advancedmachines.items.upgrades;

import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import net.minecraft.item.ItemStack;

public interface ISimpleUpgrade {
    boolean canTick(ItemStack upgrade);
    boolean onTick(TileEntityAdvancedMachine machine, ItemStack upgrade);
}
