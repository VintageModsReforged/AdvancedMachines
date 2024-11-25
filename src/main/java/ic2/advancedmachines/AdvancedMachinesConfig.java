package ic2.advancedmachines;

import cpw.mods.fml.relauncher.FMLInjectionData;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;

public class AdvancedMachinesConfig {

    public static final String SOUND_CAT = "Machine Sounds";
    public static final String SOUND_CAT_DESC = "Sound files to use on operation. Remember to use '/' instead of backslashes and the Sound directory starts on ic2/sounds. Set empty to disable.";
    public static final String ITEM_IDS_CAT = "Item IDs";
    public static final String BLOCK_IDS_CAT = "Block IDs";

    public static Configuration MAIN_CONFIG;

    public static int ADV_UPGRADE_ID;
    public static int COMPONENT_ID;

    public static int GLOWTRONIC_CRYSTAL_ID;
    public static int UNIVERSAL_CRYSTAL_ID;
    public static int PLASMA_CRYSTAL_ID;

    public static int ADV_MACHINE_ID;
    public static int ADV_ENERGY_BLOCK_ID;

    public static String MACERATOR_WORK_SOUND;
    public static String COMPRESSOR_WORK_SOUND;
    public static String EXTRACTOR_WORK_SOUND;
    public static String INDUCTION_WORK_SOUND;
    public static String RECYCLER_WORK_SOUND;
    public static String INTERRUPT_SOUND;

    public static boolean SEASONAL_IC2;

    public static void init() {
        MAIN_CONFIG = new Configuration(new File((File) FMLInjectionData.data()[6], "config/AdvancedMachines.cfg"));
        MAIN_CONFIG.load();

        MACERATOR_WORK_SOUND = getString(SOUND_CAT, SOUND_CAT_DESC, "maceratorWorkSound", "Machines/MaceratorOp.ogg", "Macerator Work Sound");
        COMPRESSOR_WORK_SOUND = getString(SOUND_CAT, SOUND_CAT_DESC, "compressorWorkSound", "Machines/CompressorOp.ogg", "Compressor Work Sound");
        EXTRACTOR_WORK_SOUND = getString(SOUND_CAT, SOUND_CAT_DESC, "extractorWorkSound", "Machines/ExtractorOp.ogg", "Extractor Work Sound");
        INDUCTION_WORK_SOUND = getString(SOUND_CAT, SOUND_CAT_DESC, "inductionWorkSound", "Machines/Induction Furnace/InductionLoop.ogg", "Induction Work Sound");
        RECYCLER_WORK_SOUND = getString(SOUND_CAT, SOUND_CAT_DESC, "recyclerWorkSound", "Machines/RecyclerOp.ogg", "Recycler Work Sound");
        INTERRUPT_SOUND = getString(SOUND_CAT, SOUND_CAT_DESC, "interruptSound", "Machines/InterruptOne.ogg", "Interrupt Work Sound");

        ADV_UPGRADE_ID = getItemId("advancedUpgradeId", 29776, "");
        GLOWTRONIC_CRYSTAL_ID = getItemId("glowtronicCrystalId", 29777, "");
        UNIVERSAL_CRYSTAL_ID = getItemId("universalCrystalId", 29778, "");
        PLASMA_CRYSTAL_ID = getItemId("plasmaCrystalId", 29779, "");
        COMPONENT_ID = getItemId("componentId", 29780, "");

        ADV_MACHINE_ID = getBlockId("advancedMachineBlockId", 188, "");
        ADV_ENERGY_BLOCK_ID = getBlockId("advancedEnergyBlockId", 189, "");

        SEASONAL_IC2 = getBoolean("general", "Set this to false if your minecraft crashes when attempting to spawn IC2 seasonal mobs.", "seasonalIC2", false, "Control IC2 seasonal mobs spawn.");

        if (MAIN_CONFIG.hasChanged()) MAIN_CONFIG.save();
    }

    private static boolean getBoolean(String cat, @Nullable String tagComment, String tag, boolean defaultValue, String comment) {
        comment = comment.replace("{t}", tag) + "\n";
        Property prop = MAIN_CONFIG.get(cat, tag, defaultValue);
        prop.comment = comment + (tagComment == null ? "" : tagComment + "\n") + "Default: " + defaultValue;
        return prop.getBoolean(defaultValue);
    }

    private static String[] getStrings(String cat, @Nullable String tagComment, String tag, String[] defaultValue, String comment) {
        comment = comment.replace("{t}", tag) + "\n";
        Property prop = MAIN_CONFIG.get(cat, tag, defaultValue);
        prop.comment = comment + (tagComment == null ? "" : tagComment + "\n") + "Default: " + Arrays.toString(defaultValue);
        return prop.getStringList();
    }

    private static String getString(String cat, @Nullable String tagComment, String tag, String defaultValue, String comment) {
        comment = comment.replace("{t}", tag) + "\n";
        Property prop = MAIN_CONFIG.get(cat, tag, defaultValue);
        prop.comment = comment + (tagComment == null ? "" : tagComment + "\n") + "Default: " + defaultValue;
        return prop.getString();
    }

    private static int getBlockId(String tag, int defaultValue, String comment) {
        comment = comment.replace("{t}", tag) + "\n";
        Property prop = MAIN_CONFIG.get(BLOCK_IDS_CAT, tag, defaultValue);
        prop.comment = comment + "Default: " + defaultValue;
        int value = prop.getInt(defaultValue);
        prop.set(Integer.toString(value));
        return value;
    }

    private static int getItemId(String tag, int defaultValue, String comment) {
        comment = comment.replace("{t}", tag) + "\n";
        Property prop = MAIN_CONFIG.get(ITEM_IDS_CAT, tag, defaultValue);
        prop.comment = comment + "Default: " + defaultValue;
        int value = prop.getInt(defaultValue);
        prop.set(Integer.toString(value));
        return value;
    }
}
