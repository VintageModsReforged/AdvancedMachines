package ic2.advancedmachines.common.tiles.base;

import ic2.advancedmachines.common.BlocksItems;
import ic2.api.Direction;
import ic2.api.IElectricItem;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;

public abstract class TileEntityBaseMachine extends TileEntityMachine implements IEnergySink {
    public int energy;
    public int fuelSlot;
    public int maxEnergy;
    public int maxInput;
    public int tier;
    public boolean addedToEnergyNet;

    public TileEntityBaseMachine(int inventorySize, int maxEnergy, int maxInput) {
        super(inventorySize);
        this.fuelSlot = 0;
        this.maxEnergy = maxEnergy;
        this.maxInput = maxInput;
        this.tier = 1;

        energy = 0;
        addedToEnergyNet = false;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.energy = tagCompound.getInteger("energy");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("energy", this.energy);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
        }
    }

    @Override
    public void invalidate() {
        if (this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnergyNet = false;
        }

        super.invalidate();
    }

    @Override
    public boolean isAddedToEnergyNet() {
        return this.addedToEnergyNet;
    }

    @Override
    public int demandsEnergy() {
        return maxEnergy - energy;
    }

    @Override
    public int injectEnergy(Direction direction, int amount) {
        if (amount > this.maxInput) {
            IC2.explodeMachineAt(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            invalidate();
            return 0;
        } else {
            this.energy += amount;
            int needed = 0;
            if (this.energy > this.maxEnergy) {
                needed = this.energy - this.maxEnergy;
                this.energy = this.maxEnergy;
            }
            return needed;
        }
    }

    @Override
    public int getMaxSafeInput() {
        return maxInput;
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity tile, Direction direction) {
        return true;
    }

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

    public boolean getPowerFromFuelSlot() {
        boolean ret = false;
        if (this.inventory[this.fuelSlot] == null) {
            return false;
        } else {
            int id = this.inventory[this.fuelSlot].itemID;
            if (Item.itemsList[id] instanceof IElectricItem) {
                if (!((IElectricItem)Item.itemsList[id]).canProvideEnergy()) {
                    return false;
                } else {
                    int transfer = ic2.core.item.ElectricItem.discharge(this.inventory[this.fuelSlot], this.maxEnergy - this.energy, this.tier, false, false);
                    this.energy += transfer;
                    return ret || transfer > 0;
                }
            } else if (id == Item.redstone.itemID) {
                this.energy += this.maxEnergy;
                --this.inventory[this.fuelSlot].stackSize;
                if (this.inventory[this.fuelSlot].stackSize <= 0) {
                    this.inventory[this.fuelSlot] = null;
                }

                return true;
            } else if (id == Ic2Items.suBattery.itemID) {
                this.energy += 1000;
                --this.inventory[this.fuelSlot].stackSize;
                if (this.inventory[this.fuelSlot].stackSize <= 0) {
                    this.inventory[this.fuelSlot] = null;
                }

                return true;
            } else {
                return ret;
            }
        }
    }

    public abstract int[] getUpgradeSlots();
}
