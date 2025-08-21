package ic2.advancedmachines;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import ic2.advancedmachines.blocks.AdvMachines;
import ic2.advancedmachines.integration.nei.NEIModule;
import ic2.advancedmachines.integration.waila.WailaPlugin;
import ic2.advancedmachines.proxy.CommonProxy;
import ic2.advancedmachines.utils.Refs;
import ic2.core.IC2;
import mods.vintage.core.platform.lang.LangManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import java.util.logging.Logger;

@Mod(modid = Refs.ID, name = Refs.NAME, dependencies = Refs.DEPS, useMetadata = true)
@NetworkMod(clientSideRequired = true)
public class AdvancedMachines {

    @SidedProxy(clientSide = Refs.PROXY_CLIENT, serverSide = Refs.PROXY_COMMON)
    public static CommonProxy proxy;

    public static Logger LOGGER = Logger.getLogger(Refs.ID);

    public static CreativeTabs ADV_TAB = new CreativeTabs(Refs.ID) {
        @Override
        public ItemStack getIconItemStack() {
            return AdvMachines.MACERATOR.STACK;
        }
    };

    public AdvancedMachines() {}

    @PreInit
    public void preInit(FMLPreInitializationEvent e) {
        proxy.preInit(e);
        LangManager.INSTANCE.loadCreativeTabName(Refs.ID, Refs.NAME);
    }

    @Init
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
        NEIModule.init();
        if (AdvancedMachinesConfig.SEASONAL_IC2 != IC2.seasonal) {
            LOGGER.info(String.format("Setting IC2.seasonal to %s, previously was %s.", AdvancedMachinesConfig.SEASONAL_IC2, IC2.seasonal));
            IC2.seasonal = AdvancedMachinesConfig.SEASONAL_IC2;
        }
        if (Loader.isModLoaded("BlockHelperAddons")) {
            WailaPlugin.init();
        }
    }
}
