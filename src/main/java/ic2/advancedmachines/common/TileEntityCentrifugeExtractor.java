package ic2.advancedmachines.common;

import ic2.api.Ic2Recipes;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class TileEntityCentrifugeExtractor extends TileEntityAdvancedMachine {
    public TileEntityCentrifugeExtractor() {
        super("Centrifuge Extractor", new int[]{1}, new int[]{2, 3, 4});
    }

    @Override
    public Container getGuiContainer(InventoryPlayer var1) {
        return new ContainerCentrifugeExtractor(var1, this);
    }

    @Override
    public ItemStack getResultFor(ItemStack input, boolean adjustOutput) {
        return Ic2Recipes.getExtractorOutputFor(input, adjustOutput);
    }

    @Override
    public int getUpgradeSlotsStartSlot() {
        return 5;
    }

    @Override
    public String getStartSoundFile() {
        return AdvancedMachines.advExtcSound;
    }

    @Override
    public String getInterruptSoundFile() {
        return AdvancedMachines.interruptSound;
    }

    @Override
    public boolean isRedstonePowered() {
        boolean redstoneUpgrade = false;
        int[] upgradeSlots = new int[] { 5, 6, 7, 8 };
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
