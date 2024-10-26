package ic2.advancedmachines.blocks.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.blocks.tiles.TileEntityCentrifugeExtractor;
import ic2.advancedmachines.blocks.container.ContainerAdvancedMachine;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.advancedmachines.utils.LangHelper;
import ic2.advancedmachines.utils.Refs;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiAdvancedMachine extends GuiContainer {

    TileEntityAdvancedMachine TILE;

    public GuiAdvancedMachine(InventoryPlayer playerInv, TileEntityAdvancedMachine tile) {
        super(new ContainerAdvancedMachine(playerInv.player, tile));
        this.TILE = tile;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int yPos = TILE instanceof TileEntityCentrifugeExtractor ? 4 : 6;
        this.fontRenderer.drawString(TILE.invName, this.xSize / 2 - this.fontRenderer.getStringWidth(TILE.invName) / 2, yPos, 4210752);
        this.fontRenderer.drawString(LangHelper.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
        this.fontRenderer.drawString(TILE.getSpeedName(), 6, 36, 4210752);
        this.fontRenderer.drawString(this.TILE.printFormattedData(), 10, 44, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(Refs.getTextureName(TILE));
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
