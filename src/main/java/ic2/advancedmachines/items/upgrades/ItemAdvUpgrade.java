package ic2.advancedmachines.items.upgrades;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.AdvancedMachinesConfig;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.advancedmachines.items.IUpgradeItem;
import ic2.advancedmachines.utils.Refs;
import ic2.core.IC2;
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
        this.setCreativeTab(IC2.tabIC2);
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
                for (int i = 0; i < inputs.length; i++) {
                    int inputIndex = inputs[i];

                    // Fill the input slot with cobblestone
                    if (machine.inventory[inputIndex] == null) {
                        machine.inventory[inputIndex] = new ItemStack(Block.cobblestone, cobblestoneUpgrade.stackSize);
                    } else if (machine.inventory[inputIndex].stackSize < cobblestone.getMaxStackSize()) {
                        machine.inventory[inputIndex].stackSize += Math.min(cobblestone.getMaxStackSize() - machine.inventory[inputIndex].stackSize, cobblestoneUpgrade.stackSize);
                    }

                    // If the current slot is not yet full, stop here
                    if (machine.inventory[inputIndex].stackSize < cobblestone.getMaxStackSize()) {
                        break;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
