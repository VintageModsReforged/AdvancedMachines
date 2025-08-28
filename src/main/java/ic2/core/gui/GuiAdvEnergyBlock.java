package ic2.core.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedEnergyBlock;
import ic2.advancedmachines.blocks.tiles.container.ContainerAdvancedElectricBlock;
import ic2.advancedmachines.utils.GuiTooltipIconButton;
import ic2.advancedmachines.utils.Refs;
import ic2.core.GuiIconButton;
import ic2.core.IC2;
import mods.vintage.core.platform.lang.Translator;
import mods.vintage.core.utils.function.Supplier;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringTranslate;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiAdvEnergyBlock extends GuiContainer {
    ContainerAdvancedElectricBlock container;

    public GuiAdvEnergyBlock(ContainerAdvancedElectricBlock container) {
        super(container);
        this.container = container;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        super.initGui();
        this.controlList.add(new GuiTooltipIconButton(
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
                        return container.TILE.getMode();
                    }
                }
        ));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = this.container.TILE.getNameByTier();
        this.fontRenderer.drawString(name, (this.xSize - this.fontRenderer.getStringWidth(name)) / 2, 6, 4210752);
        this.fontRenderer.drawString(Translator.RESET.format("container.inventory"), 28, this.ySize - 96 + 2, 4210752);
        this.fontRenderer.drawString(Translator.RESET.format("inv.electric.level"), 79, 25, 4210752);

        int energy = Math.min(this.container.TILE.energy, this.container.TILE.maxStorage);
        this.fontRenderer.drawString(" " + energy, 110, 35, 4210752);
        this.fontRenderer.drawString("/" + this.container.TILE.maxStorage, 110, 45, 4210752);

        String output = StringTranslate.getInstance().translateKeyFormat(
                "inv.electric.output", this.container.TILE.output
        );
        this.fontRenderer.drawString(output, 85, 60, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        int i = this.mc.renderEngine.getTexture(Refs.getGuiPath("electric"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i);

        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);

        if (this.container.TILE.energy > 0) {
            int i1 = (int) (24.0F * this.container.TILE.getChargeLevel());
            this.drawTexturedModalRect(j + 79, k + 34, 176, 14, i1 + 1, 16);
        }
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        if (guibutton.id == 0) {
            IC2.network.initiateClientTileEntityEvent(this.container.TILE, 0);
        }
        super.actionPerformed(guibutton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (Object obj : this.controlList) {
            if (obj instanceof GuiTooltipIconButton) {
                GuiTooltipIconButton btn = (GuiTooltipIconButton) obj;
                if (btn.hovered) {
                    btn.func_82251_b(mouseX, mouseY);
                }
            }
        }
    }

    public void drawTooltip(String tooltip, int mouseX, int mouseY) {
        this.drawCreativeTabHoveringText(tooltip, mouseX, mouseY);
    }
}
