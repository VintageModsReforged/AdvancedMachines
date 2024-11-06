package ic2.advancedmachines.blocks.tiles.base;

import ic2.advancedmachines.AdvancedMachinesConfig;
import ic2.advancedmachines.BlocksItems;
import ic2.advancedmachines.blocks.container.ContainerAdvancedMachine;
import ic2.advancedmachines.items.upgrades.ISimpleUpgrade;
import ic2.advancedmachines.utils.IStackFilter;
import ic2.advancedmachines.utils.InvAdvSlotUpgrade;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.network.NetworkHelper;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.audio.AudioSource;
import ic2.core.audio.PositionSpec;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotProcessable;
import ic2.core.block.machine.tileentity.TileEntityElectricMachine;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class TileEntityAdvancedMachine extends TileEntityElectricMachine implements IHasGui, INetworkTileEntityEventListener {

    public static final int maxProgress = 4000;
    public static final int maxEnergy = 5000;
    public static final int maxSpeed = 10000;
    public static final int energyUsage = 15;

    public short speed;
    public String invName;
    public List<InvSlotProcessable> inputs;
    public List<InvSlotOutput> outputs;
    public short progress;
    public String speedFormat = "%s%%";
    public int soundTicker;
    public AudioSource audioSource;
    private static final int eventStart = 0;
    private static final int eventInterrupt = 1;
    private static final int eventStop = 2;
    public InvAdvSlotUpgrade upgradeSlot;
    public IStackFilter inputFilter;

    private ItemStack cachedOutput = null;
    private boolean outputLocked = false;

    public TileEntityAdvancedMachine(String invName, int upgradeSlotStartIndex, IStackFilter inputFilter) {
        super(maxEnergy, 2, 0);
        this.soundTicker = IC2.random.nextInt(64);
        this.invName = invName;
        this.inputs = new ArrayList<InvSlotProcessable>();
        this.outputs = new ArrayList<InvSlotOutput>();
        this.speed = 0;
        this.progress = 0;
        this.upgradeSlot = new InvAdvSlotUpgrade(this, "upgrade", upgradeSlotStartIndex, 2);
        this.inputFilter = inputFilter;
        this.addSlots();
    }

    public abstract void addSlots();

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.speed = tagCompound.getShort("speed");
        this.progress = tagCompound.getShort("progress");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setShort("speed", this.speed);
        tagCompound.setShort("progress", this.progress);
    }

    @Override
    public String getInvName() {
        return this.invName;
    }

    @Override
    public void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IC2.audioManager.removeSources(this);
            this.audioSource = null;
        }
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        boolean needsInvUpdate = false;

        boolean wasActive = this.getActive();
        boolean newActive = false;

        if (this.speed == 0) {
            newActive = false;
        }

        if (this.progress >= maxProgress) {
            this.operate();
            needsInvUpdate = true;
            this.progress = 0;
        }

        boolean canOperate = this.canOperate();
        if (energy > 0 && (canOperate || isRedstonePowered())) {
            int energyConsume = canOperate ? 15 : 1;
            if (speed < maxSpeed) {
                ++this.speed;
            } else {
                speed = (short) maxSpeed;
            }
            energy -= energyConsume;
            newActive = true;
            if (!wasActive) {
                NetworkHelper.initiateTileEntityEvent(this, eventStart, true); // Start sound only when first becoming active
            }
        } else {
            boolean wasWorking = speed != 0;
            speed = (short) (speed - Math.min(speed, 4));
            if (wasWorking && speed == 0) {
                NetworkHelper.initiateTileEntityEvent(this, eventInterrupt, true);
            }
        }

        if (newActive && this.progress != 0) {
            if (!canOperate || this.energy < energyUsage) {
                if (!canOperate) {
                    this.progress = 0;
                    NetworkHelper.initiateTileEntityEvent(this, eventInterrupt, true); // Interrupt sound if progress stops unexpectedly
                }
                newActive = false;
            }
        } else if (canOperate) {
            if (this.energy >= energyUsage) {
                newActive = true;
            }
        } else {
            this.progress = 0;
        }

        if (newActive && canOperate) {
            this.progress = (short)(this.progress + this.speed / 30);
        }

        for (int i = 0; i < 2; ++i) {
            ItemStack upgradeStack = this.upgradeSlot.get(i);
            if (upgradeStack != null && upgradeStack.getItem() instanceof ISimpleUpgrade) {
                ISimpleUpgrade upgrade = (ISimpleUpgrade) upgradeStack.getItem();
                if (upgrade.canTick(upgradeStack) && upgrade.onTick(this, upgradeStack)) {
                    needsInvUpdate = true;
                }
            }
        }

        if (needsInvUpdate) {
            this.onInventoryChanged();
        }

        if (newActive != wasActive) {
            if (newActive) {
                NetworkHelper.initiateTileEntityEvent(this, eventStart, true); // Start sound only when transitioning to active
            } else {
                NetworkHelper.initiateTileEntityEvent(this, eventStop, true); // Stop sound only when transitioning to inactive
            }
            this.setActive(newActive);
        }
    }

    public boolean canOperate() {
        if (this.inputs.get(0).isEmpty()) {
            cachedOutput = null; // Invalidate cached output if no input
            outputLocked = false;
            return false;
        } else {
            if (!outputLocked) {
                ItemStack resultStack = this.inputFilter.getOutputFor(this.inputs.get(0).get(), false);
                if (resultStack == null) {
                    cachedOutput = null; // Invalidate cached output if no result
                    return false;
                } else {
                    cachedOutput = resultStack.copy();
                    outputLocked = true;
                }
            }

            int resultMaxStackSize = cachedOutput.getMaxStackSize();
            int freeSpaceOutputSlots = 0;
            for (InvSlotOutput curOutputSlot : this.outputs) {
                if (curOutputSlot.isEmpty()) {
                    freeSpaceOutputSlots += resultMaxStackSize;
                } else if (curOutputSlot.get().isItemEqual(cachedOutput)) {
                    freeSpaceOutputSlots += (resultMaxStackSize - curOutputSlot.get().stackSize);
                }
            }
            return freeSpaceOutputSlots >= cachedOutput.stackSize;
        }
    }

    protected void operate() {
        if (canOperate()) {
            ItemStack resultStack = cachedOutput.copy(); // Use the validated cachedOutput
            int[] spaceInOutput = new int[outputs.size()];
            int resultMaxStackSize = resultStack.getMaxStackSize();

            // Calculate space available in each output slot
            for (int i = 0; i < outputs.size(); i++) {
                if (this.outputs.get(i).isEmpty()) {
                    spaceInOutput[i] = resultMaxStackSize;
                } else if (this.outputs.get(i).get().isItemEqual(resultStack)) {
                    spaceInOutput[i] = resultMaxStackSize - this.outputs.get(i).get().stackSize;
                }
            }

            // Ensure we don't partially operate if there's not enough space
            int totalFreeSpace = 0;
            for (int space : spaceInOutput) {
                totalFreeSpace += space;
            }
            if (totalFreeSpace < resultStack.stackSize) {
                return; // Not enough space to operate
            }

            // Distribute items into output slots
            for (int i = 0; i < outputs.size(); i++) {
                int space = spaceInOutput[i];
                if (space > 0) {
                    int toTransfer = Math.min(resultStack.stackSize, space);
                    if (this.outputs.get(i).isEmpty()) {
                        this.outputs.get(i).put(resultStack.splitStack(toTransfer));
                    } else {
                        this.outputs.get(i).get().stackSize += toTransfer;
                        resultStack.stackSize -= toTransfer;
                    }
                    if (resultStack.stackSize <= 0) {
                        break;
                    }
                }
            }

            // Decrease input stack size or set to null if empty
            --this.inputs.get(0).get().stackSize;
            if (this.inputs.get(0).get().stackSize <= 0) {
                this.inputs.get(0).clear();
            }

            // Invalidate `cachedOutput` after operation
            cachedOutput = null;
            outputLocked = false;
        }
    }

    @Override
    public void onNetworkEvent(int event) {
        if (this.audioSource == null && this.getStartSoundFile() != null) {
            this.audioSource = IC2.audioManager.createSource(this, PositionSpec.Center, this.getStartSoundFile(), false, false, 0.1f);
        }
        switch (event) {
            case eventStart:
                if (this.audioSource != null) {
                    this.audioSource.play();
                }
                break;
            case eventInterrupt:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                    if (this.getInterruptSoundFile() != null) {
                        IC2.audioManager.playOnce(this, PositionSpec.Center, this.getInterruptSoundFile(), false, 0.1f);
                    }
                }
                break;
            case eventStop:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                }
        }
    }

    public abstract String getStartSoundFile();

    public String getInterruptSoundFile() {
        return AdvancedMachinesConfig.INTERRUPT_SOUND;
    }

    public abstract String getSpeedName();

    public abstract List<SlotInvSlot> getSlots();

    public String printFormattedData() {
        DecimalFormat format = new DecimalFormat("##.##", new DecimalFormatSymbols(Locale.ROOT));
        return String.format(this.speedFormat, format.format(((double) this.speed / (double) maxSpeed) * 100));
    }

    @Override
    public ContainerBase getGuiContainer(EntityPlayer player) {
        return new ContainerAdvancedMachine(player, this);
    }

    @Override
    public float getWrenchDropRate() {
        return 0.8F;
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}

    @Override
    public boolean isRedstonePowered() {
        return this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord) || isRedstonePoweredFomUpgrade(this.upgradeSlot);
    }

    public boolean isRedstonePoweredFomUpgrade(InvAdvSlotUpgrade slots) {
        if (slots != null) {
            boolean redstoneUpgrade = false;
            for (int i = 0; i < slots.size(); i++) {
                ItemStack upgrade = slots.get(i);
                if (upgrade != null && upgrade.isItemEqual(BlocksItems.REDSTONE_INVERTER)) {
                    redstoneUpgrade = true;
                    break;
                }
            }
            return redstoneUpgrade;
        } else return false;
    }

    public int gaugeProgressScaled(int factor) {
        return factor * this.progress / maxProgress;
    }

    public int gaugeFuelScaled(int factor) {
        return factor * this.energy / maxEnergy;
    }
}
