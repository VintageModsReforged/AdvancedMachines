package ic2.advancedmachines.utils;

public class Refs {

    public static final String ID = "AdvancedMachines";
    public static final String NAME = "IC2: Advanced Machines";

    public static final String PROXY_COMMON = "ic2.advancedmachines.proxy.CommonProxy";
    public static final String PROXY_CLIENT = "ic2.advancedmachines.proxy.ClientProxy";

    public static String getGuiPath(String machine) {
        return "/mods/AdvancedMachines/textures/gui/" + machine + ".png";
    }
}
