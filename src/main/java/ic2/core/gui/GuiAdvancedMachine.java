package ic2.core.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.blocks.tiles.machines.TileEntityCentrifugeExtractor;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.advancedmachines.blocks.tiles.container.ContainerAdvancedMachine;
import mods.vintage.core.platform.lang.Translator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiAdvancedMachine extends GuiContainer {

    TileEntityAdvancedMachine TILE;
    String texture;

    public GuiAdvancedMachine(ContainerAdvancedMachine container, String texture) {
        super(container);
        this.TILE = container.TILE;
        this.texture = texture;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int yPos = TILE instanceof TileEntityCentrifugeExtractor ? 4 : 6;
        this.fontRenderer.drawString(TILE.invName, this.xSize / 2 - this.fontRenderer.getStringWidth(TILE.invName) / 2, yPos, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
        this.fontRenderer.drawString(TILE.getSpeedName(), 6, 36, 4210752);
        this.fontRenderer.drawString(this.TILE.printFormattedData(), 10, 44, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        int texture = this.mc.renderEngine.getTexture(this.texture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(texture);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        int progress;
        if (this.TILE.energy > 0) {
            progress = this.TILE.gaugeFuelScaled(14);
            this.drawTexturedModalRect(x + 56, y + 36 + 14 - progress, 176, 14 - progress, 14, progress);
        }

        progress = this.TILE.gaugeProgressScaled(24);
        this.drawTexturedModalRect(x + 79, y + 34, 176, 14, progress + 1, 16);
    }
}
