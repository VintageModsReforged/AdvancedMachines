package ic2.advancedmachines.blocks.tiles;

import ic2.advancedmachines.AdvancedMachinesConfig;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.advancedmachines.utils.LangHelper;
import ic2.advancedmachines.utils.SlotFiltered;
import ic2.api.Ic2Recipes;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TileEntitySingularityCompressor extends TileEntityAdvancedMachine {
    public TileEntitySingularityCompressor() {
        super(LangHelper.format("block.advanced.compressor.name"), new int[]{1}, new int[]{2});
    }

    @Override
    public ItemStack getResultFor(ItemStack input, boolean adjustOutput) {
        return Ic2Recipes.getCompressorOutputFor(input, adjustOutput);
    }

    @Override
    public List<Slot> getSlots(InventoryPlayer playerInv) {
        List<Slot> slots = new ArrayList<Slot>();
        slots.add(SlotFiltered.compressorSlot(this, 1, 56, 17));
        slots.add(new SlotFurnace(playerInv.player, this, 2, 115, 35));
        return slots;
    }

    @Override
    public int[] getUpgradeSlots() {
        return new int[] { 3, 4 };
    }

    @Override
    public String getStartSoundFile() {
        return AdvancedMachinesConfig.COMPRESSOR_WORK_SOUND;
    }

    @Override
    public String getSpeedName() {
        return LangHelper.format("inv.speed.compressor");
    }
}