package ic2.advancedmachines.blocks.tiles.machines;

import ic2.advancedmachines.AdvancedMachinesConfig;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.advancedmachines.utils.AdvSlot;
import ic2.advancedmachines.utils.LangHelper;
import ic2.advancedmachines.utils.StackFilters;
import ic2.api.Ic2Recipes;
import ic2.api.Items;
import ic2.core.item.ItemScrapbox;
import ic2.core.util.StackUtil;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TileEntityCentrifugeExtractor extends TileEntityAdvancedMachine {
    public TileEntityCentrifugeExtractor() {
        super(LangHelper.format("block.advanced.extractor.name"), new int[]{1}, new int[]{2, 3, 4}, StackFilters.EXTRACTOR_FILTER, "Extractor");
    }

    @Override
    public List<Slot> getSlots(InventoryPlayer playerInv) {
        List<Slot> slots = new ArrayList<Slot>();
        slots.add(AdvSlot.filtered(this, 1, 56, 17));
        slots.add(new SlotFurnace(playerInv.player, this, 2, 115, 17));
        slots.add(new SlotFurnace(playerInv.player, this, 3, 115, 35));
        slots.add(new SlotFurnace(playerInv.player, this, 4, 115, 53));
        return slots;
    }

    @Override
    public int[] getUpgradeSlots() {
        return new int[] { 5, 6 };
    }

    @Override
    public ItemStack getResultFor(ItemStack input, boolean adjustOutput) {
        if (StackUtil.isStackEqual(input, Items.getItem("scrapBox"))) {
            if (adjustOutput) {
                --input.stackSize;
            }
            return ItemScrapbox.getDrop(this.worldObj);
        } else return Ic2Recipes.getExtractorOutputFor(input, adjustOutput);
    }

    @Override
    public String getStartSoundFile() {
        return AdvancedMachinesConfig.EXTRACTOR_WORK_SOUND;
    }

    @Override
    public String getSpeedName() {
        return LangHelper.format("inv.speed.extractor");
    }
}
