/*******************************************************************************
 * Copyright (c) 2012-2013 Yancarlo Ramsey and CJ Bowman
 * Licensed as open source with restrictions. Please see attached LICENSE.txt.
 ******************************************************************************/
package ic2.advancedmachines.utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class CustomSmallButton extends GuiButton {
    /**
     * Path to custom texture for button
     */
    protected String texture;

    protected int uLoc;
    protected int vLoc;
    protected int uHoverLoc;
    protected int vHoverLoc;
    protected int color;
    protected int hoverColor;
    protected boolean isHovering;

    /**
     * CButton will assume the texture size is equal to the width and height of the button
     *
     * @param id         - ID of button
     * @param xLoc       - x location of button on screen
     * @param yLoc       - y location of button on screen
     * @param width      - width of button
     * @param height     - height of button
     * @param uLoc       - x location start of texture in the texture file
     * @param vLoc       - y location start of texture in the texture file
     * @param uHoverLoc  - x location start of texture for mouse over in the texture file
     * @param vHoverLoc  - x location start of texture for mouse over in the texture file
     * @param text       - text to display on button
     * @param color      - color for the text
     * @param hoverColor - color for the text while hovering
     * @param texture    - path to the texture file
     */
    public CustomSmallButton(int id, int xLoc, int yLoc, int width, int height, int uLoc, int vLoc, int uHoverLoc, int vHoverLoc, String text, int color, int hoverColor, String texture) {
        super(id, xLoc, yLoc, width, height, text);
        this.enabled = true;
        this.drawButton = true;
        this.id = id;
        this.xPosition = xLoc;
        this.yPosition = yLoc;
        this.width = width;
        this.height = height;
        this.uLoc = uLoc;
        this.vLoc = vLoc;
        this.uHoverLoc = uHoverLoc;
        this.vHoverLoc = vHoverLoc;
        this.displayString = text;
        this.color = color;
        this.hoverColor = hoverColor;
        this.texture = texture;
    }

    /**
     * Draws this button to the screen.
     */
    @Override
    public void drawButton(Minecraft mc, int xLoc, int yLoc) {
        if (drawButton) {
            FontRenderer fr = mc.fontRenderer;
            if (texture != null) {
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                int textureIndex = mc.renderEngine.getTexture(texture);
                mc.renderEngine.bindTexture(textureIndex);

            }
            isHovering = xLoc >= xPosition && yLoc >= yPosition && xLoc < xPosition + width && yLoc < yPosition + height;
            int hoverState = this.getHoverState(isHovering);
            if (hoverState == 2) {
                this.drawTexturedModalRect(xPosition, yPosition, uHoverLoc, vHoverLoc, width, height);
            } else {
                this.drawTexturedModalRect(xPosition, yPosition, uLoc, vLoc, width, height);
            }
            int renderColor = color;
            if (!enabled) {
                renderColor = -6250336;
            } else if (isHovering) {
                renderColor = hoverColor;
            }
            fr.drawString(displayString, xPosition + (width - fr.getStringWidth(displayString)) / 2, yPosition + (height - 7) / 2, renderColor);
        }
    }
}
