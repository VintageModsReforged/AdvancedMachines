package ic2.advancedmachines.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.AdvancedMachines;
import ic2.advancedmachines.AdvancedMachinesConfig;
import ic2.advancedmachines.utils.Refs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemComponent extends Item {

    public String[] names = new String[] {
            "magnet.chunk",
            "magnet.dead",
            "magnet.component",
            "circuit.complex",
            "iridium.core"
    };

    public int textureIndex;

    public ItemComponent(int textureIndex) {
        super(AdvancedMachinesConfig.COMPONENT_ID);
        this.setHasSubtypes(true);
        this.setItemName("component");
        this.setCreativeTab(AdvancedMachines.ADV_TAB);
        this.setTextureFile(Refs.ITEMS);
        this.textureIndex = textureIndex;
    }

    @Override
    public int getIconFromDamage(int damage) {
        return this.textureIndex + damage;
    }

    @Override
    public String getItemNameIS(ItemStack stack) {
        int damage = stack.getItemDamage();
        return "item." + names[damage];
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
