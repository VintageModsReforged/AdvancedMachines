package ic2.advancedmachines.utils;

import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.advancedmachines.blocks.tiles.machines.*;

public class Refs {

    public static final String ID = "AdvancedMachines";
    public static final String NAME = "IC2 Advanced Machines Addon";
    public static final String VERSION = "4.7.5.2b";
    public static final String MC_VERSION = "[1.5.2]";
    public static final String DEPS = "required-after:IC2";

    public static final String PROXY_COMMON = "ic2.advancedmachines.proxy.CommonProxy";
    public static final String PROXY_CLIENT = "ic2.advancedmachines.proxy.ClientProxy";

    public static String getGuiPath(String machine) {
        return "/mods/advancedmachines/textures/gui/" + machine + ".png";
    }

    public static String getTextureName(TileEntityAdvancedMachine tile) {
        if (tile instanceof TileEntityAdvancedInduction) {
            return getGuiPath("induction");
        } else if (tile instanceof TileEntityCentrifugeExtractor) {
            return getGuiPath("extractor");
        } else if (tile instanceof TileEntityRotaryMacerator) {
            return getGuiPath("macerator");
        } else if (tile instanceof TileEntitySingularityCompressor){
            return getGuiPath("compressor");
        } else if (tile instanceof TileEntityCompactingRecycler) {
            return getGuiPath("recycler");
        } else {
            return null;
        }
    }
}
