package ic2.advancedmachines.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
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
        tooltip.add(AdvancedMachines.defaultEnergyConsume + " EU/t @ Tier 2");
    }

    @Override
    public int getMetadata(int var1) {
        return var1;
    }

    @Override
    public String getItemDisplayName(ItemStack itemStack) {
        return "§E" + super.getItemDisplayName(itemStack);
    }

    @Override
    public String getItemNameIS(ItemStack var1) {
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
    public void getSubItems(int id, CreativeTabs tabs, List list) {
        list.add(new ItemStack(this.itemID, 1, 0));
        list.add(new ItemStack(this.itemID, 1, 1));
        list.add(new ItemStack(this.itemID, 1, 2));
        list.add(new ItemStack(this.itemID, 1, 3));
    }
}
