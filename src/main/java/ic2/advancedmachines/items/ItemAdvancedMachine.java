package ic2.advancedmachines.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.utils.TextFormatter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemAdvancedMachine extends ItemBlock {
    public ItemAdvancedMachine(int var1) {
        super(var1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean isDebug) {
        tooltip.add(TextFormatter.AQUA.format("tooltips.usage.info"));
    }

    @Override
    public int getMetadata(int metadata) {
        return metadata;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.uncommon;
    }

    @Override
    public String getItemNameIS(ItemStack stack) {
        int itemDamage = stack.getItemDamage();
        switch (itemDamage) {
            case 0:
                return "block.advanced.macerator";
            case 1:
                return "block.advanced.compressor";
            case 2:
                return "block.advanced.extractor";
            case 3:
                return "block.advanced.induction";
            default:
                return null;
        }
    }

    @Override
    public void getSubItems(int id, CreativeTabs tabs, List list) {
        list.add(new ItemStack(this.itemID, 1, 0));
        list.add(new ItemStack(this.itemID, 1, 1));
        list.add(new ItemStack(this.itemID, 1, 2));
        list.add(new ItemStack(this.itemID, 1, 3));
    }
}
