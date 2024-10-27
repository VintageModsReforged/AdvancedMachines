package ic2.advancedmachines.blocks.tiles.base;

import ic2.core.block.wiring.TileEntityElectricBlock;

public class TileEntityAdvancedEnergyBlock extends TileEntityElectricBlock {
    public TileEntityAdvancedEnergyBlock(int tierc, int outputc, int maxStoragec) {
        super(tierc, outputc, maxStoragec);
    }

    @Override
    public String getInvName() {
        return "";
    }
}
