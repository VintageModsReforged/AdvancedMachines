package ic2.advancedmachines.client;

import ic2.advancedmachines.common.AdvancedMachines;
import ic2.advancedmachines.common.ContainerInduction;
import ic2.advancedmachines.common.TileEntityAdvancedInduction;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;

public class GuiAdvInduction extends GuiContainer {

    public TileEntityAdvancedInduction tile;

    public GuiAdvInduction(InventoryPlayer playerInv, TileEntityAdvancedInduction tile) {
        super(new ContainerInduction(playerInv, tile));
        this.tile = tile;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(AdvancedMachines.advIndName, this.xSize / 2 - this.fontRenderer.getStringWidth(AdvancedMachines.advIndName) / 2, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
        this.fontRenderer.drawString("Heat:", 6, 36, 4210752);
        this.fontRenderer.drawString(this.tile.printFormattedData(), 10, 44, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        int texture = this.mc.renderEngine.getTexture("/ic2/advancedmachines/client/sprites/GUIInduction.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(texture);
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        int var7;
        if (this.tile.energy > 0) {
            var7 = this.tile.gaugeFuelScaled(14);
            this.drawTexturedModalRect(var5 + 56, var6 + 36 + 14 - var7, 176, 14 - var7, 14, var7);
        }

        var7 = this.tile.gaugeProgressScaled(24);
        this.drawTexturedModalRect(var5 + 79, var6 + 34, 176, 14, var7 + 1, 16);
    }
}
