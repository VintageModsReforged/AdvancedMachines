package ic2.advancedmachines.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemAdvancedMachine extends ItemBlock {
    public ItemAdvancedMachine(int var1) {
        super(var1);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean isDebug) {
        tooltip.add("2 EU/t @ Tier 2");
    }

    @Override
    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.uncommon;
    }

    @Override
    public int getMetadata(int var1) {
        return var1;
    }

    @Override
    public String getUnlocalizedName(ItemStack var1) {
        int var2 = var1.getItemDamage();
        switch (var2) {
            case 0:
                return "blockRotaryMacerator";
            case 1:
                return "blockSingularityCompressor";
            case 2:
                return "blockCentrifugeExtractor";
            case 3:
                return "blockAdvancedInduction";
            default:
                return null;
        }
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
        par3List.add(new ItemStack(par1, 1, 3));
    }
}
