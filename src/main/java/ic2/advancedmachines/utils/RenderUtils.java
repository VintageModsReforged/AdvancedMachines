package ic2.advancedmachines.utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;

public class RenderUtils {

    private static final int MASKR = 0xFF0000;
    private static final int MASKG = 0x00FF00;
    private static final int MASKB = 0x0000FF;

    private static final int[] oX = new int[]{0, -1, 0, 1};
    private static final int[] oY = new int[]{-1, 0, 1, 0};

    /**
     * Individually multiply R, G, B color components by scalar value to dim or brighten the color.
     * Does not check for overflow. Beware when using values over 1.0F.
     *
     * @param color            original color
     * @param brightnessFactor should be positive and <> 1.0F
     * @return modified color
     */
    public static int multiplyColorComponents(int color, float brightnessFactor) {
        return ((int) (brightnessFactor * (color & MASKR)) & MASKR)
                | ((int) (brightnessFactor * (color & MASKG)) & MASKG)
                | ((int) (brightnessFactor * (color & MASKB)) & MASKB);
    }

    @SideOnly(Side.CLIENT)
    public static void drawCenteredText(FontRenderer fr, String text, int xLoc, int yLoc, int color) {
        fr.drawString(text, xLoc - fr.getStringWidth(text) / 2, yLoc, color);
    }

    @SideOnly(Side.CLIENT)
    public static void drawRightAlignedText(FontRenderer fr, String text, int xLoc, int yLoc, int color) {
        fr.drawString(text, xLoc - fr.getStringWidth(text), yLoc, color);
    }

    @SideOnly(Side.CLIENT)
    public static void drawLeftAlignedText(FontRenderer fr, String text, int xLoc, int yLoc, int color) {
        fr.drawString(text, xLoc, yLoc, color);
    }

    @SideOnly(Side.CLIENT)
    public static void drawRightAlignedGlowingText(FontRenderer fr, String text, int xLoc, int yLoc, int color, int glowColor) {
        drawGlowingText(fr, text, xLoc - fr.getStringWidth(text), yLoc, color, glowColor);
    }

    @SideOnly(Side.CLIENT)
    public static void drawCenteredGlowingText(FontRenderer fr, String text, int xLoc, int yLoc, int color, int glowColor) {
        drawGlowingText(fr, text, xLoc - fr.getStringWidth(text) / 2, yLoc, color, glowColor);
    }

    @SideOnly(Side.CLIENT)
    public static void drawGlowingText(FontRenderer fr, String text, int xLoc, int yLoc, int color, int glowColor) {
        for(int i = 0; i < 4; ++i) {
            fr.drawString(text, xLoc + oX[i], yLoc + oY[i], glowColor);
        }
        fr.drawString(text, xLoc, yLoc, color);
    }
}
