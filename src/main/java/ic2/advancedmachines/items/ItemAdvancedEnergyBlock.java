package ic2.advancedmachines.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
            "transformer.iv"
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
