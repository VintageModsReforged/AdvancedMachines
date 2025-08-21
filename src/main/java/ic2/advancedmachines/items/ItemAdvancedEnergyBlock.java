package ic2.advancedmachines.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.vintage.core.platform.lang.Translator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemAdvancedEnergyBlock extends ItemBlock  {

    public static String[] names = new String[] {
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
    public String getItemDisplayName(ItemStack stack) {
        Translator format;
        int meta = stack.getItemDamage();
        if (meta == 0 || meta == 3 || meta == 5) {
            format = Translator.YELLOW;
        } else if (meta == 1 || meta == 4) {
            format = Translator.LIGHT_PURPLE;
        } else format = Translator.AQUA;
        return format.literal(super.getItemDisplayName(stack));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.uncommon;
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int itemDamage = stack.getItemDamage();
        return "block.advanced." + names[itemDamage];
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(int par1, CreativeTabs tabs, List list) {
        for(int i = 0; i < names.length; ++i) {
            ItemStack is = new ItemStack(this, 1, i);
            list.add(is);
        }
    }
}
