package ic2.advancedmachines.utils;

public class AdvUtils {

    public static String getDisplayTier(int tier) {
        switch (tier) {
            case 1: return "LV";
            case 2: return "MV";
            case 3: return "HV";
            case 4: return "EV";
            case 5: return "IV";
            case 6: return "LuV";
            default: return String.valueOf(tier);
        }
    }
}
