package ic2.advancedmachines.integration.waila;

import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import ic2.advancedmachines.blocks.tiles.machines.TileEntityCentrifugeExtractor;
import ic2.advancedmachines.blocks.tiles.machines.TileEntityCompactingRecycler;
import ic2.advancedmachines.blocks.tiles.machines.TileEntityRotaryMacerator;
import ic2.advancedmachines.blocks.tiles.machines.TileEntitySingularityCompressor;
import mods.vintage.core.platform.lang.Translator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import reforged.mods.blockhelper.addons.utils.ColorUtils;
import reforged.mods.blockhelper.addons.utils.InfoProvider;
import reforged.mods.blockhelper.addons.utils.interfaces.IWailaHelper;

public class AdvancedMachinesInfoProvider extends InfoProvider {

    public static final AdvancedMachinesInfoProvider THIS = new AdvancedMachinesInfoProvider();

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer entityPlayer) {
        if (blockEntity instanceof TileEntityAdvancedMachine) {
            TileEntityAdvancedMachine advMachine = (TileEntityAdvancedMachine) blockEntity;
            text(helper, usage(advMachine.energyUsage));
            String speedName;
            if (advMachine instanceof TileEntityRotaryMacerator) {
                speedName = "info.speed.rotation";
            } else if (advMachine instanceof TileEntitySingularityCompressor) {
                speedName = "info.speed.pressure";
            } else if (advMachine instanceof TileEntityCentrifugeExtractor || advMachine instanceof TileEntityCompactingRecycler) {
                speedName = "info.speed.speed";
            } else {
                speedName = "info.speed.heat";
            }
            int speed = advMachine.speed;
            int maxSpeed = advMachine.maxSpeed;
            if (speed > 0)
                bar(helper, speed, maxSpeed, translate(speedName, speed * 100 / maxSpeed), -295680);
            int progress = advMachine.progress;
            if (progress > 0) {
                int operationsPerTick = advMachine.speed / 30;
                int scaledOp = (int) Math.min(6.0E7F, (float) advMachine.progress / operationsPerTick);
                int scaledMaxOp = (int) Math.min(6.0E7F, (float) advMachine.maxProgress / operationsPerTick);
                bar(helper, scaledOp, scaledMaxOp, Translator.WHITE.format("info.progress.full", scaledOp, scaledMaxOp).concat("t"), ColorUtils.PROGRESS);
            }
        }
    }
}
