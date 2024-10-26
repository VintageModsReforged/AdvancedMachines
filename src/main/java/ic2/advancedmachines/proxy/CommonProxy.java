package ic2.advancedmachines.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import ic2.advancedmachines.AdvancedMachinesConfig;
import ic2.advancedmachines.AdvancedMachinesRecipes;
import ic2.advancedmachines.BlocksItems;
import ic2.advancedmachines.utils.LangHelper;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
        AdvancedMachinesConfig.init();
        LangHelper.init();
        BlocksItems.init();
    }

    public void init(FMLInitializationEvent e) {

    }

    public void postInit(FMLPostInitializationEvent e) {
        AdvancedMachinesRecipes.init();
    }
}
