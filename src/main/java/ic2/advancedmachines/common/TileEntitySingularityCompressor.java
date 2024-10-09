package ic2.advancedmachines.common;

import ic2.api.Ic2Recipes;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class TileEntitySingularityCompressor extends TileEntityAdvancedMachine {
    public TileEntitySingularityCompressor() {
        super("Singularity Compressor", new int[]{1}, new int[]{2});
    }

    @Override
    public Container getGuiContainer(InventoryPlayer var1) {
        return new ContainerSingularityCompressor(var1, this);
    }

    @Override
    public ItemStack getResultFor(ItemStack input, boolean adjustOutput) {
        return Ic2Recipes.getCompressorOutputFor(input, adjustOutput);
    }

    @Override
    public int getUpgradeSlotsStartSlot() {
        return 3;
    }

    @Override
    public String getStartSoundFile() {
        return AdvancedMachines.advCompSound;
    }

    @Override
    public String getInterruptSoundFile() {
        return AdvancedMachines.interruptSound;
    }

    @Override
    public boolean isRedstonePowered() {
        boolean redstoneUpgrade = false;
        int[] upgradeSlots = new int[] { 3, 4, 5, 6 };
        for (int upgradeSlot : upgradeSlots) {
            ItemStack upgrade = this.inventory[upgradeSlot];
            if (upgrade != null) {
                if (upgrade.isItemEqual(new ItemStack(AdvancedMachines.redstoneUpgrade))) {
                    redstoneUpgrade = true;
                    break;
                }
            }
        }
        return super.isRedstonePowered() || redstoneUpgrade;
    }
}
