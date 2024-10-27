package ic2.advancedmachines.blocks.tiles.base;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.blocks.gui.GuiAdvancedMachine;
import ic2.advancedmachines.AdvancedMachinesConfig;
import ic2.advancedmachines.BlocksItems;
import ic2.advancedmachines.blocks.container.ContainerAdvancedMachine;
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
import ic2.core.util.StackUtil;
import net.minecraft.client.gui.GuiScreen;
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

    public TileEntityAdvancedMachine(String invName, int upgradeSlotStartIndex) {
        super(maxEnergy, 2, 0);
        this.soundTicker = IC2.random.nextInt(64);
        this.invName = invName;
        this.inputs = new ArrayList<InvSlotProcessable>();
        this.outputs = new ArrayList<InvSlotOutput>();
        this.speed = 0;
        this.progress = 0;
        this.upgradeSlot = new InvAdvSlotUpgrade(this, "upgrade", upgradeSlotStartIndex, 2);
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

        boolean newActive = this.getActive();
        if (this.speed == 0) {
            newActive = false;
        }

        if (this.progress >= maxProgress) {
            this.operate();
            needsInvUpdate = true;
            this.progress = 0;
            newActive = false;
            IC2.network.initiateTileEntityEvent(this, eventStop, true);
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
            NetworkHelper.initiateTileEntityEvent(this, eventStart, true);

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

        if (needsInvUpdate) {
            this.onInventoryChanged();
        }

        if (newActive != this.getActive()) {
            this.setActive(newActive);
        }
    }

    public boolean canOperate() {
        if (this.inputs.get(0).isEmpty()) {
            return false;
        } else {
            boolean canOperate = false;
            ItemStack result;
            for (InvSlotOutput output : this.outputs) {
                result = this.getResultFor(this.inputs.get(0).get(), false);
                if (result != null) {
                    canOperate = output.canAdd(result);
                }
            }
            return canOperate;
        }
    }

    public void operate() {
        if (canOperate()) {
            ItemStack result = this.getResultFor(this.inputs.get(0).get(), false).copy();
            int[] stackSizeSpaceAvailableInOutput = new int[outputs.size()];
            int resultMaxStackSize = result.getMaxStackSize();
            int index;
            for (index = 0; index < outputs.size(); index++) {
                if (outputs.get(index).isEmpty()) {
                    stackSizeSpaceAvailableInOutput[index] = resultMaxStackSize;
                } else if (StackUtil.isStackEqual(outputs.get(index).get(), result)) {
                    stackSizeSpaceAvailableInOutput[index] = resultMaxStackSize - outputs.get(index).get().stackSize;
                }
            }
            for (index = 0; index < stackSizeSpaceAvailableInOutput.length; index++) {
                if (stackSizeSpaceAvailableInOutput[index] > 0) {
                    int stackSizeToStash = Math.min(result.stackSize, stackSizeSpaceAvailableInOutput[index]);
                    if (outputs.get(index).isEmpty()) {
                        outputs.get(index).add(result);
                        break;
                    } else {
                        outputs.get(index).get().stackSize += stackSizeToStash;
                        result.stackSize -= stackSizeToStash;
                        if (result.stackSize <= 0) {
                            break;
                        }
                    }
                }
            }
            if (inputs.get(0).get().stackSize <= 0) {
                inputs.get(0).clear();
            }
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

    /**
     * Returns the ItemStack that results from processing whatever is in the Input, or null
     *
     * @param input        ItemStack to be processed
     * @param adjustOutput if true, whatever was used as input will be taken from the input slot and destroyed,
     *                     if false, the input Slots remain as they are
     * @return ItemStack that results from processing the Input, or null if no processing is possible
     */
    public abstract ItemStack getResultFor(ItemStack input, boolean adjustOutput);

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

    @SideOnly(Side.CLIENT)
    @Override
    public GuiScreen getGui(EntityPlayer player, boolean b) {
        return new GuiAdvancedMachine(player.inventory, this);
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
                if (upgrade != null && upgrade.isItemEqual(new ItemStack(BlocksItems.REDSTONE_UPGRADE))) {
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
