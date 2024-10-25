package ic2.advancedmachines.common.tiles.base;

import ic2.api.IWrenchable;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.network.NetworkHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class TileEntityBlock extends TileEntity implements IWrenchable, INetworkDataProvider, INetworkTileEntityEventListener {
    protected boolean created = false;
    public boolean active = false;
    public short facing = 5;
    public boolean prevActive = false;
    public short prevFacing = 0;
    public static List<String> networkedFields;

    public TileEntityBlock() {
        if (networkedFields == null) {
            networkedFields = new ArrayList<String>();
            networkedFields.add("active");
            networkedFields.add("facing");
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.prevFacing = this.facing = tagCompound.getShort("facing");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setShort("facing", this.facing);
    }

    @Override
    public void updateEntity() {
        if (!this.created) {
            this.created = true;
            NetworkHelper.requestInitialData(this);
            NetworkHelper.announceBlockUpdate(worldObj, xCoord, yCoord, zCoord);
        }
    }

    public boolean getActive() {
        return this.active;
    }

    public void setActive(boolean flag) {
        active = flag;
        if (prevActive != active) {
            NetworkHelper.updateTileEntityField(this, "active");
        }
        prevActive = flag;
    }

    public void setActiveWithoutNotify(boolean active) {
        this.active = active;
        this.prevActive = active;
    }

    @Override
    public short getFacing() {
        return this.facing;
    }

    @Override
    public boolean wrenchCanSetFacing(EntityPlayer player, int facingToSet) {
        return facingToSet >= 2 // Top or Bottom
                && facingToSet != facing; // dismantle upon clicking the face
    }

    @Override
    public void setFacing(short facing) {
        this.facing = facing;
        NetworkHelper.updateTileEntityField(this, "facing");
        this.prevFacing = facing;
        NetworkHelper.announceBlockUpdate(worldObj, xCoord, yCoord, zCoord);
    }

    @Override
    public boolean wrenchCanRemove(EntityPlayer player) {
        return true;
    }

    @Override
    public float getWrenchDropRate() {
        return 1.0F;
    }

    @Override
    public ItemStack getWrenchDrop(EntityPlayer player) {
        return new ItemStack(this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord), 1, this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord));
    }

    @Override
    public List<String> getNetworkedFields() {
        return networkedFields;
    }

    @Override
    public void onNetworkEvent(int event) {}
}
