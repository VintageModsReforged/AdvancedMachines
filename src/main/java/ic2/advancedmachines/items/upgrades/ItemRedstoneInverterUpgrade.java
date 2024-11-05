package ic2.advancedmachines.items.upgrades;

import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.core.block.machine.tileentity.TileEntityStandardMachine;
import ic2.core.item.IUpgradeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemRedstoneInverterUpgrade extends Item implements ISimpleUpgrade {

    public ItemRedstoneInverterUpgrade(int par1) {
        super(par1);
    }

    @Override
    public boolean canTick(ItemStack upgrade) {
        return false;
    }

    @Override
    public boolean onTick(TileEntityAdvancedMachine machine, ItemStack upgrade) {
        return false;
    }
}
