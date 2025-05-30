package ic2.advancedmachines.blocks.gui;

import ic2.advancedmachines.blocks.container.ContainerAdvancedElectricBlock;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedEnergyBlock;
import ic2.core.block.wiring.GuiElectricBlock;

public class GuiAdvancedElectricBlock extends GuiElectricBlock {

    ContainerAdvancedElectricBlock container;

    public GuiAdvancedElectricBlock(ContainerAdvancedElectricBlock container) {
        super(container);
        this.container = container;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.fontRenderer.drawString(container.tileEntity.getInvName(), this.xSize / 2 - this.fontRenderer.getStringWidth(container.tileEntity.getInvName()) / 2, 6, 4210752);
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        if (this.isPointInRegion(152, 4, 20, 20, mouseX, mouseY)) {
            this.drawCreativeTabHoveringText(((TileEntityAdvancedEnergyBlock)this.container.tileEntity).getMode(), mouseX - x, mouseY - y);
        }
    }
}
