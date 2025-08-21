package ic2.advancedmachines;

import ic2.advancedmachines.utils.Refs;
import mods.vintage.core.helpers.ConfigHelper;
import mods.vintage.core.platform.config.ItemBlockID;
import mods.vintage.core.platform.lang.LocalizationProvider;
import net.minecraftforge.common.Configuration;

@LocalizationProvider
public class AdvancedMachinesConfig extends Configuration {

    public static final String SOUND_CAT = "Machine Sounds";
    public static final String SOUND_CAT_DESC = "Sound files to use on operation. Remember to use '/' instead of backslashes and the Sound directory starts on ic2/sounds. Set empty to disable.";

    @LocalizationProvider.List(modId = Refs.ID)
    public static String[] LANGS;

    public static ItemBlockID ADV_UPGRADE_ID = ItemBlockID.ofItem("advancedUpgradeId", 29776);
    public static ItemBlockID GLOWTRONIC_CRYSTAL_ID = ItemBlockID.ofItem("glowtronicCrystalId", 29777);
    public static ItemBlockID UNIVERSAL_CRYSTAL_ID = ItemBlockID.ofItem("universalCrystalId", 29778);
    public static ItemBlockID PLASMA_CRYSTAL_ID = ItemBlockID.ofItem("plasmaCrystalId", 29779);
    public static ItemBlockID COMPONENT_ID = ItemBlockID.ofItem("componentId", 29780);

    public static ItemBlockID ADV_MACHINE_ID = ItemBlockID.ofBlock("advancedMachineBlockId", 188);
    public static ItemBlockID ADV_ENERGY_BLOCK_ID = ItemBlockID.ofBlock("advancedEnergyBlockId", 189);

    public static String MACERATOR_WORK_SOUND;
    public static String COMPRESSOR_WORK_SOUND;
    public static String EXTRACTOR_WORK_SOUND;
    public static String INDUCTION_WORK_SOUND;
    public static String RECYCLER_WORK_SOUND;
    public static String INTERRUPT_SOUND;

    public static boolean SEASONAL_IC2;

    public AdvancedMachinesConfig() {
        super(ConfigHelper.getConfigFileFor("AdvancedMachines"));
        load();
        LANGS = ConfigHelper.getLocalizations(this, new String[] {"en_US"}, Refs.ID);

        MACERATOR_WORK_SOUND = ConfigHelper.getString(this, SOUND_CAT, SOUND_CAT_DESC, "maceratorWorkSound", "Machines/MaceratorOp.ogg", "Macerator Work Sound");
        COMPRESSOR_WORK_SOUND = ConfigHelper.getString(this, SOUND_CAT, SOUND_CAT_DESC, "compressorWorkSound", "Machines/CompressorOp.ogg", "Compressor Work Sound");
        EXTRACTOR_WORK_SOUND = ConfigHelper.getString(this, SOUND_CAT, SOUND_CAT_DESC, "extractorWorkSound", "Machines/ExtractorOp.ogg", "Extractor Work Sound");
        INDUCTION_WORK_SOUND = ConfigHelper.getString(this, SOUND_CAT, SOUND_CAT_DESC, "inductionWorkSound", "Machines/Induction Furnace/InductionLoop.ogg", "Induction Work Sound");
        RECYCLER_WORK_SOUND = ConfigHelper.getString(this, SOUND_CAT, SOUND_CAT_DESC, "recyclerWorkSound", "Machines/RecyclerOp.ogg", "Recycler Work Sound");
        INTERRUPT_SOUND = ConfigHelper.getString(this, SOUND_CAT, SOUND_CAT_DESC, "interruptSound", "Machines/InterruptOne.ogg", "Interrupt Work Sound");

        SEASONAL_IC2 = ConfigHelper.getBoolean(this, "general", "Set this to false if your minecraft crashes when attempting to spawn IC2 seasonal mobs.", "seasonalIC2", false, "Control IC2 seasonal mobs spawn.");

        if (this.hasChanged()) this.save();
    }
}
