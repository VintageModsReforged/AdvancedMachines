package ic2.advancedmachines.items;

import ic2.core.IC2;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

public class ItemBase extends Item {

    public String name;

    public ItemBase(int id, String name) {
        super(id);
        this.setUnlocalizedName(name);
        this.setCreativeTab(IC2.tabIC2);
        this.name = name;
    }

    @Override
    public void registerIcons(IconRegister registry) {
        this.itemIcon = registry.registerIcon("advancedmachines:" + name);
    }
}
