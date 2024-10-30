package ic2.core.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.blocks.tiles.machines.TileEntityChargedElectrolyzer;
import ic2.advancedmachines.blocks.tiles.container.ContainerChargedElectrolyzer;
import ic2.advancedmachines.utils.LangHelper;
import ic2.advancedmachines.utils.Refs;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiAdvElectrolyzer extends GuiContainer {

    TileEntityChargedElectrolyzer TILE;

    public GuiAdvElectrolyzer(ContainerChargedElectrolyzer container) {
        super(container);
        this.TILE = container.TILE;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(TILE.getInvName(), this.xSize / 2 - this.fontRenderer.getStringWidth(TILE.getInvName()) / 2, 6, 4210752);
        this.fontRenderer.drawString(LangHelper.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        int i = this.mc.renderEngine.getTexture(Refs.getGuiPath("charged"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i);
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
        if (this.TILE.energy > 0) {
            int i1 = this.TILE.gaugeEnergyScaled(24);
            this.drawTexturedModalRect(j + 79, k + 34, 176, 14, i1 + 1, 16);
        }
    }
}
