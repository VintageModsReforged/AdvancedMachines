package ic2.advancedmachines.blocks;

import ic2.advancedmachines.blocks.tiles.*;
import ic2.core.block.machine.tileentity.TileEntityMachine;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

public class BlockAdvMachine extends BlockAdvancedBlock {

    // default meta values
    public static final int MACERATOR = 0;
    public static final int COMPRESSOR = 1;
    public static final int EXTRACTOR = 2;
    public static final int INDUCTION = 3;
    public static final int RECYCLER = 4;
    public static final int ELECTROLYZER = 5;
    public static final int RARE_EARTH = 6;

    public BlockAdvMachine(int id) {
        super(id);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return getBlockEntity(metadata);
    }

    public TileEntityMachine getBlockEntity(int meta) {
        switch (meta) {
            case MACERATOR: return new TileEntityRotaryMacerator();
            case COMPRESSOR: return new TileEntitySingularityCompressor();
            case EXTRACTOR: return new TileEntityCentrifugeExtractor();
            case INDUCTION: return new TileEntityAdvancedInduction();
            case RECYCLER: return new TileEntityCompactingRecycler();
            case ELECTROLYZER: return new TileEntityChargedElectrolyzer();
            default: return null;
        }
    }

    /**
     * Get the block's damage value (for use with pick block).
     */
    @Override
    public int getDamageValue(World world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z); // advanced machine item meta exactly equals the block meta
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        int blockMetadata = world.getBlockMetadata(x, y, z);
        if ((blockMetadata == MACERATOR || blockMetadata == COMPRESSOR) && isActive(world, x, y, z)) {
            float xPos = (float) x + 1.0F;
            float yPos = (float) y + 1.0F;
            float zPos = (float) z + 1.0F;
            for (int i = 0; i < 4; ++i) {
                float xOffset = -0.2F - random.nextFloat() * 0.6F;
                float yOffset = -0.1F + random.nextFloat() * 0.2F;
                float zOffset = -0.2F - random.nextFloat() * 0.6F;
                world.spawnParticle("smoke", xPos + xOffset, yPos + yOffset, zPos + zOffset, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
