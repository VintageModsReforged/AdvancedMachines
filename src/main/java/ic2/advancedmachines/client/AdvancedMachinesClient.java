package ic2.advancedmachines.client;

import ic2.advancedmachines.common.IProxy;
import ic2.advancedmachines.common.Refs;
import ic2.advancedmachines.common.tiles.base.TileEntityAdvancedMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public class AdvancedMachinesClient implements IProxy {

    @Override
    public void load() {
        MinecraftForgeClient.preloadTexture(Refs.GUI_EXTRACTOR);
        MinecraftForgeClient.preloadTexture(Refs.GUI_INDUCTION);
        MinecraftForgeClient.preloadTexture(Refs.GUI_MACERATOR);
        MinecraftForgeClient.preloadTexture(Refs.GUI_COMPRESSOR);
        MinecraftForgeClient.preloadTexture(Refs.BLOCKS);
        MinecraftForgeClient.preloadTexture(Refs.ITEMS);
    }

    @Override
    public Object getGuiElementForClient(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof TileEntityAdvancedMachine) {
            TileEntityAdvancedMachine advancedMachine = (TileEntityAdvancedMachine) te;
            return new GuiAdvancedMachine(player.inventory, advancedMachine);
        }
        return null;
    }
}
