package ic2.advancedmachines.blocks.tiles.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.blocks.tiles.energy.TileEntityAdjustableTransformer;
import ic2.core.ContainerIC2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerAdjustableTransformer extends ContainerIC2 {

    public TileEntityAdjustableTransformer TILE;
    public int outputRate = -1;
    public int packetSize = -1;
    public byte[] sideSettings = {0, 0, 0, 0, 0, 0}; // DOWN, UP, NORTH, SOUTH, WEST, EAST
    public int outputAvg = -1;
    public int inputAvg = -1;
    public int energyBuffer = -1;

    public ContainerAdjustableTransformer(InventoryPlayer playerInv, TileEntityAdjustableTransformer tile) {
        this.TILE = tile;
    }

    @Override
    public void detectAndSendChanges() {
        final int syncOutAvg = (int) (TILE.outputTracker.getAverage() * 100);
        final int syncInAvg = (int) (TILE.inputTracker.getAverage() * 100);

        for (Object object : crafters) {
            ICrafting crafter = (ICrafting) object;

            if (this.outputRate != TILE.outputRate) {
                crafter.sendProgressBarUpdate(this, 0, TILE.outputRate & 65535);
                crafter.sendProgressBarUpdate(this, 1, TILE.outputRate >>> 16);
            }

            if (this.packetSize != TILE.packetSize) {
                crafter.sendProgressBarUpdate(this, 2, TILE.packetSize & 65535);
                crafter.sendProgressBarUpdate(this, 3, TILE.packetSize >>> 16);
            }

            for (int i = 0; i < 6; i++)
                if (this.sideSettings[i] != TILE.sideSettings[i]) {
                    crafter.sendProgressBarUpdate(this, 4 + i, TILE.sideSettings[i]);
                }

            if (outputAvg != syncOutAvg) {
                crafter.sendProgressBarUpdate(this, 10, syncOutAvg & 65535);
                crafter.sendProgressBarUpdate(this, 11, syncOutAvg >>> 16);
            }

            if (inputAvg != syncInAvg) {
                crafter.sendProgressBarUpdate(this, 12, syncInAvg & 65535);
                crafter.sendProgressBarUpdate(this, 13, syncInAvg >>> 16);
            }

            if (this.energyBuffer != TILE.energyBuffer) {
                crafter.sendProgressBarUpdate(this, 14, TILE.energyBuffer & 65535);
                crafter.sendProgressBarUpdate(this, 15, TILE.energyBuffer >>> 16);
            }
        }

        // Done sending updates, record the new current values
        this.outputRate = TILE.outputRate;
        this.packetSize = TILE.packetSize;
        for (int i = 0; i < 6; i++) {
            this.sideSettings[i] = TILE.sideSettings[i];
        }
        outputAvg = syncOutAvg;
        inputAvg = syncInAvg;
        this.energyBuffer = TILE.energyBuffer;
    }

    @Override
    public int guiInventorySize() {
        return 0;
    }

    @Override
    public int getInput() {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int param, int value) {
        switch (param) {
            case 0:
                TILE.outputRate = TILE.outputRate & -65536 | value;
                break;
            case 1:
                TILE.outputRate = TILE.outputRate & 65535 | (value << 16);
                break;
            case 2:
                TILE.packetSize = TILE.packetSize & -65536 | value;
                break;
            case 3:
                TILE.packetSize = TILE.packetSize & 65535 | (value << 16);
                break;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                TILE.sideSettings[param - 4] = (byte) value;
                break;
            case 10:
                outputAvg = outputAvg & -65536 | value;
                break;
            case 11:
                outputAvg = outputAvg & 65535 | (value << 16);
                break;
            case 12:
                inputAvg = inputAvg & -65536 | value;
                break;
            case 13:
                inputAvg = inputAvg & 65535 | (value << 16);
                break;
            case 14:
                TILE.energyBuffer = TILE.energyBuffer & -65536 | value;
                break;
            case 15:
                TILE.energyBuffer = TILE.energyBuffer & 65535 | (value << 16);
                break;
            default:
                System.out.println(this.getClass() + "#updateProgressBar(II) returns default case!");
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.TILE.isUseableByPlayer(player);
    }
}
