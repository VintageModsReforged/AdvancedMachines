package ic2.advancedmachines.blocks.tiles.machines;

import ic2.advancedmachines.AdvancedMachinesConfig;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.advancedmachines.utils.AdvSlot;
import ic2.advancedmachines.utils.StackFilters;
import ic2.api.Ic2Recipes;
import mods.vintage.core.helpers.LangHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TileEntityRotaryMacerator extends TileEntityAdvancedMachine {

    public TileEntityRotaryMacerator() {
        super(LangHelper.format("block.advanced.macerator.name"), new int[]{1}, new int[]{2, 3}, StackFilters.MACERATOR_FILTER, "Macerator");
    }

    @Override
    public ItemStack getResultFor(ItemStack macerated, boolean adjustOutput) {
        return Ic2Recipes.getMaceratorOutputFor(macerated, adjustOutput);
    }

    @Override
    public List<Slot> getSlots(InventoryPlayer playerInv) {
        List<Slot> slots = new ArrayList<Slot>();
        slots.add(AdvSlot.filtered(this, 1, 56, 17));
        slots.add(new SlotFurnace(playerInv.player, this, 2, 115, 25)); // left Result Slot
        slots.add(new SlotFurnace(playerInv.player, this, 3, 115, 46)); // right Result Slot
        return slots;
    }

    @Override
    public int[] getUpgradeSlots() {
        return new int[] { 4, 5 };
    }

    @Override
    public String getStartSoundFile() {
        return AdvancedMachinesConfig.MACERATOR_WORK_SOUND;
    }

    @Override
    public String getSpeedName() {
        return LangHelper.format("inv.speed.macerator");
    }
}
