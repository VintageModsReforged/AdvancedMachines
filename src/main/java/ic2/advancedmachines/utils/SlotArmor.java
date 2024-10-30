package ic2.advancedmachines.utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotArmor extends Slot {

    public InventoryPlayer inventory;
    public int armorIndex;

    public SlotArmor(InventoryPlayer playerInv, int armorIndex, int xPos, int yPos) {
        super(playerInv, 36 + (3 - armorIndex), xPos, yPos);
        this.inventory = playerInv;
        this.armorIndex = armorIndex;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem().isValidArmor(stack, this.armorIndex);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getBackgroundIconIndex() {
        return 15 + this.armorIndex * 16;
    }
}
