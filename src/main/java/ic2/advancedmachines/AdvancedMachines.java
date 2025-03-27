package ic2.advancedmachines;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.LanguageRegistry;
import ic2.advancedmachines.blocks.AdvMachines;
import ic2.advancedmachines.network.AdvNetworkHandler;
import ic2.advancedmachines.network.AdvNetworkHandlerClient;
import ic2.advancedmachines.proxy.CommonProxy;
import ic2.advancedmachines.utils.Refs;
import ic2.core.IC2;
import mods.vintage.core.platform.lang.ILangProvider;
import mods.vintage.core.platform.lang.LangManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Mod(modid = Refs.ID, name = Refs.NAME, version = Refs.VERSION, acceptedMinecraftVersions = Refs.MC_VERSION, dependencies = Refs.DEPS)
@NetworkMod(clientSideRequired = true,
        clientPacketHandlerSpec = @NetworkMod.SidedPacketHandler(channels = { Refs.ID }, packetHandler = AdvNetworkHandlerClient.class),
        serverPacketHandlerSpec = @NetworkMod.SidedPacketHandler(channels = { Refs.ID }, packetHandler = AdvNetworkHandler.class))
public class AdvancedMachines implements ILangProvider {

    @SidedProxy(clientSide = Refs.PROXY_CLIENT, serverSide = Refs.PROXY_COMMON)
    public static CommonProxy proxy;

    @SidedProxy(clientSide = Refs.NETWORK_CLIENT, serverSide = Refs.NETWORK_COMMON)
    public static AdvNetworkHandler network;

    public static Logger LOGGER = Logger.getLogger(Refs.ID);

    public static CreativeTabs ADV_TAB = new CreativeTabs(Refs.ID) {
        @Override
        public ItemStack getIconItemStack() {
            return AdvMachines.MACERATOR.STACK;
        }
    };

    public AdvancedMachines() {
        LOGGER.setParent(FMLLog.getLogger());
    }

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent e) {
        proxy.preInit(e);
        LangManager.THIS.registerLangProvider(this);
        LangManager.THIS.loadCreativeTabName(Refs.ID, Refs.NAME);
    }

    @Mod.Init
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
        if (AdvancedMachinesConfig.SEASONAL_IC2 != IC2.seasonal) {
            LOGGER.info(String.format("Setting IC2.seasonal to %s, previously was %s.", AdvancedMachinesConfig.SEASONAL_IC2, IC2.seasonal));
            IC2.seasonal = AdvancedMachinesConfig.SEASONAL_IC2;
        }
    }

    @Override
    public String getModid() {
        return Refs.ID;
    }

    @Override
    public List<String> getLocalizationList() {
        return Arrays.asList(AdvancedMachinesConfig.LANGS);
    }
}
