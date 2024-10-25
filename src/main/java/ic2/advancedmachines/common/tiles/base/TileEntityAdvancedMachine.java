package ic2.advancedmachines.common.tiles.base;

import ic2.advancedmachines.common.AdvancedMachinesConfig;
import ic2.advancedmachines.common.container.ContainerAdvancedMachine;
import ic2.api.network.NetworkHelper;
import ic2.core.IC2;
import ic2.core.audio.AudioSource;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public abstract class TileEntityAdvancedMachine extends TileEntityBaseMachine implements ISidedInventory {

    public static int maxProgress = 4000;
    public static int maxEnergy = 5000;
    public static int maxSpeed = 10000;
    public static int maxInput = 128;

    public short speed;
    public String invName;
    public int[] inputs;
    public int[] outputs;
    public short progress;
    public String speedFormat = "%s%%";

    public AudioSource audioSource;
    public static final int eventStart = 0;
    public static final int eventInterrupt = 1;
    public static final int eventStop = 2;

    public int energyUsage = 15;

    public TileEntityAdvancedMachine(String invName, int[] inputSlots, int[] outputSlots) {
        super(inputSlots.length + outputSlots.length + 4, maxEnergy, maxInput);
        this.invName = invName;
        this.inputs = inputSlots;
        this.outputs = outputSlots;
        this.speed = 0;
        this.progress = 0;
        System.out.println("Tile: " + this.invName + " with slot size: " + this.inventory.length);
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
    public void updateEntity() {
        super.updateEntity();
        if (IC2.platform.isRendering()) return;
        boolean needsInvUpdate = false;
        if (energy <= maxEnergy) {
            needsInvUpdate = getPowerFromFuelSlot();
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
            NetworkHelper.initiateTileEntityEvent(this, eventStop, true);
        }

        boolean canOperate = canOperate();
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
            progress = 0;
        }

        if (newActive && canOperate) {
            progress = (short) (progress + speed / 30);
            this.energy -= energyUsage;
        }

        if (needsInvUpdate) {
            this.onInventoryChanged();
        }

        if (newActive != this.getActive()) {
            this.setActive(newActive);
            worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
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

    /**
     * Returns the ItemStack that results from processing whatever is in the Input, or null
     *
     * @param input        ItemStack to be processed
     * @param adjustOutput if true, whatever was used as input will be taken from the input slot and destroyed,
     *                     if false, the input Slots remain as they are
     * @return ItemStack that results from processing the Input, or null if no processing is possible
     */
    public abstract ItemStack getResultFor(ItemStack input, boolean adjustOutput);

    public Container getGuiContainer(InventoryPlayer player) {
        return new ContainerAdvancedMachine(player, this);
    }

    public abstract String getStartSoundFile();

    public String getInterruptSoundFile() {
        return AdvancedMachinesConfig.INTERRUPT_SOUND;
    }

    public abstract String getSpeedName();

    public abstract List<Slot> getSlots(InventoryPlayer playerInv);

    public String printFormattedData() {
        DecimalFormat format = new DecimalFormat("##.##", new DecimalFormatSymbols(Locale.ROOT));
        return String.format(this.speedFormat, format.format(((double) this.speed / (double) maxSpeed) * 100));
    }

    @Override
    public void invalidate() {
        if (this.audioSource != null) {
            IC2.audioManager.removeSources(this);
            this.audioSource = null;
        }
        super.invalidate();
    }

    @Override
    public void onNetworkEvent(int event) {
        if (this.audioSource == null && this.getStartSoundFile() != null) {
            this.audioSource = IC2.audioManager.createSource(this, this.getStartSoundFile());
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
                        IC2.audioManager.playOnce(this, this.getInterruptSoundFile());
                    }
                }
                break;
            case eventStop:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                }
        }
    }

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

    public int gaugeProgressScaled(int factor) {
        return factor * this.progress / maxProgress;
    }

    public int gaugeFuelScaled(int factor) {
        return factor * this.energy / maxEnergy;
    }
}
