package ic2.advancedmachines.items.upgrades;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.AdvancedMachines;
import ic2.advancedmachines.AdvancedMachinesConfig;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.advancedmachines.items.IUpgradeItem;
import ic2.advancedmachines.utils.AdvUtils;
import ic2.advancedmachines.utils.Refs;
import ic2.api.Direction;
import ic2.core.util.StackUtil;
import mods.vintage.core.platform.lang.FormattedTranslator;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

public class ItemAdvUpgrade extends Item implements IUpgradeItem {

    public String[] names = new String[] {
            "redstone.inverter",
            "cobblestone.generator",
            "ejector"
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
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public int getIconIndex(ItemStack stack, int pass) {
        int damage = stack.getItemDamage();
        if (damage == 2) {
            int outputSide = StackUtil.getOrCreateNbtData(stack).getByte("output");
            return getIconFromDamage(22 + outputSide);
        } else return super.getIconIndex(stack);
    }

    @Override
    public String getItemNameIS(ItemStack stack) {
        int damage = stack.getItemDamage();
        return "item." + names[damage];
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug) {
        super.addInformation(stack, player, list, debug);
        if (stack.getItemDamage() == 2) {
            String side;
            int outputSide = StackUtil.getOrCreateNbtData(stack).getByte("output") - 1;
            switch (outputSide) {
                case 0:
                    side = "output.west";
                    break;
                case 1:
                    side = "output.east";
                    break;
                case 2:
                    side = "output.bottom";
                    break;
                case 3:
                    side = "output.top";
                    break;
                case 4:
                    side = "output.north";
                    break;
                case 5:
                    side = "output.south";
                    break;
                default:
                    side = "output.all";
            }
            list.add(FormattedTranslator.AQUA.format("tooltip.item.upgrade.ejector", FormattedTranslator.GOLD.format(side)));
        }
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xOffset, float yOffset, float zOffset) {
        if (stack.getItemDamage() == 2) {
            int outputSide = 1 + (side + 2) % 6;
            NBTTagCompound tag = StackUtil.getOrCreateNbtData(stack);
            if (tag.getByte("output") != outputSide) {
                tag.setByte("output", (byte) outputSide);
                return true;
            }
        }
        return false;
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
        return upgrade.getItemDamage() == 1
                || upgrade.getItemDamage() == 2;
    }

    @Override
    public boolean onTick(TileEntityAdvancedMachine machine, ItemStack upgrade) {
        int meta = upgrade.getItemDamage();
        if (meta == 1) { // Cobblestone generator
            return generateCobblestone(machine, upgrade);
        } else if (meta == 2) {
            return handleTransport(machine, upgrade);
        }
        return false;
    }

    public boolean handleTransport(TileEntityAdvancedMachine machine, ItemStack upgrade) {
        for (int i = 0; i < machine.outputs.length; i++) {
            int outputSlot = machine.outputs[i];
            ItemStack output = machine.inventory[outputSlot];
            if (output != null && machine.energy >= 20) {
                int amount = Math.min(output.stackSize, machine.energy / 20);
                int dir = StackUtil.getOrCreateNbtData(upgrade).getByte("output");
                if (dir >= 1 && dir <= 6) {
                    TileEntity te = Direction.values()[dir - 1].applyToTileEntity(machine);
                    if (!(te instanceof IInventory)) {
                        return false;
                    }
                    amount = AdvUtils.extractToInventory((IInventory) te, StackUtil.copyWithSize(output, amount), false);
                } else {
                    amount = AdvUtils.distribute(machine, StackUtil.copyWithSize(output, amount), false);
                }
                output.stackSize -= amount;
                if (output.stackSize <= 0) {
                    machine.inventory[outputSlot] = null;
                }
                machine.energy -= 20 * amount;
                return true;
            }
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
