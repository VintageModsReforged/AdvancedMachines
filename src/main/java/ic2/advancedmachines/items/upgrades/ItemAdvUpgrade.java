package ic2.advancedmachines.items.upgrades;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.AdvancedMachines;
import ic2.advancedmachines.AdvancedMachinesConfig;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.advancedmachines.items.IUpgradeItem;
import ic2.advancedmachines.utils.Refs;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemAdvUpgrade extends Item implements IUpgradeItem {

    public String[] names = new String[] {
            "redstone.inverter",
            "cobblestone.generator"
    };

    public ItemAdvUpgrade() {
        super(AdvancedMachinesConfig.ADV_UPGRADE_ID);
        this.setTextureFile(Refs.ITEMS);
        this.setCreativeTab(AdvancedMachines.ADV_TAB);
        this.setHasSubtypes(true);
        this.setItemName("advanced.upgrade");
    }

    @Override
    public int getIconFromDamage(int meta) {
        return meta;
    }

    @Override
    public String getItemNameIS(ItemStack stack) {
        int damage = stack.getItemDamage();
        return "item." + names[damage];
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(int id, CreativeTabs tabs, List itemList) {
        for(int i = 0; i < names.length; ++i) {
            ItemStack is = new ItemStack(this, 1, i);
            itemList.add(is);
        }
    }

    @Override
    public boolean canTick(ItemStack upgrade) {
        return upgrade.getItemDamage() == 1; // only cobblestone upgrade for now
    }

    @Override
    public boolean onTick(TileEntityAdvancedMachine machine, ItemStack upgrade) {
        int meta = upgrade.getItemDamage();
        if (meta == 1) { // Cobblestone generator
            return generateCobblestone(machine, upgrade);
        }
        return false;
    }

    public int lastFilledSlot = 0; // Tracks the last filled input slot

    /**
     * Generates cobblestone and fills the input slots one at a time.
     *
     * @param machine The machine instance.
     * @param cobblestoneUpgrade The cobblestone upgrade item.
     * @return True if the machine can process cobblestone, otherwise false.
     */
    public boolean generateCobblestone(TileEntityAdvancedMachine machine, ItemStack cobblestoneUpgrade) {
        if (machine.getWorldObj().getTotalWorldTime() % 20 == 0) {
            int[] inputs = machine.inputs;
            ItemStack cobblestone = new ItemStack(Block.cobblestone);
            boolean canProcessCobblestone = machine.inputFilter.match(cobblestone);

            if (canProcessCobblestone) {
                int cobblestoneLeft = cobblestoneUpgrade.stackSize;
                for (int i = 0; i < inputs.length; i++) {
                    int inputIndex = inputs[i];
                    if (cobblestoneLeft <= 0) {
                        break;
                    }
                    // Fill the input slot with cobblestone
                    if (machine.inventory[inputIndex] == null) {
                        machine.inventory[inputIndex] = new ItemStack(Block.cobblestone, Math.min(cobblestoneLeft, cobblestone.getMaxStackSize()));
                        cobblestoneLeft -= machine.inventory[inputIndex].stackSize;
                    } else if (machine.inventory[inputIndex].stackSize < cobblestone.getMaxStackSize() && machine.inventory[inputIndex].isItemEqual(cobblestone)) {
                        int spaceLeft = cobblestone.getMaxStackSize() - machine.inventory[inputIndex].stackSize;
                        int amountToAdd = Math.min(spaceLeft, cobblestoneLeft);
                        machine.inventory[inputIndex].stackSize += amountToAdd;
                        cobblestoneLeft -= amountToAdd;
                    }
                    // If the current slot is full, move to the next slot
                    if (machine.inventory[inputIndex].stackSize >= cobblestone.getMaxStackSize()) {
                        lastFilledSlot = (lastFilledSlot + 1) % inputs.length;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
