package ic2.advancedmachines.utils;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

import java.util.Collections;
import java.util.List;

public class GuiRenderHelper {

    public static void drawHoveringText(List<String> lines, int mouseX, int mouseY, FontRenderer font, int guiWidth, int guiHeight, float zLevel) {
        if (lines.isEmpty()) return;

        GL11.glDisable(32826);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(2896);
        GL11.glDisable(2929);

        int maxWidth = 0;
        for (String s : lines) {
            int w = font.getStringWidth(s);
            if (w > maxWidth) maxWidth = w;
        }

        int x = mouseX + 12;
        int y = mouseY - 12;
        int height = 8 + (lines.size() > 1 ? 2 + (lines.size() - 1) * 10 : 0);

        if (x + maxWidth > guiWidth) x -= 28 + maxWidth;
        if (y + height + 6 > guiHeight) y = guiHeight - height - 6;

        // Draw background gradient
        drawGradientRect(x - 3, y - 4, x + maxWidth + 3, y - 3, -267386864, -267386864, zLevel);
        drawGradientRect(x - 3, y + height + 3, x + maxWidth + 3, y + height + 4, -267386864, -267386864, zLevel);
        drawGradientRect(x - 3, y - 3, x + maxWidth + 3, y + height + 3, -267386864, -267386864, zLevel);
        drawGradientRect(x - 4, y - 3, x - 3, y + height + 3, -267386864, -267386864, zLevel);
        drawGradientRect(x + maxWidth + 3, y - 3, x + maxWidth + 4, y + height + 3, -267386864, -267386864, zLevel);

        int border1 = 1347420415;
        int border2 = (border1 & 16711422) >> 1 | border1 & -16777216;
        drawGradientRect(x - 3, y - 3 + 1, x - 2, y + height + 2, border1, border2, zLevel);
        drawGradientRect(x + maxWidth + 2, y - 3 + 1, x + maxWidth + 3, y + height + 2, border1, border2, zLevel);
        drawGradientRect(x - 3, y - 3, x + maxWidth + 3, y - 2, border1, border1, zLevel);
        drawGradientRect(x - 3, y + height + 2, x + maxWidth + 3, y + height + 3, border2, border2, zLevel);

        // Draw text
        int textY = y;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            font.drawStringWithShadow(line, x, textY, -1);
            textY += i == 0 ? 2 + 10 : 10;
        }

        GL11.glEnable(2896);
        GL11.glEnable(2929);
        RenderHelper.enableStandardItemLighting();
        GL11.glEnable(32826);
    }

    public static void drawHoveringText(List<String> lines, int mouseX, int mouseY, FontRenderer font) {
        drawHoveringText(lines, mouseX, mouseY, font, 320, 240, 300.0F);
    }

    // Draw a gradient rectangle
    public static void drawGradientRect(int x1, int y1, int x2, int y2, int color1, int color2, float zLevel) {
        float alpha1 = (float) (color1 >> 24 & 255) / 255F;
        float red1 = (float) (color1 >> 16 & 255) / 255F;
        float green1 = (float) (color1 >> 8 & 255) / 255F;
        float blue1 = (float) (color1 & 255) / 255F;

        float alpha2 = (float) (color2 >> 24 & 255) / 255F;
        float red2 = (float) (color2 >> 16 & 255) / 255F;
        float green2 = (float) (color2 >> 8 & 255) / 255F;
        float blue2 = (float) (color2 & 255) / 255F;

        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);

        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.setColorRGBA_F(red1, green1, blue1, alpha1);
        tess.addVertex(x2, y1, zLevel);
        tess.addVertex(x1, y1, zLevel);
        tess.setColorRGBA_F(red2, green2, blue2, alpha2);
        tess.addVertex(x1, y2, zLevel);
        tess.addVertex(x2, y2, zLevel);
        tess.draw();

        GL11.glShadeModel(7424);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
    }

    public static void drawTooltip(String text, int x, int y, FontRenderer font) {
        drawHoveringText(Collections.singletonList(text), x, y, font);
    }
}
