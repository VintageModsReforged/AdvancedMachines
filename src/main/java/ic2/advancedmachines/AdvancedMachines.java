package ic2.advancedmachines;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.LanguageRegistry;
import ic2.advancedmachines.blocks.AdvMachines;
import ic2.advancedmachines.proxy.CommonProxy;
import ic2.advancedmachines.utils.Refs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import java.util.logging.Logger;

@Mod(modid = Refs.ID, name = Refs.NAME, version = Refs.VERSION, acceptedMinecraftVersions = Refs.MC_VERSION, dependencies = Refs.DEPS)
@NetworkMod(clientSideRequired = true)
public class AdvancedMachines {

    @SidedProxy(clientSide = Refs.PROXY_CLIENT, serverSide = Refs.PROXY_COMMON)
    public static CommonProxy proxy;

    public static Logger LOGGER = Logger.getLogger(Refs.ID);

    public static CreativeTabs ADV_TAB = new CreativeTabs(Refs.ID) {{
        LanguageRegistry.instance().addStringLocalization("itemGroup." + Refs.ID, Refs.NAME);
    }
        @Override
        public ItemStack getIconItemStack() {
            return AdvMachines.MACERATOR.STACK;
        }
    };

    public AdvancedMachines() {}

    @PreInit
    public void preInit(FMLPreInitializationEvent e) {
        proxy.preInit(e);
    }

    @Init
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
}
