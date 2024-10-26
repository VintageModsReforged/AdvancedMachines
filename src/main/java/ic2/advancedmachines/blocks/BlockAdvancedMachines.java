package ic2.advancedmachines.blocks;

import ic2.advancedmachines.BlocksItems;
import ic2.advancedmachines.Refs;
import ic2.advancedmachines.blocks.tiles.TileEntityAdvancedInduction;
import ic2.advancedmachines.blocks.tiles.TileEntityCentrifugeExtractor;
import ic2.advancedmachines.blocks.tiles.TileEntityRotaryMacerator;
import ic2.advancedmachines.blocks.tiles.TileEntitySingularityCompressor;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.api.Items;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.BlockMultiID;
import ic2.core.block.TileEntityBlock;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class BlockAdvancedMachines extends BlockContainer {

    public static final int[][] sideAndFacingToSpriteOffset = BlockMultiID.sideAndFacingToSpriteOffset;

    public enum AdvMachines{
        MACERATOR(0),
        COMPRESSOR(1),
        EXTRACTOR(2),
        INDUCTION(3);

        public ItemStack STACK;

        AdvMachines(int meta) {
            this.STACK = new ItemStack(BlocksItems.ADVANCED_MACHINE_BLOCK, 1, meta);
        }
    }

    public BlockAdvancedMachines(int id) {
        super(id, Material.iron);
        this.setBlockName("blockAdvMachine");
        this.setHardness(2.0F);
        this.setStepSound(soundMetalFootstep);
        this.setCreativeTab(IC2.tabIC2);
    }

    @Override
    public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int side) {
        TileEntity te = iblockaccess.getBlockTileEntity(i, j, k);
        int facing = te instanceof ic2.core.block.TileEntityBlock ? ((TileEntityBlock)te).getFacing() : 0;
        int meta = iblockaccess.getBlockMetadata(i, j, k);
        return isActive(iblockaccess, i, j, k) ? meta + (sideAndFacingToSpriteOffset[side][facing] + 6) * 16 : meta + sideAndFacingToSpriteOffset[side][facing] * 16;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int blockSide, int metaData) {
        return metaData + sideAndFacingToSpriteOffset[blockSide][3] * 16;
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return null;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return getBlockEntity(meta);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> drops = super.getBlockDropped(world, x, y, z, metadata, fortune);
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity instanceof IInventory) {
            IInventory inventory = (IInventory) tileEntity;

            for (int i = 0; i < inventory.getSizeInventory(); ++i) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (stack != null) {
                    drops.add(stack);
                    inventory.setInventorySlotContents(i, null);
                }
            }
        }

        return drops;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
        boolean firstItem = true;
        for (Iterator<ItemStack> stackIterator = this.getBlockDropped(world, x, y, z, world.getBlockMetadata(x, y, z), 0).iterator(); stackIterator.hasNext(); firstItem = false) {
            ItemStack stack = stackIterator.next();
            if (!firstItem) {
                if (stack == null) {
                    return;
                }

                double offset = 0.7D;
                double xOffset = (double) world.rand.nextFloat() * offset + (1.0D - offset) * 0.5D;
                double yOffset = (double) world.rand.nextFloat() * offset + (1.0D - offset) * 0.5D;
                double zOffset = (double) world.rand.nextFloat() * offset + (1.0D - offset) * 0.5D;
                EntityItem drop = new EntityItem(world, (double) x + xOffset, (double) y + yOffset, (double) z + zOffset, stack);
                drop.delayBeforeCanPickup = 10;
                world.spawnEntityInWorld(drop);
                return;
            }
        }
    }

    @Override
    public int idDropped(int meta, Random random, int amount) {
        return Items.getItem("advancedMachine").itemID;
    }

    /**
     * Get the block's damage value (for use with pick block).
     */
    @Override
    public int getDamageValue(World world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z); // advanced machine item meta exactly equals the block meta
    }

    @Override
    public String getTextureFile() {
        return Refs.BLOCKS;
    }

    public TileEntityAdvancedMachine getBlockEntity(int meta) {
        switch (meta) {
            case 0:
                return new TileEntityRotaryMacerator();
            case 1:
                return new TileEntitySingularityCompressor();
            case 2:
                return new TileEntityCentrifugeExtractor();
            case 3:
                return new TileEntityAdvancedInduction();
            default:
                return null;
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving player) {
        if (IC2.platform.isSimulating()) {
            int heading = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            TileEntityAdvancedMachine te = (TileEntityAdvancedMachine) world.getBlockTileEntity(x, y, z);
            switch (heading) {
                case 0:
                    te.setFacing((short) 2);
                    break;
                case 1:
                    te.setFacing((short) 5);
                    break;
                case 2:
                    te.setFacing((short) 3);
                    break;
                case 3:
                    te.setFacing((short) 4);
                    break;
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float a, float b, float c) {
        if (entityPlayer.isSneaking()) {
            return false;
        } else {
            TileEntity te = world.getBlockTileEntity(x, y, z);
            if (te instanceof IHasGui) {
                return IC2.platform.isSimulating() ? IC2.platform.launchGui(entityPlayer, (IHasGui)te) : true;
            } else {
                return false;
            }
        }
    }

    public static boolean isActive(IBlockAccess world, int x, int y, int z) {
        return ((TileEntityAdvancedMachine) world.getBlockTileEntity(x, y, z)).getActive();
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        int blockMetadata = world.getBlockMetadata(x, y, z);
        if ((blockMetadata == 0 || blockMetadata == 1) && isActive(world, x, y, z)) {
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
