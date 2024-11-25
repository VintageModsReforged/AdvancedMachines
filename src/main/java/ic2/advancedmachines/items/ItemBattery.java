package ic2.advancedmachines.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.AdvancedMachines;
import ic2.advancedmachines.utils.AdvUtils;
import ic2.advancedmachines.utils.Refs;
import ic2.api.ElectricItem;
import ic2.api.IElectricItem;
import ic2.core.IC2;
import ic2.core.util.StackUtil;
import core.platform.lang.TextFormatter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class ItemBattery extends Item implements IElectricItem {

    public int tier, maxCharge, transfer;
    public String name;

    public ItemBattery(int id, String name, int tier, int transfer, int maxStorage) {
        super(id);
        this.setMaxDamage(27);
        this.setMaxStackSize(1);
        this.setCreativeTab(AdvancedMachines.ADV_TAB);
        this.setTextureFile(Refs.ITEMS);
        this.name = name;
        this.tier = tier;
        this.transfer = transfer;
        this.maxCharge = maxStorage;
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean isDebug) {
        DecimalFormat formatter = new DecimalFormat("###,###", new DecimalFormatSymbols(Locale.ROOT));
        tooltip.add(TextFormatter.AQUA.format("tooltips.energy.storage.info", formatter.format(AdvUtils.getCharge(stack)), formatter.format(this.getMaxCharge())));
        tooltip.add(TextFormatter.LIGHT_PURPLE.format("tooltips.energy.tier.info", this.getTier(), AdvUtils.getDisplayTier(this.getTier())));
        super.addInformation(stack, player, tooltip, isDebug);
    }

    @Override
    public int getIconFromDamage(int damage) {
        if (damage <= 1) {
            return this.iconIndex + 4;
        } else if (damage <= 8) {
            return this.iconIndex + 3;
        } else if (damage <= 14) {
            return this.iconIndex + 2;
        } else {
            return damage <= 20 ? this.iconIndex + 1 : this.iconIndex;
        }
    }

    @Override
    public String getItemName() {
        return this.name;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return StackUtil.getOrCreateNbtData(stack).getBoolean("active");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (IC2.platform.isSimulating()) {
            if (player.isSneaking()) {
                NBTTagCompound tag = StackUtil.getOrCreateNbtData(stack);
                boolean status = tag.getBoolean("active");
                tag.setBoolean("active", !status);
            }
        }
        return super.onItemRightClick(stack, world, player);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int hand, boolean update) {
        if (IC2.platform.isSimulating()) {
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                boolean isInHotbar = false;
                ItemStack batteryStack = null;
                for (int i = 0; i < 9; i++) {
                    ItemStack hotbarStack = player.inventory.getStackInSlot(i);
                    if (hotbarStack != null && hotbarStack.getItem() instanceof ItemBattery) {
                        batteryStack = hotbarStack;
                        isInHotbar = true;
                        break;
                    }
                }

                if (isInHotbar) {
                    boolean inventoryChanged = false;
                    for (int i = 0; i < 9; i++) {
                        ItemStack hotbarTool = player.inventory.getStackInSlot(i);
                        if (hotbarTool != null && hotbarTool.getItem() instanceof IElectricItem) {
                            IElectricItem hotbarElectricItem = (IElectricItem) hotbarTool.getItem();
                            if (hotbarElectricItem.getTier() <= ((IElectricItem) batteryStack.getItem()).getTier() && canProvideEnergy(batteryStack)) {
                                int transfer = ElectricItem.charge(hotbarTool, Integer.MAX_VALUE, Integer.MAX_VALUE, true, true);
                                transfer = ElectricItem.discharge(batteryStack, transfer, Integer.MAX_VALUE, true, false);
                                if (transfer > 0) {
                                    ElectricItem.charge(hotbarTool, transfer, Integer.MAX_VALUE, true, false);
                                    inventoryChanged = true;
                                }
                            }
                        }
                    }
                    if (inventoryChanged) player.openContainer.detectAndSendChanges();
                }
            }
        }
    }

    @Override
    public String getItemNameIS(ItemStack stack) {
        return "item." + this.name;
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(int id, CreativeTabs tabs, List itemList) {
        AdvUtils.addChargeVariants(this, itemList);
    }

    public boolean canProvideEnergy(ItemStack stack) {
        return StackUtil.getOrCreateNbtData(stack).getBoolean("active");
    }

    @Override
    public boolean canProvideEnergy() {
        return true;
    }

    @Override
    public int getChargedItemId() {
        return this.itemID;
    }

    @Override
    public int getEmptyItemId() {
        return this.itemID;
    }

    @Override
    public int getMaxCharge() {
        return this.maxCharge;
    }

    @Override
    public int getTier() {
        return this.tier;
    }

    @Override
    public int getTransferLimit() {
        return this.transfer;
    }
}
