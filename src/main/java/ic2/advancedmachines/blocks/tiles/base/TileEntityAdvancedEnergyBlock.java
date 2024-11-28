package ic2.advancedmachines.blocks.tiles.base;

import mods.vintage.core.platform.lang.Translator;
import ic2.advancedmachines.blocks.tiles.container.ContainerAdvancedElectricBlock;
import ic2.advancedmachines.blocks.tiles.machines.IEnergyProvider;
import ic2.core.ContainerIC2;
import ic2.core.IC2;
import ic2.core.block.wiring.TileEntityElectricBlock;
import net.minecraft.entity.player.EntityPlayer;

public class TileEntityAdvancedEnergyBlock extends TileEntityElectricBlock implements IEnergyProvider {

    public String name;

    public TileEntityAdvancedEnergyBlock(String name, int tier, int out, int maxStorage) {
        super(tier, out, maxStorage);
        this.name = Translator.format("block.advanced." + name + ".name");
    }

    @Override
    public String getInvName() {
        return this.name;
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
            IC2.platform.messagePlayer(player, getMode());
        }
    }

    public String getMode() {
        switch (this.redstoneMode) {
            case 0: return Translator.format("tooltip.block.energy.nothing");
            case 1: return Translator.format("tooltip.block.energy.full");
            case 2: return Translator.format("tooltip.block.energy.part");
            case 3: return Translator.format("tooltip.block.energy.empty");
            case 4: return Translator.format("tooltip.block.energy.no_output");
            case 5: return Translator.format("tooltip.block.energy.no_output_unless_full");
            default: return "";
        }
    }
}
