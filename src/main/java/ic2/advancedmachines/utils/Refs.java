package ic2.advancedmachines.utils;

import ic2.advancedmachines.blocks.tiles.*;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;

public class Refs {

    public static final String ID = "AdvancedMachines";
    public static final String NAME = "IC2 Advanced Machines Addon";
    public static final String VERSION = "4.7h";
    public static final String MC_VERSION = "[1.4.7]";
    public static final String DEPS = "required-after:IC2";

    public static final String PROXY_COMMON = "ic2.advancedmachines.proxy.CommonProxy";
    public static final String PROXY_CLIENT = "ic2.advancedmachines.proxy.ClientProxy";

    public static final String BLOCKS = "/mods/advancedmachines/textures/blocks.png";
    public static final String ITEMS = "/mods/advancedmachines/textures/items.png";

    public static String getGuiPathFor(String machine) {
        return "/mods/advancedmachines/textures/gui/" + machine + ".png";
    }

    public static String getTextureName(TileEntityAdvancedMachine tile) {
        if (tile instanceof TileEntityAdvancedInduction) {
            return getGuiPathFor("induction");
        } else if (tile instanceof TileEntityCentrifugeExtractor) {
            return getGuiPathFor("extractor");
        } else if (tile instanceof TileEntityRotaryMacerator) {
            return getGuiPathFor("macerator");
        } else if (tile instanceof TileEntitySingularityCompressor){
            return getGuiPathFor("compressor");
        } else  if (tile instanceof TileEntityCompactedRecycler) {
            return getGuiPathFor("recycler");
        } else return "";
    }
}
