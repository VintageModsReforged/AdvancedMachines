package ic2.advancedmachines.blocks.tiles.machines;

import ic2.advancedmachines.blocks.tiles.container.ContainerChargedElectrolyzer;
import ic2.advancedmachines.utils.AdvMachinesRecipeManager;
import ic2.api.Items;
import ic2.core.ContainerIC2;
import ic2.core.block.machine.tileentity.TileEntityElectrolyzer;
import ic2.core.block.wiring.TileEntityElectricBlock;
import core.helpers.LangHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class TileEntityChargedElectrolyzer extends TileEntityElectrolyzer {

    public static List recipesDrain = new Vector();
    public static List recipesPower = new Vector();

    public static final int inputSlot = 0;
    public static final int outputSlot = 1;

    List<TileEntityElectricBlock> energyProviders = new ArrayList<TileEntityElectricBlock>();

    public TileEntityChargedElectrolyzer() {}

    @Override
    public String getInvName() {
        return LangHelper.format("block.advanced.electrolyzer.name");
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer player) {
        return new ContainerChargedElectrolyzer(player.inventory, this);
    }

    @Override
    public String getGuiClassName(EntityPlayer entityPlayer) {
        return "gui.GuiAdvElectrolyzer";
    }

    @Override
    public void updateEntity() {
        boolean needsInvUpdate = false;
        boolean newActive = false;
        if (this.ticker++ % 16 == 0) {
            energyProviders = lookForEnergyProviders();
        }

        if (!energyProviders.isEmpty()) {
            if (this.shouldDrain() && this.canDrain()) {
                boolean needsUpdate = false;
                for (TileEntityElectricBlock provider : this.energyProviders) {
                    needsUpdate |= drain(provider);
                }
                needsInvUpdate = needsUpdate;
                newActive = true;
            } else if (this.shouldPower() && (this.canPower() || this.energy > 0)) {
                boolean needsUpgrade = false;
                for (TileEntityElectricBlock provider : this.energyProviders) {
                    needsUpgrade |= power(provider);
                }
                needsInvUpdate = needsUpgrade;
                newActive = true;
            }
        }

        if (this.getActive() != newActive) {
            this.setActive(newActive);
            needsInvUpdate = true;
        }

        if (needsInvUpdate) {
            this.onInventoryChanged();
        }
    }

    public boolean drain(TileEntityElectricBlock provider) {
        if (shouldDrain(provider)) {
            provider.energy -= this.getProcessRateFrom(provider);
            this.energy += (short) this.getProcessRateFrom(provider);
            if (this.energy >= 20000) {
                this.energy -= 20000;
                ItemStack result = getDrainResultFor(this.inventory[inputSlot], true).copy();
                int stackSizeSpaceAvailableInOutput = 0;
                int resultMaxStackSize = result.getMaxStackSize();

                if (inventory[outputSlot] == null) {
                    stackSizeSpaceAvailableInOutput = resultMaxStackSize;
                } else if (inventory[outputSlot].isItemEqual(result)) {
                    stackSizeSpaceAvailableInOutput = resultMaxStackSize - inventory[outputSlot].stackSize;
                }
                if (stackSizeSpaceAvailableInOutput > 0) {
                    int stackSizeToStash = Math.min(result.stackSize, stackSizeSpaceAvailableInOutput);
                    if (inventory[outputSlot] == null) {
                        inventory[outputSlot] = result;
                    } else {
                        inventory[outputSlot].stackSize += stackSizeToStash;
                    }
                }
                if (inventory[inputSlot].stackSize <= 0) {
                    inventory[inputSlot] = null;
                }
                return true;
            } else return false;
        } else return false;
    }

    public boolean power(TileEntityElectricBlock provider) {
        if (shouldPower(provider)) {
            if (this.energy > 0) {
                int out = this.processRate();
                if (out > this.energy) {
                    out = this.energy;
                }
                this.energy -= (short) out;
                provider.energy += out;
                return false;
            } else {
                ItemStack result = getPowerResultFor(this.inventory[inputSlot], true).copy();
                if (result.isItemEqual(Items.getItem("waterCell"))) {
                    this.energy = (short) (this.energy + 12000 + 2000 * provider.tier);
                    int stackSizeSpaceAvailableInOutput = 0;
                    int resultMaxStackSize = result.getMaxStackSize();

                    if (inventory[outputSlot] == null) {
                        stackSizeSpaceAvailableInOutput = resultMaxStackSize;
                    } else if (inventory[outputSlot].isItemEqual(result)) {
                        stackSizeSpaceAvailableInOutput = resultMaxStackSize - inventory[outputSlot].stackSize;
                    }
                    if (stackSizeSpaceAvailableInOutput > 0) {
                        int stackSizeToStash = Math.min(result.stackSize, stackSizeSpaceAvailableInOutput);
                        if (inventory[outputSlot] == null) {
                            inventory[outputSlot] = result;
                        } else {
                            inventory[outputSlot].stackSize += stackSizeToStash;
                        }
                    }
                    if (inventory[inputSlot].stackSize <= 0) {
                        inventory[inputSlot] = null;
                    }
                }
                return true;
            }
        } else return false;
    }

    @Override
    public boolean shouldPower() {
        boolean shouldPower = false;
        for (TileEntityElectricBlock provider : this.energyProviders) {
            shouldPower |= shouldPower(provider);
        }
        return shouldPower;
    }

    public boolean shouldPower(TileEntityElectricBlock provider) {
        return (double) provider.energy / provider.maxStorage <= 0.3;
    }

    @Override
    public boolean canDrain() {
        ItemStack result = this.getDrainResultFor(this.inventory[inputSlot], false);
        return canOperate(result);
    }

    @Override
    public boolean canPower() {
        ItemStack result = this.getPowerResultFor(this.inventory[inputSlot], false);
        return canOperate(result);
    }

    public boolean canOperate(ItemStack result) {
        if (result == null) {
            return false;
        } else {
            ItemStack resultCopy = result.copy();
            int resultMaxStackSize = resultCopy.getMaxStackSize();
            int freeSpaceOutputSlots = 0;
            if (inventory[outputSlot] == null) {
                freeSpaceOutputSlots += resultMaxStackSize;
            } else if (inventory[outputSlot].isItemEqual(resultCopy)) {
                freeSpaceOutputSlots += (resultMaxStackSize - inventory[outputSlot].stackSize);
            }
            return freeSpaceOutputSlots >= resultCopy.stackSize;
        }
    }

    @Override
    public boolean shouldDrain() {
        boolean shouldDrain = false;
        for (TileEntityElectricBlock provider : this.energyProviders) {
            shouldDrain |= shouldDrain(provider);
        }
        return shouldDrain;
    }

    public boolean shouldDrain(TileEntityElectricBlock provider) {
        return (double) provider.energy / provider.maxStorage >= 0.7;
    }

    @Override
    public int processRate() {
        int transferRate = 0;
        for (TileEntityElectricBlock provider : this.energyProviders) {
            transferRate += getProcessRateFrom(provider);
        }
        return transferRate;
    }

    public int getProcessRateFrom(TileEntityElectricBlock provider) {
        if (provider instanceof IEnergyProvider) {
            IEnergyProvider energyProvider = (IEnergyProvider) provider;
            return energyProvider.getTransferRate();
        } else {
            switch (provider.tier) {
                case 2: return 8;
                case 3: return 32;
                default: return 2;
            }
        }
    }

    public List<TileEntityElectricBlock> lookForEnergyProviders() {
        List<TileEntityElectricBlock> providers = new ArrayList<TileEntityElectricBlock>();
        for (ForgeDirection direction : ForgeDirection.values()) {
            int xPos = this.xCoord + direction.offsetX;
            int yPos = this.yCoord + direction.offsetY;
            int zPos = this.zCoord + direction.offsetZ;
            TileEntity blockEntity = this.worldObj.getBlockTileEntity(xPos, yPos, zPos);
            if (blockEntity instanceof TileEntityElectricBlock) {
                providers.add((TileEntityElectricBlock) blockEntity);
            }
        }
        return providers;
    }

    public ItemStack getDrainResultFor(ItemStack stack, boolean consume) {
        return AdvMachinesRecipeManager.getDrainElectrolyzerOutputFor(stack, consume);
    }

    public ItemStack getPowerResultFor(ItemStack stack, boolean consume) {
        return AdvMachinesRecipeManager.getPowerElectrolyzerOutputFor(stack, consume);
    }
}
