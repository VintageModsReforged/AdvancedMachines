package ic2.advancedmachines.blocks.tiles.base;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.blocks.container.ContainerAdvancedElectricBlock;
import ic2.advancedmachines.blocks.gui.GuiAdvancedElectricBlock;
import ic2.advancedmachines.blocks.tiles.machines.IEnergyProvider;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.block.wiring.TileEntityElectricBlock;
import mods.vintage.core.platform.lang.Translator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

public class TileEntityAdvancedEnergyBlock extends TileEntityElectricBlock implements IEnergyProvider {

    public String name;

    public TileEntityAdvancedEnergyBlock(String name, int tier, int out, int maxStorage) {
        super(tier, out, maxStorage);
        this.name = StatCollector.translateToLocal("block.advanced." + name + ".name");
    }

    @Override
    public ContainerBase getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerAdvancedElectricBlock(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiAdvancedElectricBlock(new ContainerAdvancedElectricBlock(entityPlayer, this));
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
    public void onNetworkEvent(EntityPlayer player, int event) {
        if (event == 0) {
            this.redstoneMode = (byte) (++this.redstoneMode % redstoneModes);
            IC2.platform.messagePlayer(player, getMode());
        }
    }

    public String getMode() {
        switch (this.redstoneMode) {
            case 0: return Translator.RESET.format("tooltip.block.energy.nothing");
            case 1: return Translator.RESET.format("tooltip.block.energy.full");
            case 2: return Translator.RESET.format("tooltip.block.energy.part");
            case 3: return Translator.RESET.format("tooltip.block.energy.empty");
            case 4: return Translator.RESET.format("tooltip.block.energy.no_output");
            case 5: return Translator.RESET.format("tooltip.block.energy.no_output_unless_full");
            default: return "";
        }
    }
}
