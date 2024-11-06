package ic2.advancedmachines.blocks.container;

import ic2.core.block.wiring.ContainerElectricBlock;
import ic2.core.block.wiring.TileEntityElectricBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerAdvancedElectricBlock extends ContainerElectricBlock {

    public byte redstoneMode = -1;

    public ContainerAdvancedElectricBlock(EntityPlayer entityPlayer, TileEntityElectricBlock tileEntity) {
        super(entityPlayer, tileEntity);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (Object crafter : this.crafters) {
            ICrafting icrafting = (ICrafting) crafter;
            if (this.redstoneMode != this.tileEntity.redstoneMode) {
                icrafting.sendProgressBarUpdate(this, 2, this.tileEntity.redstoneMode);
            }
        }
        this.redstoneMode = this.tileEntity.redstoneMode;
    }

    @Override
    public void updateProgressBar(int index, int value) {
        super.updateProgressBar(index, value);
        if (index == 2) {
            this.tileEntity.redstoneMode = (byte) value;
        }
    }
}
