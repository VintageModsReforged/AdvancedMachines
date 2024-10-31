package ic2.advancedmachines.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.blocks.tiles.energy.*;
import ic2.advancedmachines.utils.Refs;
import ic2.core.IC2;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.wiring.TileEntityElectricBlock;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import java.util.List;

public class BlockAdvEnergyBlock extends BlockAdvancedBlock {

    // default meta values
    public static final int ESU = 0;
    public static final int ISU = 1;
    public static final int PESU = 2;
    public static final int EV = 3;
    public static final int IV = 4;
    public static final int ADJUSTABLE = 5;

    private final int textureIndexIN;
    private final int textureIndexOUT;

    public BlockAdvEnergyBlock(int id) {
        super(id);

        this.textureIndexIN = ADJUSTABLE;
        this.textureIndexOUT = ADJUSTABLE + 16;
    }

    @Override
    public String getTextureFile() {
        return Refs.BLOCK_ELECTRIC;
    }

    @Override
    public int getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == ADJUSTABLE) {
            TileEntity tile = world.getBlockTileEntity(x, y, z);
            final byte flags = ((TileEntityAdjustableTransformer)tile).sideSettings[side];
            return (flags & 1) == 0 ? this.textureIndexIN : this.textureIndexOUT;
        } else return super.getBlockTexture(world, x, y, z, side);
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
        if (metadata == ADJUSTABLE) {
            return this.textureIndexIN;
        } else return super.getBlockTextureFromSideAndMetadata(side, metadata);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return getBlockEntity(metadata);
    }

    public TileEntityBlock getBlockEntity(int meta) {
        switch (meta) {
            case ESU: return new TileEntityESU();
            case ISU: return new TileEntityISU();
            case PESU: return new TileEntityPESU();
            case EV: return new TileEntityTranformerEV();
            case IV: return new TileEntityTranformerIV();
            case ADJUSTABLE: return new TileEntityAdjustableTransformer();
            default: return null;
        }
    }

    @Override
    public boolean isProvidingWeakPower(IBlockAccess iblockaccess, int x, int y, int z, int side) {
        TileEntity te = iblockaccess.getBlockTileEntity(x, y, z);
        if (te instanceof TileEntityElectricBlock) {
            TileEntityElectricBlock electricBlock = (TileEntityElectricBlock) te;
            return electricBlock.isEmittingRedstone();
        } else return false;
    }

    @Override
    public boolean isBlockNormalCube(World world, int i, int j, int k) {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return true;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess world, int i, int j, int k, int l) {
        return true;
    }

    @Override
    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side) {
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityliving) {
        int meta = world.getBlockMetadata(x, y, z);
        if (IC2.platform.isSimulating()) {
            if (meta == ADJUSTABLE) {
                super.onBlockPlacedBy(world, x, y, z, entityliving);
            } else {
                TileEntityBlock te = (TileEntityBlock) world.getBlockTileEntity(x, y, z);
                if (entityliving == null) {
                    te.setFacing((short) 1);
                } else {
                    int yaw = MathHelper.floor_double((double) (entityliving.rotationYaw * 4.0F / 360.0F) + 0.5) & 3;
                    int pitch = Math.round(entityliving.rotationPitch);
                    if (pitch >= 65) {
                        te.setFacing((short) 1);
                    } else if (pitch <= -65) {
                        te.setFacing((short) 0);
                    } else {
                        switch (yaw) {
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
                        }
                    }
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
}
