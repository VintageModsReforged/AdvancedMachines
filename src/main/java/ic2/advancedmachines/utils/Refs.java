package ic2.advancedmachines.utils;

import ic2.advancedmachines.blocks.tiles.TileEntityAdvancedInduction;
import ic2.advancedmachines.blocks.tiles.TileEntityCentrifugeExtractor;
import ic2.advancedmachines.blocks.tiles.TileEntityRotaryMacerator;
import ic2.advancedmachines.blocks.tiles.TileEntitySingularityCompressor;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;

public class Refs {

    public static final String ID = "AdvancedMachines";
    public static final String NAME = "IC2 Advanced Machines Addon";
    public static final String VERSION = "4.7g";
    public static final String MC_VERSION = "[1.4.7]";
    public static final String DEPS = "required-after:IC2";

    public static final String PROXY_COMMON = "ic2.advancedmachines.proxy.CommonProxy";
    public static final String PROXY_CLIENT = "ic2.advancedmachines.proxy.ClientProxy";

    public static final String GUI_EXTRACTOR = "/mods/advancedmachines/textures/gui/extractor.png";
    public static final String GUI_INDUCTION = "/mods/advancedmachines/textures/gui/induction.png";
    public static final String GUI_MACERATOR = "/mods/advancedmachines/textures/gui/macerator.png";
    public static final String GUI_COMPRESSOR = "/mods/advancedmachines/textures/gui/compressor.png";
    public static final String BLOCKS = "/mods/advancedmachines/textures/blocks.png";
    public static final String ITEMS = "/mods/advancedmachines/textures/items.png";

    public static String getTextureName(TileEntityAdvancedMachine tile) {
        if (tile instanceof TileEntityAdvancedInduction) {
            return GUI_INDUCTION;
        } else if (tile instanceof TileEntityCentrifugeExtractor) {
            return GUI_EXTRACTOR;
        } else if (tile instanceof TileEntityRotaryMacerator) {
            return GUI_MACERATOR;
        } else if (tile instanceof TileEntitySingularityCompressor){
            return GUI_COMPRESSOR;
        } else {
            return "";
        }
    }
}
