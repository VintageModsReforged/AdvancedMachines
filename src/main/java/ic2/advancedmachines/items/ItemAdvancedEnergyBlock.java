package ic2.advancedmachines.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.vintage.core.platform.lang.FormattedTranslator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemAdvancedEnergyBlock extends ItemBlock {

    public String[] names = new String[] {
            "esu",
            "isu",
            "pesu",
            "transformer.ev",
            "transformer.iv",
            "transformer.adjustable"
    };

    public ItemAdvancedEnergyBlock(int id) {
        super(id);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean isDebug) {

    }

    @Override
    public String getItemDisplayName(ItemStack stack) {
        FormattedTranslator format;
        int meta = stack.getItemDamage();
        if (meta == 0 || meta == 3 || meta == 5) {
            format = FormattedTranslator.YELLOW;
        } else if (meta == 1 || meta == 4) {
            format = FormattedTranslator.LIGHT_PURPLE;
        } else format = FormattedTranslator.AQUA;
        return format.literal(super.getItemDisplayName(stack));
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
        return "block.advanced." + names[itemDamage];
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
}
