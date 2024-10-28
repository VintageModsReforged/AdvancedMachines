package ic2.advancedmachines.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.utils.TextFormatter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemAdvancedMachine extends ItemBlock {

    public static Map<Integer, String> META_NAMES = new HashMap<Integer, String>();

    public ItemAdvancedMachine(int id) {
        super(id);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);

        META_NAMES.put(0, "macerator");
        META_NAMES.put(1, "compressor");
        META_NAMES.put(2, "extractor");
        META_NAMES.put(3, "induction");
        META_NAMES.put(4, "recycler");
        META_NAMES.put(5, "electrolyzer");
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean isDebug) {
        int itemDamage = stack.getItemDamage();
        if (itemDamage == 5) {
            tooltip.add(TextFormatter.AQUA.format("tooltips.transfer.info"));
        } else tooltip.add(TextFormatter.AQUA.format("tooltips.usage.info"));
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
        if (META_NAMES.containsKey(itemDamage)) {
            return "block.advanced." + META_NAMES.get(itemDamage);
        } else return null;
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(int id, CreativeTabs tabs, List itemList) {
        for(int i = 0; i < 16; ++i) {
            ItemStack is = new ItemStack(this, 1, i);
            if (Item.itemsList[this.itemID].getItemNameIS(is) != null) {
                itemList.add(is);
            }
        }
    }
}
