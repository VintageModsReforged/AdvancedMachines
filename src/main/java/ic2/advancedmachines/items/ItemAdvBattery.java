package ic2.advancedmachines.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.AdvancedMachines;
import ic2.advancedmachines.BlocksItems;
import ic2.advancedmachines.utils.AdvUtils;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.IC2;
import ic2.core.util.StackUtil;
import mods.vintage.core.platform.lang.FormattedTranslator;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class ItemAdvBattery extends Item implements IElectricItem {

    public int tier, maxCharge, transfer;
    public String name;
    public Icon[] textures = new Icon[5];

    public ItemAdvBattery(int id, String name, int tier, int transfer, int maxStorage) {
        super(id);
        this.setMaxDamage(27);
        this.setMaxStackSize(1);
        this.setCreativeTab(AdvancedMachines.ADV_TAB);
        this.name = name;
        this.tier = tier;
        this.transfer = transfer;
        this.maxCharge = maxStorage;
    }

    @Override
    public String getItemDisplayName(ItemStack stack) {
        Item battery = stack.getItem();
        FormattedTranslator format;
        if (battery == BlocksItems.GLOWTRONIC_CRYSTAL) {
            format = FormattedTranslator.YELLOW;
        } else if (battery == BlocksItems.UNIVERSAL_CRYSTAL) {
            format = FormattedTranslator.LIGHT_PURPLE;
        } else format = FormattedTranslator.AQUA;
        return format.literal(super.getItemDisplayName(stack));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return StackUtil.getOrCreateNbtData(stack).getBoolean("active");
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean isDebug) {
        DecimalFormat formatter = new DecimalFormat("###,###", new DecimalFormatSymbols(Locale.ROOT));
        tooltip.add(FormattedTranslator.AQUA.format("tooltips.energy.storage.info", formatter.format(AdvUtils.getCharge(stack)), formatter.format(this.getMaxCharge(stack))));
        tooltip.add(FormattedTranslator.LIGHT_PURPLE.format("tooltips.energy.tier.info", this.getTier(stack), AdvUtils.getDisplayTier(this.getTier(stack))));
        super.addInformation(stack, player, tooltip, isDebug);
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
                    if (hotbarStack != null && hotbarStack.getItem() instanceof ItemAdvBattery) {
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
                            if (hotbarElectricItem.getTier(stack) <= ((IElectricItem) batteryStack.getItem()).getTier(stack) && canProvideEnergy(batteryStack)) {
                                int transfer = ElectricItem.manager.charge(hotbarTool, Integer.MAX_VALUE, Integer.MAX_VALUE, true, true);
                                transfer = ElectricItem.manager.discharge(batteryStack, transfer, Integer.MAX_VALUE, true, false);
                                if (transfer > 0) {
                                    ElectricItem.manager.charge(hotbarTool, transfer, Integer.MAX_VALUE, true, false);
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
    public String getUnlocalizedName(ItemStack stack) {
        return "item." + this.name;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        String textureFolder = "batteries";
        for(int index = 0; index < this.textures.length; ++index) {
            this.textures[index] = iconRegister.registerIcon("AdvancedMachines:" + textureFolder + "/" + this.name + "/" + index);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int meta) {
        if (meta <= 1) {
            return this.textures[4];
        } else {
            return meta >= this.getMaxDamage() - 1 ? this.textures[0] : this.textures[3 - 3 * (meta - 2) / (this.getMaxDamage() - 4 + 1)];
        }
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(int id, CreativeTabs tabs, List itemList) {
        AdvUtils.addChargeVariants(this, itemList);
    }

    @Override
    public boolean canProvideEnergy(ItemStack stack) {
        return StackUtil.getOrCreateNbtData(stack).getBoolean("active");
    }

    @Override
    public int getChargedItemId(ItemStack itemStack) {
        return this.itemID;
    }

    @Override
    public int getEmptyItemId(ItemStack itemStack) {
        return this.itemID;
    }

    @Override
    public int getMaxCharge(ItemStack itemStack) {
        return this.maxCharge;
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return this.tier;
    }

    @Override
    public int getTransferLimit(ItemStack itemStack) {
        return this.transfer;
    }
}
