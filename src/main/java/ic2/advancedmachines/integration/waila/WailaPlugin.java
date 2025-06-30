package ic2.advancedmachines.integration.waila;

import reforged.mods.blockhelper.addons.base.WailaCommonHandler;

public class WailaPlugin {

    public static void init() {
        WailaCommonHandler.INSTANCE.registerProviders(AdvancedMachinesInfoProvider.THIS);
    }
}
