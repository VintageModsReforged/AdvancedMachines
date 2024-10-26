package ic2.advancedmachines.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import ic2.advancedmachines.Refs;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        MinecraftForgeClient.preloadTexture(Refs.GUI_EXTRACTOR);
        MinecraftForgeClient.preloadTexture(Refs.GUI_INDUCTION);
        MinecraftForgeClient.preloadTexture(Refs.GUI_MACERATOR);
        MinecraftForgeClient.preloadTexture(Refs.GUI_COMPRESSOR);
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
