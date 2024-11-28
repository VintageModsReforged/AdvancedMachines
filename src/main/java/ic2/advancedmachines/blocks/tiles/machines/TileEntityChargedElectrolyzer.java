package ic2.advancedmachines.blocks.tiles.machines;

import ic2.advancedmachines.AdvancedMachinesRecipes;
import ic2.advancedmachines.blocks.container.ContainerChargedElectrolyzer;
import ic2.advancedmachines.blocks.gui.GuiAdvancedElectrolyzer;
import ic2.advancedmachines.utils.InvSlotFiltered;
import ic2.advancedmachines.utils.StackFilters;
import ic2.api.item.Items;
import ic2.api.recipe.IMachineRecipeManager;
import ic2.core.BasicMachineRecipeManager;
import ic2.core.ContainerBase;
import ic2.core.block.invslot.InvSlotConsumable;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.machine.tileentity.TileEntityElectrolyzer;
import ic2.core.block.wiring.TileEntityElectricBlock;
import ic2.core.slot.SlotInvSlot;
import mods.vintage.core.platform.lang.Translator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class TileEntityChargedElectrolyzer extends TileEntityElectrolyzer {

    List<TileEntityElectricBlock> energyProviders = new ArrayList<TileEntityElectricBlock>();

    public InvSlotConsumable input;
    public InvSlotOutput output;

    public TileEntityChargedElectrolyzer() {
        this.input = new InvSlotFiltered(this, "input", 0, 1, StackFilters.ELECTROLYZER_IN);
        this.output = new InvSlotOutput(this, "output", 1, 1);
    }

    public List<SlotInvSlot> getSlots() {
        List<SlotInvSlot> slots = new ArrayList<SlotInvSlot>();
        slots.add(new SlotInvSlot(this.input, 0, 54, 35));
        slots.add(new SlotInvSlot(this.output, 0, 112, 35));
        return slots;
    }

    @Override
    public String getInvName() {
        return Translator.format("block.advanced.electrolyzer.name");
    }

    @Override
    public ContainerBase getGuiContainer(EntityPlayer player) {
        return new ContainerChargedElectrolyzer(player, this);
    }

    @Override
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiAdvancedElectrolyzer(new ContainerChargedElectrolyzer(entityPlayer, this));
    }

    @Override
    public int processRate() {
        int transferRate = 0;
        for (TileEntityElectricBlock provider : this.energyProviders) {
            transferRate += getProcessRateFrom(provider);
        }
        return transferRate;
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
                ItemStack result = ((ItemStack) AdvancedMachinesRecipes.electrolyzer_drain.getOutputFor(input.get(), true)).copy();
                int stackSizeSpaceAvailableInOutput = 0;
                int resultMaxStackSize = result.getMaxStackSize();

                if (output.isEmpty()) {
                    stackSizeSpaceAvailableInOutput = resultMaxStackSize;
                } else if (output.get().isItemEqual(result)) {
                    stackSizeSpaceAvailableInOutput = resultMaxStackSize - output.get().stackSize;
                }
                if (stackSizeSpaceAvailableInOutput > 0) {
                    int stackSizeToStash = Math.min(result.stackSize, stackSizeSpaceAvailableInOutput);
                    if (output.isEmpty()) {
                        output.put(result);
                    } else {
                        output.get().stackSize += stackSizeToStash;
                    }
                }
                if (input.get().stackSize <= 0) {
                    input.clear();
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
                ItemStack result = ((ItemStack) AdvancedMachinesRecipes.electrolyzer_power.getOutputFor(input.get(), true)).copy();
                if (result.isItemEqual(Items.getItem("waterCell"))) {
                    this.energy = (short) (this.energy + 12000 + 2000 * provider.tier);
                    int stackSizeSpaceAvailableInOutput = 0;
                    int resultMaxStackSize = result.getMaxStackSize();

                    if (output.isEmpty()) {
                        stackSizeSpaceAvailableInOutput = resultMaxStackSize;
                    } else if (output.get().isItemEqual(result)) {
                        stackSizeSpaceAvailableInOutput = resultMaxStackSize - output.get().stackSize;
                    }
                    if (stackSizeSpaceAvailableInOutput > 0) {
                        int stackSizeToStash = Math.min(result.stackSize, stackSizeSpaceAvailableInOutput);
                        if (output.isEmpty()) {
                            output.put(result);
                        } else {
                            output.get().stackSize += stackSizeToStash;
                        }
                    }
                    if (input.get().stackSize <= 0) {
                        input.clear();
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
        ItemStack result = (ItemStack) AdvancedMachinesRecipes.electrolyzer_drain.getOutputFor(input.get(), false);
        return canOperate(result);
    }

    @Override
    public boolean canPower() {
        ItemStack result = (ItemStack) AdvancedMachinesRecipes.electrolyzer_power.getOutputFor(input.get(), false);
        return canOperate(result);
    }

    public boolean canOperate(ItemStack result) {
        if (result == null) {
            return false;
        } else {
            ItemStack resultCopy = result.copy();
            int resultMaxStackSize = resultCopy.getMaxStackSize();
            int freeSpaceOutputSlots = 0;
            if (output.isEmpty()) {
                freeSpaceOutputSlots += resultMaxStackSize;
            } else if (output.get().isItemEqual(resultCopy)) {
                freeSpaceOutputSlots += (resultMaxStackSize - output.get().stackSize);
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
}
