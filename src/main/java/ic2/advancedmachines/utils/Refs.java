package ic2.advancedmachines.utils;

public class Refs {

    public static final String ID = "AdvancedMachines";
    public static final String NAME = "IC2 Advanced Machines Addon";

    public static final String PROXY_COMMON = "ic2.advancedmachines.proxy.CommonProxy";
    public static final String PROXY_CLIENT = "ic2.advancedmachines.proxy.ClientProxy";

    public static final String NETWORK_COMMON = "ic2.advancedmachines.network.AdvNetworkHandler";
    public static final String NETWORK_CLIENT = "ic2.advancedmachines.network.AdvNetworkHandlerClient";

    public static final String BLOCK_MACHINES = "/mods/AdvancedMachines/textures/blocks/machines.png";
    public static final String BLOCK_ELECTRIC = "/mods/AdvancedMachines/textures/blocks/electric.png";
    public static final String ITEMS = "/mods/AdvancedMachines/textures/items/items.png";

    public static final String[] KEY_DIRECTION_NAMES = {"inv.dir.down", "inv.dir.up", "inv.dir.north", "inv.dir.south", "inv.dir.west", "inv.dir.east"};

    public static String getGuiPath(String machine) {
        return "/mods/AdvancedMachines/textures/gui/" + machine + ".png";
    }

    public static final int AT_MIN_PACKET = 4;
    public static final int AT_MAX_PACKET = 2048;
    public static final int AT_MIN_OUTPUT = 1;
    public static final int AT_MAX_OUTPUT = 32768;
    public static final int AT_PACKETS_TICK = 64;
}
