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

public class ItemAdvancedMachine extends ItemBlock {

    public String[] names = new String[] {
            "macerator",
            "compressor",
            "extractor",
            "induction",
            "recycler",
            "electrolyzer"
    };

    public ItemAdvancedMachine(int id) {
        super(id);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean isDebug) {
        int itemDamage = stack.getItemDamage();
        if (itemDamage == 5) {
            tooltip.add(FormattedTranslator.AQUA.format("tooltips.transfer.info"));
        } else tooltip.add(FormattedTranslator.AQUA.format("tooltips.usage.info"));
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
