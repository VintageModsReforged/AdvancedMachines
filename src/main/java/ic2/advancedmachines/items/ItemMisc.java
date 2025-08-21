package ic2.advancedmachines.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.AdvancedMachines;
import ic2.advancedmachines.AdvancedMachinesConfig;
import mods.vintage.core.platform.config.IItemBlockIDProvider;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import java.util.List;

public class ItemMisc extends Item implements IItemBlockIDProvider {

    public String[] names = new String[] {
            "magnet.chunk",
            "magnet.dead",
            "magnet.component",
            "circuit.complex",
            "iridium.core"
    };

    public Icon[] textures = new Icon[names.length];

    public ItemMisc() {
        super(AdvancedMachinesConfig.COMPONENT_ID.get());
        this.setHasSubtypes(true);
        this.setCreativeTab(AdvancedMachines.ADV_TAB);
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

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister registry) {
        for (int i = 0; i < names.length; i++) {
            textures[i] = registry.registerIcon("AdvancedMachines:misc/" + names[i]);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIconFromDamage(int meta) {
        return this.textures[meta];
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int damage = stack.getItemDamage();
        return "item." + names[damage];
    }
}
