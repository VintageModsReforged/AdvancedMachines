package ic2.advancedmachines.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.blocks.tiles.machines.TileEntityAdvancedInduction;
import ic2.advancedmachines.blocks.tiles.machines.TileEntityCentrifugeExtractor;
import ic2.advancedmachines.blocks.tiles.machines.TileEntityRotaryMacerator;
import ic2.advancedmachines.blocks.tiles.machines.TileEntitySingularityCompressor;
import ic2.core.block.TileEntityInventory;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockAdvancedMachine extends BlockAdvancedBlock {

    // default meta values
    public static final int MACERATOR = 0;
    public static final int COMPRESSOR = 1;
    public static final int EXTRACTOR = 2;
    public static final int INDUCTION = 3;
    public static final int RECYCLER = 4;
    public static final int ELECTROLYZER = 5;

    public BlockAdvancedMachine(int id) {
        super(id);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return getBlockEntity(metadata);
    }

    public TileEntityInventory getBlockEntity(int meta) {
        switch (meta) {
            case MACERATOR: return new TileEntityRotaryMacerator();
            case COMPRESSOR: return new TileEntitySingularityCompressor();
            case EXTRACTOR: return new TileEntityCentrifugeExtractor();
            case INDUCTION: return new TileEntityAdvancedInduction();
//            case RECYCLER: return new TileEntityCompactingRecycler();
//            case ELECTROLYZER: return new TileEntityChargedElectrolyzer();
            default: return null;
        }
    }

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

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(int id, CreativeTabs tabs, List itemList) {
        for(int i = 0; i < 16; ++i) {
            ItemStack is = new ItemStack(this, 1, i);
            if (Item.itemsList[this.blockID].getUnlocalizedName(is) != null) {
                itemList.add(is);
            }
        }
    }
}
