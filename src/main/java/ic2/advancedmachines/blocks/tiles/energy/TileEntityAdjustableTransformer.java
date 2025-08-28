package ic2.advancedmachines.blocks.tiles.energy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.AdvancedMachines;
import ic2.advancedmachines.blocks.tiles.container.ContainerAdjustableTransformer;
import ic2.advancedmachines.network.IGuiListener;
import ic2.advancedmachines.utils.MovingAverage;
import ic2.advancedmachines.utils.Refs;
import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileSourceEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.core.ContainerIC2;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TileEntityAdjustableTransformer extends TileEntityBlock implements IEnergySink, IEnergySource, IHasGui, IGuiListener {

    public byte[] sideSettings = {0, 0, 0, 0, 0, 0}; // DOWN, UP, NORTH, SOUTH, WEST, EAST

    protected int maxInput = 32768;
    public int energyBuffer = 0;
    public int energyReceived = 0;

    public int outputRate = 32;
    public int packetSize = 32;
    public int energyCap = 32;

    public MovingAverage outputTracker = new MovingAverage(12);
    public MovingAverage inputTracker = new MovingAverage(12);

    protected boolean initialized = false;

    public TileEntityAdjustableTransformer() {
        super();
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        outputRate = tagCompound.getInteger("outputRate");
        packetSize = tagCompound.getInteger("packetSize");
        energyBuffer = tagCompound.getInteger("energyBuffer");
        if (packetSize > Refs.AT_MAX_PACKET) packetSize = Refs.AT_MAX_PACKET;
        if (packetSize < Refs.AT_MIN_PACKET) packetSize = Refs.AT_MIN_PACKET;
        if (outputRate > packetSize * Refs.AT_PACKETS_TICK) outputRate = packetSize * Refs.AT_PACKETS_TICK;
        if (outputRate > Refs.AT_MAX_OUTPUT) outputRate = Refs.AT_MAX_OUTPUT;
        if (outputRate < Refs.AT_MIN_OUTPUT) outputRate = Refs.AT_MIN_OUTPUT;
        if (energyBuffer > packetSize * Refs.AT_PACKETS_TICK) energyBuffer = packetSize * Refs.AT_PACKETS_TICK;
        energyCap = Math.max(packetSize, outputRate);

        NBTTagList nbttaglist = tagCompound.getTagList("SideSettings");
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound entry = (NBTTagCompound) nbttaglist.tagAt(i);
            if (i < sideSettings.length) {
                sideSettings[i] = (byte) (entry.getByte("Flags") & 255);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("outputRate", outputRate);
        tagCompound.setInteger("packetSize", packetSize);
        tagCompound.setInteger("energyBuffer", energyBuffer);

        NBTTagList nbttaglist = new NBTTagList();
        for (byte sideSetting : sideSettings) {
            NBTTagCompound entry = new NBTTagCompound();
            entry.setByte("Flags", sideSetting);
            nbttaglist.appendTag(entry);
        }
        tagCompound.setTag("SideSettings", nbttaglist);
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating()) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.initialized = true;
        }
    }

    @Override
    public void onUnloaded() {
        if (IC2.platform.isSimulating() && this.initialized) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.initialized = false;
        }
        super.onUnloaded();
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        int energySent = 0;
        if (!receivingRedstoneSignal()) {
            if (energyReceived > outputRate) energyReceived -= outputRate;
            else energyReceived = 0;

            if (energyBuffer >= packetSize) {
                boolean packetSent;
                do {
                    packetSent = false;
                    EnergyTileSourceEvent sourceEvent = new EnergyTileSourceEvent(this, packetSize);
                    MinecraftForge.EVENT_BUS.post(sourceEvent);
                    final int surplus = sourceEvent.amount;

                    if (surplus < packetSize) { // If any of it was consumed...
                        packetSent = true;
                        final int amountSent = packetSize - surplus;
                        energySent += amountSent;
                        energyBuffer -= amountSent;
                    }
                } // Repeat until output failed, or not enough EU for one packet, or rate limit reached
                while (packetSent && energyBuffer >= packetSize && energySent < outputRate);
            }
        }
        this.outputTracker.tick(energySent);
    }

    protected boolean receivingRedstoneSignal() {
        return worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
    }

    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return null;
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {}

    @Override
    public String getInvName() {
        return StatCollector.translateToLocal("block.advanced.transformer.adjustable.name");
    }

    @Override
    public int getInventoryStackLimit() {
        return 0;
    }

    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        if (this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this) {
            return false;
        } else {
            return entityplayer.getDistance((double) this.xCoord + 0.5, (double) this.yCoord + 0.5, (double) this.zCoord + 0.5) <= 64.0;
        }
    }

    @Override
    public void openChest() {}

    @Override
    public void closeChest() {}

    @Override
    public boolean isAddedToEnergyNet() {
        return this.initialized;
    }

    @Override
    public boolean emitsEnergyTo(TileEntity tile, Direction direction) {
        return (sideSettings[direction.toSideValue()] & 1) == 1;
    }

    @Override
    public int getMaxEnergyOutput() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getMaxSafeInput() {
        return this.maxInput;
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity tile, Direction direction) {
        return (sideSettings[direction.toSideValue()] & 1) == 0;
    }

    @Override
    public int demandsEnergy() {
        if (!receivingRedstoneSignal()) {
            final int tickAmt = Math.max(outputRate - energyReceived, 0);
            final int capAmt = Math.max(energyCap - energyBuffer, 0);
            return Math.min(tickAmt, capAmt);
        }
        return 0;
    }

    @Override
    public int injectEnergy(Direction direction, int amount) {
        if (IC2.platform.isSimulating()) {
            if (amount > maxInput) {
                IC2.explodeMachineAt(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
                if (amount <= 1) {
                    return 0;
                } else return amount - 1;
            } else {
                this.energyReceived += amount;
                this.energyBuffer += amount;
                this.inputTracker.tick(amount);
            }
        }
        return 0;
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer player) {
        return new ContainerAdjustableTransformer(player.inventory, this);
    }

    @Override
    public String getGuiClassName(EntityPlayer entityPlayer) {
        return "gui.GuiAdjustableTransformer";
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void receiveDescriptionData(int packetID, DataInputStream stream) {
        try {
            for (int i = 0; i < 6; i++) {
                sideSettings[i] = stream.readByte();
            }
        }
        catch (IOException e) {
            return;
        }
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void receiveGuiButton(int id) {
        switch (id) {
            case 0:
                packetSize += 1;
                if (packetSize > Refs.AT_MAX_PACKET) packetSize = Refs.AT_MAX_PACKET;
                break;
            case 1:
                packetSize += 10;
                if (packetSize > Refs.AT_MAX_PACKET) packetSize = Refs.AT_MAX_PACKET;
                break;
            case 2:
                packetSize += 64;
                if (packetSize == 68) packetSize = 64;
                if (packetSize > Refs.AT_MAX_PACKET) packetSize = Refs.AT_MAX_PACKET;
                break;
            case 3:
                packetSize *= 2;
                if (packetSize > Refs.AT_MAX_PACKET) packetSize = Refs.AT_MAX_PACKET;
                break;
            case 4:
                packetSize -= 1;
                if (packetSize < Refs.AT_MIN_PACKET) packetSize = Refs.AT_MIN_PACKET;
                if (outputRate > packetSize * Refs.AT_PACKETS_TICK) outputRate = packetSize * Refs.AT_PACKETS_TICK;
                break;
            case 5:
                packetSize -= 10;
                if (packetSize < Refs.AT_MIN_PACKET) packetSize = Refs.AT_MIN_PACKET;
                if (outputRate > packetSize * Refs.AT_PACKETS_TICK) outputRate = packetSize * Refs.AT_PACKETS_TICK;
                break;
            case 6:
                packetSize -= 64;
                if (packetSize < Refs.AT_MIN_PACKET) packetSize = Refs.AT_MIN_PACKET;
                if (outputRate > packetSize * Refs.AT_PACKETS_TICK) outputRate = packetSize * Refs.AT_PACKETS_TICK;
                break;
            case 7:
                packetSize /= 2;
                if (packetSize < Refs.AT_MIN_PACKET) packetSize = Refs.AT_MIN_PACKET;
                if (outputRate > packetSize * Refs.AT_PACKETS_TICK) outputRate = packetSize * Refs.AT_PACKETS_TICK;
                break;
            case 8:
                outputRate += 1;
                if (outputRate > packetSize * Refs.AT_PACKETS_TICK) outputRate = packetSize * Refs.AT_PACKETS_TICK;
                if (outputRate > Refs.AT_MAX_OUTPUT) outputRate = Refs.AT_MAX_OUTPUT;
                break;
            case 9:
                outputRate += 10;
                if (outputRate > packetSize * Refs.AT_PACKETS_TICK) outputRate = packetSize * Refs.AT_PACKETS_TICK;
                if (outputRate > Refs.AT_MAX_OUTPUT) outputRate = Refs.AT_MAX_OUTPUT;
                break;
            case 10:
                outputRate += 64;
                if (outputRate == 65) outputRate = 64;
                if (outputRate > packetSize * Refs.AT_PACKETS_TICK) outputRate = packetSize * Refs.AT_PACKETS_TICK;
                if (outputRate > Refs.AT_MAX_OUTPUT) outputRate = Refs.AT_MAX_OUTPUT;
                break;
            case 11:
                outputRate *= 2;
                if (outputRate > packetSize * Refs.AT_PACKETS_TICK) outputRate = packetSize * Refs.AT_PACKETS_TICK;
                if (outputRate > Refs.AT_MAX_OUTPUT) outputRate = Refs.AT_MAX_OUTPUT;
                break;
            case 12:
                outputRate -= 1;
                if (outputRate < Refs.AT_MIN_OUTPUT) outputRate = Refs.AT_MIN_OUTPUT;
                break;
            case 13:
                outputRate -= 10;
                if (outputRate < Refs.AT_MIN_OUTPUT) outputRate = Refs.AT_MIN_OUTPUT;
                break;
            case 14:
                outputRate -= 64;
                if (outputRate < Refs.AT_MIN_OUTPUT) outputRate = Refs.AT_MIN_OUTPUT;
                break;
            case 15:
                outputRate /= 2;
                if (outputRate < Refs.AT_MIN_OUTPUT) outputRate = Refs.AT_MIN_OUTPUT;
                break;
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
                if (initialized) MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
                initialized = false;
                sideSettings[id - 16] ^= 1;
                MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
                initialized = true;
                break;
        }
        energyCap = Math.max(packetSize, outputRate);
        final byte voltLevel = (byte) (packetSize <= 32 ? 0 : packetSize <= 128 ? 2 : packetSize <= 512 ? 4 : 6);
        for (int i = 0; i < 6; i++)
            sideSettings[i] = (byte) (sideSettings[i] & 249 | voltLevel);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public Packet getDescriptionPacket() {
        return createDescPacket();
    }

    protected Packet250CustomPayload createDescPacket() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);
        try {
            data.writeInt(0);
            data.writeInt(xCoord);
            data.writeInt(yCoord);
            data.writeInt(zCoord);
            for (int i = 0; i < 6; i++) {
                data.writeByte(sideSettings[i]);
            }
        }
        catch (IOException e) {
            AdvancedMachines.LOGGER.info(String.format("Server failed to create description packet for [%s, %s, %s]. More details: %s", xCoord, yCoord, zCoord, e));
        }
        Packet250CustomPayload packet = new Packet250CustomPayload(Refs.ID, bytes.toByteArray());
        packet.isChunkDataPacket = true;
        return packet;
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}
}
