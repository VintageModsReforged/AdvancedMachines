package ic2.advancedmachines.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemAdvancedEnergyBlock extends ItemBlock {

    public static Map<Integer, String> META_NAMES = new HashMap<Integer, String>();

    public ItemAdvancedEnergyBlock(int id) {
        super(id);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);

        META_NAMES.put(0, "esu");
        META_NAMES.put(1, "isu");
        META_NAMES.put(2, "pesu");
//        META_NAMES.put(3, "transformer.ev");
//        META_NAMES.put(4, "transformer.iv");
//        META_NAMES.put(5, "transformer.adj");
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean isDebug) {

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
