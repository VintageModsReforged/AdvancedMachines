package ic2.advancedmachines.blocks.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.blocks.container.ContainerChargedElectrolyzer;
import ic2.advancedmachines.blocks.tiles.machines.TileEntityChargedElectrolyzer;
import ic2.advancedmachines.utils.Refs;
import mods.vintage.core.helpers.LangHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiAdvancedElectrolyzer extends GuiContainer {

    TileEntityChargedElectrolyzer tile;

    public GuiAdvancedElectrolyzer(ContainerChargedElectrolyzer container) {
        super(container);
        this.tile = container.tile;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(tile.getInvName(), this.xSize / 2 - this.fontRenderer.getStringWidth(tile.getInvName()) / 2, 6, 4210752);
        this.fontRenderer.drawString(LangHelper.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(Refs.getGuiPath("charged"));
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
        if (this.tile.energy > 0) {
            int i1 = this.tile.gaugeEnergyScaled(24);
            this.drawTexturedModalRect(j + 79, k + 34, 176, 14, i1 + 1, 16);
        }
    }
}
