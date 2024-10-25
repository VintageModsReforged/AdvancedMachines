package ic2.advancedmachines.common.tiles.base;

import ic2.advancedmachines.common.AdvancedMachinesConfig;
import ic2.advancedmachines.common.BlocksItems;
import ic2.advancedmachines.common.container.ContainerAdvancedMachine;
import ic2.api.Direction;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.network.NetworkHelper;
import ic2.core.ContainerIC2;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.audio.AudioSource;
import ic2.core.block.machine.tileentity.TileEntityElecMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public abstract class TileEntityAdvancedMachine extends TileEntityElecMachine implements IHasGui, ISidedInventory, INetworkTileEntityEventListener {

    public static int maxProgress = 4000;
    public static int maxEnergy = 5000;
    public static int maxSpeed = 10000;
    public static int maxInput = 128;
    public static int energyUsage = 15;

    public short speed;
    public String invName;
    public int[] inputs;
    public int[] outputs;
    public short progress;
    public String speedFormat = "%s%%";
    public int soundTicker;
    public AudioSource audioSource;
    private static final int eventStart = 0;
    private static final int eventInterrupt = 1;
    private static final int eventStop = 2;

    public TileEntityAdvancedMachine(String invName, int[] inputSlots, int[] outputSlots) {
        super(inputSlots.length + outputSlots.length + 4, 0, maxEnergy, maxInput);
        this.invName = invName;
        this.inputs = inputSlots;
        this.outputs = outputSlots;
        this.speed = 0;
        this.progress = 0;
        this.soundTicker = IC2.random.nextInt(64);
    }

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
        if (this.energy <= maxEnergy) {
            needsInvUpdate = this.provideEnergy();
        }

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
            if (speed < maxSpeed) {
                ++this.speed;
                --energy;
            } else {
                speed = (short) maxSpeed;
                energy -= energyUsage;
            }

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
            this.energy -= energyUsage;
        }

        if (needsInvUpdate) {
            this.onInventoryChanged();
        }

        if (newActive != this.getActive()) {
            this.setActive(newActive);
        }
    }

    protected boolean canOperate() {
        if (inventory[inputs[0]] == null) {
            return false;
        } else {
            ItemStack resultStack = getResultFor(inventory[inputs[0]], false);
            if (resultStack == null) {
                return false;
            } else {
                int resultMaxStackSize = resultStack.getMaxStackSize();
                int freeSpaceOutputSlots = 0;
                for (int index = 0; index < outputs.length; ++index) {
                    int curOutputSlot = outputs[index];
                    if (inventory[curOutputSlot] == null) {
                        freeSpaceOutputSlots += resultMaxStackSize;
                    } else if (inventory[curOutputSlot].isItemEqual(resultStack)) {
                        freeSpaceOutputSlots += (resultMaxStackSize - inventory[curOutputSlot].stackSize);
                    }
                }

                return freeSpaceOutputSlots >= resultStack.stackSize;
            }
        }
    }

    protected void operate() {
        if (canOperate()) {
            ItemStack resultStack = getResultFor(inventory[inputs[0]], true).copy();
            int[] stackSizeSpaceAvailableInOutput = new int[outputs.length];
            int resultMaxStackSize = resultStack.getMaxStackSize();

            int index;
            for (index = 0; index < outputs.length; ++index) {
                if (inventory[outputs[index]] == null) {
                    stackSizeSpaceAvailableInOutput[index] = resultMaxStackSize;
                } else if (inventory[outputs[index]].isItemEqual(resultStack)) {
                    stackSizeSpaceAvailableInOutput[index] = resultMaxStackSize - inventory[outputs[index]].stackSize;
                }
            }

            for (index = 0; index < stackSizeSpaceAvailableInOutput.length; ++index) {
                if (stackSizeSpaceAvailableInOutput[index] > 0) {
                    int stackSizeToStash = Math.min(resultStack.stackSize, stackSizeSpaceAvailableInOutput[index]);
                    if (inventory[outputs[index]] == null) {
                        inventory[outputs[index]] = resultStack;
                        break;
                    } else {
                        inventory[outputs[index]].stackSize += stackSizeToStash;
                        resultStack.stackSize -= stackSizeToStash;
                        if (resultStack.stackSize <= 0) {
                            break;
                        }
                    }
                }
            }
            if (inventory[inputs[0]].stackSize <= 0) {
                inventory[inputs[0]] = null;
            }
        }
    }

    @Override
    public void onNetworkEvent(int event) {
        if (this.audioSource == null && this.getStartSoundFile() != null) {
            this.audioSource = IC2.audioManager.createSource(this, this.getStartSoundFile());
        }

        switch (event) {
            case 0:
                if (this.audioSource != null) {
                    this.audioSource.play();
                }
                break;
            case 1:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                    if (this.getInterruptSoundFile() != null) {
                        IC2.audioManager.playOnce(this, this.getInterruptSoundFile());
                    }
                }
                break;
            case 2:
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

    public abstract List<Slot> getSlots(InventoryPlayer playerInv);

    public abstract int[] getUpgradeSlots();

    public String printFormattedData() {
        DecimalFormat format = new DecimalFormat("##.##", new DecimalFormatSymbols(Locale.ROOT));
        return String.format(this.speedFormat, format.format(((double) this.speed / (double) maxSpeed) * 100));
    }

    @Override
    public int injectEnergy(Direction directionFrom, int amount) {
        if (amount > 128) {
            IC2.explodeMachineAt(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            return 0;
        } else {
            this.energy += amount;
            int re = 0;
            if (this.energy > maxEnergy) {
                re = this.energy - maxEnergy;
                this.energy = maxEnergy;
            }
            return re;
        }
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer player) {
        return new ContainerAdvancedMachine(player.inventory, this);
    }

    @Override
    public String getGuiClassName(EntityPlayer entityPlayer) {
        return "";
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}

    @Override
    public int getStartInventorySide(ForgeDirection side) {
        switch (side) {
            case DOWN:
                return 0; // power slot always 0
            case UP:
                return inputs[0];
            default:
                return outputs[0];
        }
    }

    @Override
    public int getSizeInventorySide(ForgeDirection side) {
        switch (side) {
            case DOWN:
                return 1;
            case UP:
                return this.inputs.length;
            default:
                return this.outputs.length;
        }
    }

    @Override
    public boolean isRedstonePowered() {
        return this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord) || isRedstonePoweredFomUpgrade(getUpgradeSlots());
    }

    public boolean isRedstonePoweredFomUpgrade(int[] slots) {
        boolean redstoneUpgrade = false;
        for (int upgradeSlot : slots) {
            ItemStack upgrade = this.inventory[upgradeSlot];
            if (upgrade != null) {
                if (upgrade.isItemEqual(new ItemStack(BlocksItems.REDSTONE_UPGRADE))) {
                    redstoneUpgrade = true;
                    break;
                }
            }
        }
        return redstoneUpgrade;
    }

    public int gaugeProgressScaled(int factor) {
        return factor * this.progress / maxProgress;
    }

    public int gaugeFuelScaled(int factor) {
        return factor * this.energy / maxEnergy;
    }
}
