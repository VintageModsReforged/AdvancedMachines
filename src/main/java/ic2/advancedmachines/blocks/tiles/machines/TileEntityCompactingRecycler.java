package ic2.advancedmachines.blocks.tiles.machines;

import ic2.advancedmachines.AdvancedMachinesConfig;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.advancedmachines.utils.AdvMachinesRecipes;
import ic2.advancedmachines.utils.LangHelper;
import ic2.api.item.Items;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotProcessable;
import ic2.core.block.invslot.InvSlotProcessableGeneric;
import ic2.core.block.machine.tileentity.TileEntityRecycler;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TileEntityCompactingRecycler extends TileEntityAdvancedMachine {
    public TileEntityCompactingRecycler() {
        super(LangHelper.format("block.advanced.recycler.name"), 3, AdvMachinesRecipes.recycler);
    }

    @Override
    public void addSlots() {
        // inputs
        this.inputs.add(new InvSlotProcessableGeneric(this, "inputA", 1, 1, this.recipeFilter));
        // outputs
        this.outputs.add(new InvSlotOutput(this, "outputA", 2, 1));
    }

    @Override
    public List<SlotInvSlot> getSlots() {
        List<SlotInvSlot> slots = new ArrayList<SlotInvSlot>();
        slots.add(new SlotInvSlot(this.inputs.get(0), 0, 56, 17));
        slots.add(new SlotInvSlot(this.outputs.get(0), 0, 115, 35));
        return slots;
    }

    @Override
    protected void operate() {
        if (canOperate()) {
            InvSlotProcessable input = this.inputs.get(0);
            InvSlotOutput output = this.outputs.get(0);
            ItemStack scrap = Items.getItem("scrap");
            boolean blackListed = TileEntityRecycler.getIsItemBlacklisted(input.get());
            --input.get().stackSize;
            if (input.get().stackSize <= 0) input.clear();

            if (this.worldObj.rand.nextInt(8) == 0 && !blackListed) {
                if (output.isEmpty()) {
                    output.put(scrap.copy());
                } else ++output.get().stackSize;
            }
        }
    }

    @Override
    public boolean canOperate() {
        InvSlotProcessable input = this.inputs.get(0);
        InvSlotOutput output = this.outputs.get(0);
        ItemStack scrap = Items.getItem("scrap");
        if (input.isEmpty()) {
            return false;
        } else return output.isEmpty() || output.get().isItemEqual(scrap) && output.get().stackSize < scrap.getMaxStackSize();
    }

    @Override
    public ItemStack getResultFor(ItemStack input, boolean adjustOutput) {
        return null;
    }

    @Override
    public String getStartSoundFile() {
        return AdvancedMachinesConfig.RECYCLER_WORK_SOUND;
    }

    @Override
    public String getSpeedName() {
        return LangHelper.format("inv.speed.recycler");
    }
}
