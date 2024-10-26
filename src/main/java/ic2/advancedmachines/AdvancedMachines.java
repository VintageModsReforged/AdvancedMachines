package ic2.advancedmachines;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import ic2.advancedmachines.proxy.CommonProxy;

@Mod(modid = Refs.ID, name = Refs.NAME, version = Refs.VERSION, acceptedMinecraftVersions = Refs.MC_VERSION, dependencies = Refs.DEPS)
@NetworkMod(clientSideRequired = true)
public class AdvancedMachines {

    @SidedProxy(clientSide = Refs.PROXY_CLIENT, serverSide = Refs.PROXY_COMMON)
    public static CommonProxy proxy;

    public static AdvancedMachines INSTANCE;

    public AdvancedMachines() {}

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent e) {
        INSTANCE = this;
        proxy.preInit(e);
    }

    @Mod.Init
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
}
