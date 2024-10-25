package ic2.advancedmachines.utils;

import cpw.mods.fml.common.registry.LanguageRegistry;
import ic2.advancedmachines.AdvancedMachinesConfig;
import net.minecraft.util.StatCollector;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class LangHelper {

    public LangHelper() {
        throw new UnsupportedOperationException();
    }

    public static void init() {
        try {
            String[] LANGS = AdvancedMachinesConfig.LANGS;
            if (LANGS.length == 0) {
                throw new IOException("Something went wrong when reading langs from config file! Please regenerate the config or retort to mod author if this doesn't fix itself!");
            } else {
                for (String lang : LANGS) {
                    loadLanguage(lang);
                }
            }
        } catch (Throwable ignored) {}
    }

    public static void loadLanguage(String lang) {
        InputStream stream = null;
        InputStreamReader reader = null;
        try {
            stream = LangHelper.class.getResourceAsStream("/mods/advancedmachines/lang/" + lang + ".lang");
            if (stream == null) {
                throw new IOException("Couldn't load language file.");
            }

            reader = new InputStreamReader(stream, "UTF-8");
            Properties props = new Properties();
            props.load(reader);
            for (String key : props.stringPropertyNames()) {
                LanguageRegistry.instance().addStringLocalization(key, lang, props.getProperty(key));
            }

        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Throwable ignored) {
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (Throwable ignored) {
                }
            }
        }
    }

    public static String format(String key, Object... args) {
        return StatCollector.translateToLocalFormatted(key, args);
    }

    public static String format(boolean b) {
        return format(b ? "true" : "false");
    }
}
