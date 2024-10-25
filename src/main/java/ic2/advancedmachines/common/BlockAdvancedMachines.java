package ic2.advancedmachines.common;

import ic2.advancedmachines.common.tiles.TileEntityAdvancedInduction;
import ic2.advancedmachines.common.tiles.TileEntityCentrifugeExtractor;
import ic2.advancedmachines.common.tiles.TileEntityRotaryMacerator;
import ic2.advancedmachines.common.tiles.TileEntitySingularityCompressor;
import ic2.advancedmachines.common.tiles.base.TileEntityAdvancedMachine;
import ic2.api.Items;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.core.IC2;
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
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class BlockAdvancedMachines extends BlockContainer {

    public static final int[][] sideAndFacingToSpriteOffset = new int[][]{{3, 2, 0, 0, 0, 0}, {2, 3, 1, 1, 1, 1}, {1, 1, 3, 2, 5, 4}, {0, 0, 2, 3, 4, 5}, {4, 5, 4, 5, 3, 2}, {5, 4, 5, 4, 2, 3}};

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

    public int[][] sprites;
    private final int idWrench;
    private final int idEWrench;

    public BlockAdvancedMachines(int id) {
        super(id, Material.iron);
        this.setBlockName("blockAdvMachine");
        this.setHardness(2.0F);
        this.setStepSound(soundMetalFootstep);
        this.sprites = new int[][]{{86, 20, 86, 19, 86, 21, 86, 19}, {86, 26, 86, 27, 86, 26, 86, 28}, {86, 86, 24, 22, 86, 86, 25, 23}};
        this.blockIndexInTexture = this.sprites[0][0];
        idWrench = Items.getItem("wrench").itemID;
        idEWrench = Items.getItem("electricWrench").itemID;
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
        TileEntity var8 = world.getBlockTileEntity(x, y, z);
        if (var8 instanceof IInventory) {
            IInventory var9 = (IInventory) var8;

            for (int i = 0; i < var9.getSizeInventory(); ++i) {
                ItemStack var11 = var9.getStackInSlot(i);
                if (var11 != null) {
                    drops.add(var11);
                    var9.setInventorySlotContents(i, null);
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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9) {
        if (entityPlayer.isSneaking()) {
            return false;
        }

        if (entityPlayer.getCurrentEquippedItem() != null
                && (entityPlayer.getCurrentEquippedItem().itemID == idWrench || entityPlayer.getCurrentEquippedItem().itemID == idEWrench)) {
            TileEntityAdvancedMachine team = (TileEntityAdvancedMachine) world.getBlockTileEntity(x, y, z);
            if (team != null) {
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(team));
                team.invalidate();
                team.setActive(false);
                return true;
            }
        } else {
            entityPlayer.openGui(AdvancedMachines.INSTANCE, 0, world, x, y, z);
            return true;
        }
        return false;
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
