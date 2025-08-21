package ic2.advancedmachines.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.AdvancedMachines;
import ic2.advancedmachines.AdvancedMachinesConfig;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.advancedmachines.items.upgrades.ISimpleUpgrade;
import ic2.core.block.invslot.InvSlotProcessable;
import mods.vintage.core.platform.config.IItemBlockIDProvider;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import java.util.List;

public class ItemAdvUpgrade extends Item implements ISimpleUpgrade, IItemBlockIDProvider {

    public String[] names = new String[] {
            "redstone.inverter",
            "cobblestone.generator"
    };

    public Icon[] icons = new Icon[names.length];

    public ItemAdvUpgrade() {
        super(AdvancedMachinesConfig.ADV_UPGRADE_ID.get());
        this.setUnlocalizedName("advanced.upgrade");
        this.setHasSubtypes(true);
        this.setCreativeTab(AdvancedMachines.ADV_TAB);
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

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister registry) {
        for (int i = 0; i < names.length; i++) {
            icons[i] = registry.registerIcon("AdvancedMachines:upgrades/" + names[i]);
        }
    }

    @Override
    public Icon getIconFromDamage(int meta) {
        return this.icons[meta];
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int damage = stack.getItemDamage();
        return "item." + names[damage];
    }

    @Override
    public boolean canTick(ItemStack upgrade) {
        return upgrade.getItemDamage() == 1;
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
            List<InvSlotProcessable> inputs = machine.inputs;
            ItemStack cobblestone = new ItemStack(Block.cobblestone);
            boolean canProcessCobblestone = machine.inputFilter.match(cobblestone);

            if (canProcessCobblestone) {
                int cobblestoneLeft = cobblestoneUpgrade.stackSize;
                for (int i = 0; i < inputs.size(); i++) {
                    InvSlotProcessable inputIndex = inputs.get(i);
                    if (cobblestoneLeft <= 0) {
                        break;
                    }
                    // Fill the input slot with cobblestone
                    if (inputIndex.isEmpty()) {
                        inputIndex.put(new ItemStack(Block.cobblestone, Math.min(cobblestoneLeft, cobblestone.getMaxStackSize())));
                        cobblestoneLeft -= inputIndex.get().stackSize;
                    } else if (inputIndex.get().stackSize < cobblestone.getMaxStackSize() && inputIndex.get().isItemEqual(cobblestone)) {
                        int spaceLeft = cobblestone.getMaxStackSize() - inputIndex.get().stackSize;
                        int amountToAdd = Math.min(spaceLeft, cobblestoneLeft);
                        inputIndex.get().stackSize += amountToAdd;
                        cobblestoneLeft -= amountToAdd;
                    }
                    // If the current slot is full, move to the next slot
                    if (inputIndex.get().stackSize >= cobblestone.getMaxStackSize()) {
                        lastFilledSlot = (lastFilledSlot + 1) % inputs.size();
                    }
                }
                return true;
            }
        }
        return false;
    }
}
