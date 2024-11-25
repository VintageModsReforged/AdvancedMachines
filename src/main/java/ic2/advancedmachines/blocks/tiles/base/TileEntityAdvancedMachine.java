package ic2.advancedmachines.blocks.tiles.base;

import ic2.advancedmachines.AdvancedMachinesConfig;
import ic2.advancedmachines.BlocksItems;
import ic2.advancedmachines.blocks.tiles.container.ContainerAdvancedMachine;
import ic2.advancedmachines.items.IUpgradeItem;
import ic2.advancedmachines.utils.IStackFilter;
import ic2.api.Direction;
import ic2.api.Items;
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

    public int maxProgress = 4000;
    public int maxSpeed = 10000;
    public int energyUsage = 15;

    public int defaultMaxInput;
    public int defaultEnergyStorage;

    public short speed;
    public String invName;
    public int[] inputs;
    public int[] outputs;
    public short progress;
    public String speedFormat = "%s%%";
    public int soundTicker;
    public AudioSource audioSource;
    private final int eventStart = 0;
    private final int eventInterrupt = 1;
    private final int eventStop = 2;
    public IStackFilter inputFilter;
    public String guiPath;

    private ItemStack cachedOutput = null;
    private boolean outputLocked = false;


    public TileEntityAdvancedMachine(String invName, int[] inputSlots, int[] outputSlots, IStackFilter inputFilter, String guiPath) {
        super(inputSlots.length + outputSlots.length + 4, 0, 5000, 128);
        this.invName = invName;
        this.inputs = inputSlots;
        this.outputs = outputSlots;
        this.speed = 0;
        this.progress = 0;
        this.soundTicker = IC2.random.nextInt(64);
        this.inputFilter = inputFilter;
        this.guiPath = guiPath;

        this.defaultMaxInput = this.maxInput;
        this.defaultEnergyStorage = this.maxEnergy;
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
    public void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating()) {
            this.handleUpgrades();
        }
    }

    @Override
    public void onInventoryChanged() {
        super.onInventoryChanged();
        if (IC2.platform.isSimulating()) {
            this.handleUpgrades();
        }
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        boolean needsInvUpdate = false;

        if (this.energy <= maxEnergy) {
            needsInvUpdate = this.provideEnergy();
        }

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
                NetworkHelper.initiateTileEntityEvent(this, eventInterrupt, true); // Interrupt sound if progress stops
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
            this.progress = (short) (this.progress + this.speed / 30);
        }

        for (int i = 0; i < 2; ++i) {
            ItemStack upgradeStack = this.inventory[this.getUpgradeSlots()[i]];
            if (upgradeStack != null && upgradeStack.getItem() instanceof IUpgradeItem) {
                IUpgradeItem upgrade = (IUpgradeItem) upgradeStack.getItem();
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

    protected boolean canOperate() {
        if (inventory[inputs[0]] == null) {
            cachedOutput = null; // Invalidate cached output if no input
            outputLocked = false;
            return false;
        } else {
            if (!outputLocked) {
                ItemStack resultStack = getResultFor(inventory[inputs[0]], false);
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

            for (int curOutputSlot : outputs) {
                if (inventory[curOutputSlot] == null) {
                    freeSpaceOutputSlots += resultMaxStackSize;
                } else if (inventory[curOutputSlot].isItemEqual(cachedOutput)) {
                    freeSpaceOutputSlots += (resultMaxStackSize - inventory[curOutputSlot].stackSize);
                }
            }
            return freeSpaceOutputSlots >= cachedOutput.stackSize;
        }
    }

    protected void operate() {
        if (canOperate()) {
            ItemStack resultStack = cachedOutput.copy(); // Use the validated cachedOutput
            int[] spaceInOutput = new int[outputs.length];
            int resultMaxStackSize = resultStack.getMaxStackSize();

            // Calculate space available in each output slot
            for (int i = 0; i < outputs.length; i++) {
                if (inventory[outputs[i]] == null) {
                    spaceInOutput[i] = resultMaxStackSize;
                } else if (inventory[outputs[i]].isItemEqual(resultStack)) {
                    spaceInOutput[i] = resultMaxStackSize - inventory[outputs[i]].stackSize;
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
            for (int i = 0; i < outputs.length; i++) {
                int space = spaceInOutput[i];
                if (space > 0) {
                    int toTransfer = Math.min(resultStack.stackSize, space);
                    if (inventory[outputs[i]] == null) {
                        inventory[outputs[i]] = resultStack.splitStack(toTransfer);
                    } else {
                        inventory[outputs[i]].stackSize += toTransfer;
                        resultStack.stackSize -= toTransfer;
                    }
                    if (resultStack.stackSize <= 0) {
                        break;
                    }
                }
            }

            // Decrease input stack size or set to null if empty
            --inventory[inputs[0]].stackSize;
            if (inventory[inputs[0]].stackSize <= 0) {
                inventory[inputs[0]] = null;
            }

            // Invalidate `cachedOutput` after operation
            cachedOutput = null;
            outputLocked = false;
        }
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
        if (amount > this.maxInput) {
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
                if (upgrade.isItemEqual(BlocksItems.REDSTONE_INVERTER)) {
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

    @Override
    public String getGuiClassName(EntityPlayer player) {
        return "gui.GuiAdv" + this.guiPath;
    }

    public void handleUpgrades() {
        int transformerUpgradeCount = 0;
        int energyStorageUpgradeCount = 0;
        int[] upgradeSlots = getUpgradeSlots();
        for (int i = 0; i < 2; i++) {
            int upgradeSlot = upgradeSlots[i];
            ItemStack upgradeStack = this.inventory[upgradeSlot];
            if (upgradeStack != null) {
                if (upgradeStack.isItemEqual(Items.getItem("transformerUpgrade"))) {
                    transformerUpgradeCount += upgradeStack.stackSize;
                } else if (upgradeStack.isItemEqual(Items.getItem("energyStorageUpgrade"))) {
                    energyStorageUpgradeCount += upgradeStack.stackSize;
                }
            }
        }

        if (transformerUpgradeCount > 10) {
            transformerUpgradeCount = 10;
        }

        this.maxInput = this.defaultMaxInput * (int) Math.pow(4.0F, transformerUpgradeCount);
        this.maxEnergy = this.defaultEnergyStorage + energyStorageUpgradeCount * 10000 + this.maxInput - 1;
        this.tier = transformerUpgradeCount + 1;
    }
}
