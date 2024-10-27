package ic2.advancedmachines.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import ic2.advancedmachines.items.ItemAdvancedMachine;
import ic2.advancedmachines.utils.Refs;
import net.minecraftforge.client.MinecraftForgeClient;

import java.util.Map;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        Map<Integer, String> metaNames = ItemAdvancedMachine.META_NAMES;
        for (Integer gui : metaNames.keySet()) {
            String guiTexture = Refs.getGuiPathFor(metaNames.get(gui));
            MinecraftForgeClient.preloadTexture(guiTexture);
        }
        MinecraftForgeClient.preloadTexture(Refs.BLOCKS);
        MinecraftForgeClient.preloadTexture(Refs.ITEMS);
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }
}
