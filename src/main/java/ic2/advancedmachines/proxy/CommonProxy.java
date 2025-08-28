package ic2.advancedmachines.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import ic2.advancedmachines.AdvancedMachinesConfig;
import ic2.advancedmachines.AdvancedMachinesRecipes;
import ic2.advancedmachines.BlocksItems;
import ic2.advancedmachines.utils.Refs;
import mods.vintage.core.platform.config.ConfigHandler;
import net.minecraftforge.common.Configuration;

public class CommonProxy {

    ConfigHandler CONFIG_HANDLER = new ConfigHandler(Refs.ID);
    Configuration CONFIG;

    public void preInit(FMLPreInitializationEvent e) {
        CONFIG = new AdvancedMachinesConfig();
        CONFIG_HANDLER.initIDs(CONFIG);
    }

    public void init(FMLInitializationEvent e) {
        CONFIG_HANDLER.confirmIDs(CONFIG);
        BlocksItems.init();
        AdvancedMachinesRecipes.init();
    }

    public void postInit(FMLPostInitializationEvent e) {
        CONFIG_HANDLER.confirmOwnership(CONFIG);
    }
}
