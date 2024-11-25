package ic2.core.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.AdvancedMachines;
import ic2.advancedmachines.blocks.tiles.container.ContainerAdjustableTransformer;
import ic2.advancedmachines.blocks.tiles.energy.TileEntityAdjustableTransformer;
import ic2.advancedmachines.utils.RenderUtils;
import ic2.advancedmachines.utils.CustomSmallButton;
import ic2.advancedmachines.utils.Refs;
import core.helpers.LangHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;

@SideOnly(Side.CLIENT)
public class GuiAdjustableTransformer extends GuiContainer {

    ContainerAdjustableTransformer CONTAINER;
    TileEntityAdjustableTransformer tile;
    private final CustomSmallButton[] buttons = new CustomSmallButton[16];
    private final CustomSmallButton[] dirButtons = new CustomSmallButton[6];

    private int xLoc;
    private int yLoc;
    private final int yOff = 30;

    public static final String guiTexture = Refs.getGuiPath("adjustable");

    private static final String[] displayStrings = {"+1", "+10", "+64", "x2", "-1", "-10", "-64", "/2"};
    private static final int GREEN = 0x55FF55;
    private static final int GREEN_GLOW = RenderUtils.multiplyColorComponents(GREEN, 0.16F);
    private final DecimalFormat fraction = new DecimalFormat("##0.00");

    public GuiAdjustableTransformer(ContainerAdjustableTransformer container) {
        super(container);
        this.tile = container.TILE;
        this.CONTAINER = container;
        this.xSize = 240;
        this.ySize = 140;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        super.initGui();

        this.xLoc = (width - xSize) / 2;
        this.yLoc = (height - ySize) / 2;

        for (int i = 0; i < buttons.length; i++) {
            CustomSmallButton button = new CustomSmallButton(i, xLoc + 8 + 24 * (i % 4), yLoc + yOff + 33 + 13 * (i / 4) + 17 * (i / 8), 24, 13, 1, 192, 1, 207, displayStrings[i % 8], 4210752, 16777120, guiTexture);
            this.controlList.add(button);
        }
        for (int i = 0; i < dirButtons.length; i++) {
            CustomSmallButton button = new CustomSmallButton(i + 16, xLoc + 173, yLoc + yOff + 24 + 13 * i, 32, 13, 27, 192, 27, 207, LangHelper.format(Refs.KEY_DIRECTION_NAMES[i]), 4210752, 16777120, guiTexture);
            this.controlList.add(button);
        }

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int textureIndex = mc.renderEngine.getTexture(guiTexture);
        mc.renderEngine.bindTexture(textureIndex);
        // Draw GUI background graphic
        drawTexturedModalRect(xLoc, yLoc, 0, 0, xSize, ySize);
        // Draw title text
        RenderUtils.drawCenteredText(fontRenderer, tile.getInvName(), width / 2, yLoc + 6, 4210752);
        // Draw stats text
        RenderUtils.drawRightAlignedText(fontRenderer, LangHelper.format("inv.electric.avg.eu"), xLoc + 180, yLoc + 26, 4210752);
        RenderUtils.drawRightAlignedText(fontRenderer, LangHelper.format("inv.electric.avg.packet.in"), xLoc + 180, yLoc + 36, 4210752);
        RenderUtils.drawLeftAlignedText(fontRenderer, LangHelper.format("inv.electric.eu.bugger"), xLoc + 49, yLoc + 26, 4210752);

        RenderUtils.drawRightAlignedGlowingText(fontRenderer, Integer.toString(tile.energyBuffer), xLoc + 44, yLoc + 26, GREEN, GREEN_GLOW);
        // Factor of 100 because data is in fixed point (x100)
        final float outAvg = (float) (((ContainerAdjustableTransformer) inventorySlots).outputAvg) / 100F;
        final float inAvg = (float) (((ContainerAdjustableTransformer) inventorySlots).inputAvg) / 100F;
        RenderUtils.drawRightAlignedGlowingText(fontRenderer, fraction.format(outAvg), xLoc + 230, yLoc + 26, GREEN, GREEN_GLOW);
        RenderUtils.drawRightAlignedGlowingText(fontRenderer, fraction.format(inAvg), xLoc + 230, yLoc + 36, GREEN, GREEN_GLOW);

        // Packet size section text
        RenderUtils.drawCenteredText(fontRenderer, LangHelper.format("inv.electric.packet.size"), xLoc + 88, yLoc + yOff + 21, 0xB00000);
        RenderUtils.drawRightAlignedGlowingText(fontRenderer, Integer.toString(tile.packetSize), xLoc + 146, yLoc + yOff + 49, GREEN, GREEN_GLOW);
        fontRenderer.drawString("[" + Refs.AT_MIN_PACKET + " - " + Refs.AT_MAX_PACKET + "]", xLoc + 110, yLoc + yOff + 35, 4210752);
        fontRenderer.drawString(LangHelper.format("inv.eu"), xLoc + 152, yLoc + yOff + 49, 4210752);

        // Transfer rate section text
        RenderUtils.drawCenteredText(fontRenderer, LangHelper.format("inv.electric.transfer.max"), xLoc + 88, yLoc + yOff + 64, 0xB00000);
        RenderUtils.drawRightAlignedGlowingText(fontRenderer, Integer.toString(tile.outputRate), xLoc + 146, yLoc + yOff + 92, GREEN, GREEN_GLOW);
        fontRenderer.drawString("[" + Refs.AT_MIN_OUTPUT + " - " + Refs.AT_MAX_OUTPUT + "]", xLoc + 110, yLoc + yOff + 78, 4210752);
        fontRenderer.drawString(LangHelper.format("inv.eu"), xLoc + 152, yLoc + yOff + 92, 4210752);

        // Side input/output settings text
        for (int i = 0; i < 6; i++) {
            RenderUtils.drawGlowingText(fontRenderer, LangHelper.format((tile.sideSettings[i] & 1) == 0 ? "inv.in" : "inv.out"), xLoc + 214, yLoc + yOff + 27 + 13 * i, GREEN, GREEN_GLOW);
        }
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        AdvancedMachines.network.sendTileEntityGuiButtonUpdate(this.tile, guiButton.id);
        super.actionPerformed(guiButton);
    }
}
