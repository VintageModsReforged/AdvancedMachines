package ic2.advancedmachines.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.AdvancedMachines;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.advancedmachines.render.RenderAdvBlock;
import ic2.api.item.Items;
import ic2.api.tile.IWrenchable;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.BlockMultiID;
import ic2.core.block.BlockTextureStitched;
import ic2.core.block.TileEntityBlock;
import ic2.core.util.StackUtil;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureStitched;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class BlockAdvancedBlock extends BlockContainer {

    public static final int[][] sideAndFacingToSpriteOffset = BlockMultiID.sideAndFacingToSpriteOffset;
    @SideOnly(Side.CLIENT)
    protected Icon[][] textures;
    public int renderMask = 63;
    public int metaMachinesCount;

    protected BlockAdvancedBlock(int id) {
        super(id, Material.iron);
        this.setUnlocalizedName("blockAdvanced");
        this.setHardness(2.0F);
        this.setStepSound(soundMetalFootstep);
        this.setCreativeTab(AdvancedMachines.ADV_TAB);
    }

    public abstract TileEntity createTileEntity(World world, int metadata);

    public String getTextureFolder() {
        return "machines";
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister iconRegister) {
        int metaCount = metaMachinesCount;
//        for(metaCount = 0; this.getTextureName(metaCount) != null; ++metaCount) {}
        this.textures = new Icon[metaCount][12];
        String textureFolder = this.getTextureFolder() == null ? "" : this.getTextureFolder() + "/";

        for(int index = 0; index < metaCount; ++index) {
            for(int active = 0; active < 2; ++active) {
                for(int side = 0; side < 6; ++side) {
                    int subIndex = active * 6 + side;
                    String name = "advancedmachines:" + textureFolder + this.getTextureName(index) + "." + subIndex;
                    TextureStitched texture = new BlockTextureStitched(name);
                    this.textures[index][subIndex] = texture;
                    ((TextureMap)iconRegister).setTextureEntry(name, texture);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getBlockTexture(IBlockAccess iBlockAccess, int x, int y, int z, int side) {
        int facing = this.getFacing(iBlockAccess, x, y, z);
        boolean active = isActive(iBlockAccess, x, y, z);
        int meta = iBlockAccess.getBlockMetadata(x, y, z);
        int index = this.getTextureIndex(meta);
        int subIndex = this.getTextureSubIndex(side, facing, active);
        if (index >= this.textures.length) {
            return null;
        } else {
            try {
                return this.textures[index][subIndex];
            } catch (Exception e) {
                AdvancedMachines.LOGGER.info(String.format("Coordinates: [x=%s, y=%s, z=%s]. Side: %s. Block: %s. Meta: %s. Facing: %s. Active: %s. Index: %s. SubIndex: %s", x, y, z, side, this, meta, facing, active, index, subIndex));
                return null;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIcon(int side, int meta) {
        int facing = this.getFacing(meta);
        int index = this.getTextureIndex(meta);
        int subIndex = this.getTextureSubIndex(side, facing);
        if (index >= this.textures.length) {
            return null;
        } else {
            try {
                return this.textures[index][subIndex];
            } catch (Exception e) {
                IC2.platform.displayError(e, "Side: " + side + "\n" + "Block: " + this + "\n" + "Meta: " + meta + "\n" + "Facing: " + facing + "\n" + "Index: " + index + "\n" + "SubIndex: " + subIndex);
                return null;
            }
        }
    }

    @Override
    public int getRenderType() {
        return RenderAdvBlock.renderId;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return (this.renderMask & 1 << side) != 0 ? super.shouldSideBeRendered(blockAccess, x, y, z, side) : false;
    }

    @SideOnly(Side.CLIENT)
    public void onRender(IBlockAccess blockAccess, int x, int y, int z) {
        TileEntity te = blockAccess.getBlockTileEntity(x, y, z);
        if (te instanceof ic2.core.block.TileEntityBlock) {
            ((TileEntityBlock)te).onRender();
        }
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
        ArrayList<ItemStack> dropped = super.getBlockDropped(world, x, y, z, metadata, fortune);
        TileEntity var8 = world.getBlockTileEntity(x, y, z);
        if (var8 instanceof IInventory) {
            IInventory inventory = (IInventory) var8;

            for (int i = 0; i < inventory.getSizeInventory(); ++i) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (stack != null) {
                    dropped.add(stack);
                    inventory.setInventorySlotContents(i, null);
                }
            }
        }

        return dropped;
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
            } else {
                StackUtil.dropAsEntity(world, x, y, z, drop);
            }
        }
        super.breakBlock(world, x, y, z, a, b);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityliving, ItemStack itemStack) {
        if (IC2.platform.isSimulating()) {
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
            if (tileEntity instanceof IWrenchable) {
                IWrenchable te = (IWrenchable) tileEntity;
                if (entityliving == null) {
                    te.setFacing((short)2);
                } else {
                    int l = MathHelper.floor_double((double)(entityliving.rotationYaw * 4.0F / 360.0F) + 0.5) & 3;
                    switch (l) {
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

    @Override
    public void onSetBlockIDWithMetaData(World world, int x, int y, int z, int meta) {
        super.onSetBlockIDWithMetaData(world, x, y, z, meta);
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof TileEntityBlock) {
            TileEntityBlock teb = (TileEntityBlock)te;
            if (teb.loaded) {
                teb.onUnloaded();
            }
        }
    }

    @Override
    public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis) {
        if (axis != ForgeDirection.UNKNOWN) {
            TileEntity tileEntity = worldObj.getBlockTileEntity(x, y, z);
            if (tileEntity instanceof IWrenchable) {
                IWrenchable te = (IWrenchable) tileEntity;
                int newFacing = ForgeDirection.getOrientation(te.getFacing()).getRotation(axis).ordinal();
                if (te.wrenchCanSetFacing((EntityPlayer) null, newFacing)) {
                    te.setFacing((short) newFacing);
                }
            }

        }
        return false;
    }

    @Override
    public int idDropped(int meta, Random random, int i) {
        return Items.getItem("advancedMachine").itemID;
    }

    @Override
    public int damageDropped(int par1) {
        return Items.getItem("advancedMachine").getItemDamage();
    }

    /**
     * Get the block's damage value (for use with pick block).
     */
    @Override
    public int getDamageValue(World world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z); // advanced machine item meta exactly equals the block meta
    }

    public final int getTextureSubIndex(int side, int facing) {
        return this.getTextureSubIndex(side, facing, false);
    }

    public final int getTextureSubIndex(int side, int facing, boolean active) {
        int ret = sideAndFacingToSpriteOffset[side][facing];
        return active ? ret + 6 : ret;
    }

    public static boolean isActive(IBlockAccess iblockaccess, int i, int j, int k) {
        TileEntity te = iblockaccess.getBlockTileEntity(i, j, k);
        return te instanceof TileEntityBlock && ((TileEntityBlock) te).getActive();
    }

    public int getFacing(int meta) {
        return 3;
    }

    public int getFacing(IBlockAccess iBlockAccess, int x, int y, int z) {
        TileEntity te = iBlockAccess.getBlockTileEntity(x, y, z);
        if (te instanceof TileEntityAdvancedMachine) {
            return ((TileEntityAdvancedMachine) te).getFacing();
        } else {
            int meta = iBlockAccess.getBlockMetadata(x, y, z);
            return this.getFacing(meta);
        }
    }

    public String getTextureName(int index) {
        Item item = Item.itemsList[this.blockID];
        if (!item.getHasSubtypes()) {
            return index == 0 ? this.getUnlocalizedName() : null;
        } else {
            ItemStack itemStack = new ItemStack(this, 1, index);
            String ret = item.getUnlocalizedName(itemStack);
            return ret == null ? null : ret.replace("item", "block");
        }
    }

    public int getTextureIndex(int meta) {
        return meta;
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return null;
    }
}
