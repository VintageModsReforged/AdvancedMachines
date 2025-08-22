package ic2.advancedmachines.blocks.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.blocks.container.ContainerAdvancedElectricBlock;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedEnergyBlock;
import ic2.advancedmachines.utils.GuiTooltipIconButton;
import ic2.core.IC2;
import mods.vintage.core.platform.lang.Translator;
import mods.vintage.core.utils.function.Supplier;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringTranslate;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiAdvancedElectricBlock extends GuiContainer {
    private final ContainerAdvancedElectricBlock container;

    public GuiAdvancedElectricBlock(ContainerAdvancedElectricBlock container) {
        super(container);
        this.ySize = 196;
        this.container = container;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiTooltipIconButton(
                this,
                0,
                (this.width - this.xSize) / 2 + 152,
                (this.height - this.ySize) / 2 + 4,
                20,
                20,
                new ItemStack(Item.redstone),
                true,
                new Supplier<String>() {
                    @Override
                    public String get() {
                        return ((TileEntityAdvancedEnergyBlock) container.tileEntity).getMode();
                    }
                }
        ));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = this.container.tileEntity.getNameByTier();
        this.fontRenderer.drawString(name, (this.xSize - this.fontRenderer.getStringWidth(name)) / 2, 6, 4210752);
        this.fontRenderer.drawString(Translator.RESET.format("ic2.container.armor"), 8, this.ySize - 126 + 3, 4210752);
        this.fontRenderer.drawString(Translator.RESET.format("container.inventory"), 8, this.ySize - 96 + 3, 4210752);
        this.fontRenderer.drawString(Translator.RESET.format("ic2.container.electricBlock.level"), 79, 25, 4210752);

        int energy = Math.min(this.container.tileEntity.energy, this.container.tileEntity.maxStorage);
        this.fontRenderer.drawString(" " + energy, 110, 35, 4210752);
        this.fontRenderer.drawString("/" + this.container.tileEntity.maxStorage, 110, 45, 4210752);

        String output = StringTranslate.getInstance().translateKeyFormat(
                "ic2.container.electricBlock.output", this.container.tileEntity.output
        );
        this.fontRenderer.drawString(output, 85, 60, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        this.mc.renderEngine.bindTexture("/mods/ic2/textures/gui/GUIElectricBlock.png");

        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);

        if (this.container.tileEntity.energy > 0) {
            int i1 = (int) (24.0F * this.container.tileEntity.getChargeLevel());
            this.drawTexturedModalRect(j + 79, k + 34, 176, 14, i1 + 1, 16);
        }
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        super.actionPerformed(guibutton);
        if (guibutton.id == 0) {
            IC2.network.initiateClientTileEntityEvent(this.container.tileEntity, 0);
        }
    }

    public void drawTooltip(String tooltip, int mouseX, int mouseY) {
        this.drawCreativeTabHoveringText(tooltip, mouseX, mouseY);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (Object obj : this.buttonList) {
            if (obj instanceof GuiTooltipIconButton) {
                GuiTooltipIconButton btn = (GuiTooltipIconButton) obj;
                if (btn.hovered) {
                    btn.func_82251_b(mouseX, mouseY);
                }
            }
        }
    }
}

