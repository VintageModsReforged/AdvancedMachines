package ic2.advancedmachines.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.utils.Refs;
import ic2.api.Items;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.BlockMultiID;
import ic2.core.block.TileEntityBlock;
import ic2.core.util.StackUtil;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class BlockAdvancedBlock extends BlockContainer {

    public static final int[][] sideAndFacingToSpriteOffset = BlockMultiID.sideAndFacingToSpriteOffset;

    public BlockAdvancedBlock(int id) {
        super(id, Material.iron);
        this.setBlockName("blockAdvanced");
        this.setHardness(2.0F);
        this.setStepSound(soundMetalFootstep);
        this.setCreativeTab(IC2.tabIC2);
    }

    public abstract TileEntity createTileEntity(World world, int metadata);

    @Override
    public String getTextureFile() {
        return Refs.BLOCKS;
    }

    @Override
    public int idDropped(int meta, Random random, int i) {
        return Items.getItem("advancedMachine").itemID;
    }

    @Override
    public int damageDropped(int par1) {
        return Items.getItem("advancedMachine").getItemDamage();
    }

    @Override
    public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int side) {
        TileEntity te = iblockaccess.getBlockTileEntity(i, j, k);
        int facing = te instanceof TileEntityBlock ? ((TileEntityBlock)te).getFacing() : 0;
        int meta = iblockaccess.getBlockMetadata(i, j, k);
        return isActive(iblockaccess, i, j, k) ? meta + (sideAndFacingToSpriteOffset[side][facing] + 6) * 16 : meta + sideAndFacingToSpriteOffset[side][facing] * 16;
    }

    public static boolean isActive(IBlockAccess iblockaccess, int i, int j, int k) {
        TileEntity te = iblockaccess.getBlockTileEntity(i, j, k);
        return te instanceof TileEntityBlock && ((TileEntityBlock) te).getActive();
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta) {
        return meta + sideAndFacingToSpriteOffset[side][3] * 16;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float a, float b, float c) {
        if (entityPlayer.isSneaking()) {
            return false;
        } else {
            TileEntity te = world.getBlockTileEntity(x, y, z);
            if (te instanceof IHasGui) {
                return IC2.platform.isRendering() || IC2.platform.launchGui(entityPlayer, (IHasGui) te);
            } else {
                return false;
            }
        }
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
    public void breakBlock(World world, int x, int y, int z, int a, int b) {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof TileEntityBlock) {
            ((TileEntityBlock)te).onBlockBreak(a, b);
        }
        boolean firstItem = true;
        List<ItemStack> drops = this.getBlockDropped(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
        for (ItemStack drop : drops) {
            if (firstItem) {
                firstItem = false;
            } else StackUtil.dropAsEntity(world, x, y, z, drop);
        }
        super.breakBlock(world, x, y, z, a, b);
    }

    @Override
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
        if (IC2.platform.isSimulating()) {
            TileEntityBlock te = (TileEntityBlock)world.getBlockTileEntity(i, j, k);
            if (entityliving == null) {
                te.setFacing((short)2);
            } else {
                int lookSide = MathHelper.floor_double((double)(entityliving.rotationYaw * 4.0F / 360.0F) + 0.5) & 3;
                switch (lookSide) {
                    case 0:
                        te.setFacing((short) 2);
                        break;
                    case 1:
                        te.setFacing((short) 5);
                        break;
                    case 2:
                        te.setFacing((short)3);
                        break;
                    case 3:
                        te.setFacing((short)4);
                }
            }

        }
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(int id, CreativeTabs tabs, List itemList) {
        for(int i = 0; i < 16; ++i) {
            ItemStack is = new ItemStack(this, 1, i);
            if (Item.itemsList[this.blockID].getItemNameIS(is) != null) {
                itemList.add(is);
            }
        }
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return new ItemStack(this, 1, world.getBlockMetadata(x, y, z));
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return null;
    }
}
