package ic2.advancedmachines.utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.GuiIconButton;
import ic2.core.gui.GuiAdvEnergyBlock;
import mods.vintage.core.utils.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

@SideOnly(Side.CLIENT)
public class GuiTooltipIconButton extends GuiIconButton {
    private final Supplier<String> tooltip;
    private final GuiAdvEnergyBlock gui;
    public boolean hovered;

    public GuiTooltipIconButton(GuiAdvEnergyBlock gui, int id, int x, int y, int w, int h, ItemStack icon, boolean drawQuantity, Supplier<String> tooltip) {
        super(id, x, y, w, h, icon, drawQuantity);
        this.gui = gui;
        this.tooltip = tooltip;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        super.drawButton(mc, mouseX, mouseY);

        // Set hover state
        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition &&
                mouseX < this.xPosition + this.width &&
                mouseY < this.yPosition + this.height;
    }

    @Override
    public void func_82251_b(int mouseX, int mouseY) {
        gui.drawTooltip(this.tooltip.get(), mouseX, mouseY);
    }
}
