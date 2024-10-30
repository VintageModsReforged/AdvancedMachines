package ic2.advancedmachines.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import ic2.advancedmachines.utils.Refs;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {

    public String[] GUIS = new String[] {
            "compressor",
            "extractor",
            "induction",
            "macerator",
            "recycler",
            "electric",
            "charged"
    };

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        for (String gui : GUIS) {
            MinecraftForgeClient.preloadTexture(Refs.getGuiPath(gui));
        }
        MinecraftForgeClient.preloadTexture(Refs.BLOCK_MACHINES);
        MinecraftForgeClient.preloadTexture(Refs.BLOCK_ELECTRIC);
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
