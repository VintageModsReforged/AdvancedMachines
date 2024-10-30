package ic2.advancedmachines.blocks.tiles.base;

import ic2.advancedmachines.blocks.tiles.container.ContainerAdvancedElectricBlock;
import ic2.advancedmachines.blocks.tiles.machines.IEnergyProvider;
import ic2.advancedmachines.utils.LangHelper;
import ic2.core.ContainerIC2;
import ic2.core.IC2;
import ic2.core.block.wiring.TileEntityElectricBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityAdvancedEnergyBlock extends TileEntityElectricBlock implements IEnergyProvider {

    public String name;
    public byte workMode = 0;

    public TileEntityAdvancedEnergyBlock(String name, int tier, int out, int maxStorage) {
        super(tier, out, maxStorage);
        this.name = LangHelper.format("block.advanced." + name + ".name");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setByte("workMode", workMode);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        workMode = nbttagcompound.getByte("workMode");
    }

    @Override
    public String getInvName() {
        return this.name;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.getTotalWorldTime() % 20 == 0) {
            System.out.println(this.redstoneMode);
        }
    }

    @Override
    public int getTransferRate() {
        return this.output / 16;
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerAdvancedElectricBlock(entityPlayer.inventory, this);
    }

    @Override
    public String getGuiClassName(EntityPlayer entityPlayer) {
        return "gui.GuiAdvEnergyBlock";
    }

    @Override
    public void onNetworkEvent(EntityPlayer player, int event) {
        if (event == 0) {
            this.redstoneMode = (byte) (++this.redstoneMode % redstoneModes);
            IC2.platform.messagePlayer(player, getMode(this.redstoneMode));
        }
    }

    public String getMode(byte mode) {
        switch (mode) {
            case 0: return LangHelper.format("tooltip.block.energy.nothing");
            case 1: return LangHelper.format("tooltip.block.energy.full");
            case 2: return LangHelper.format("tooltip.block.energy.part");
            case 3: return LangHelper.format("tooltip.block.energy.empty");
            case 4: return LangHelper.format("tooltip.block.energy.no_output");
            case 5: return LangHelper.format("tooltip.block.energy.no_output_unless_full");
            default: return "";
        }
    }
}
