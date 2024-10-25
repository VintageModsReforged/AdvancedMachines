package ic2.advancedmachines.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import ic2.advancedmachines.blocks.tiles.gui.GuiAdvancedMachine;
import ic2.advancedmachines.Refs;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        MinecraftForgeClient.preloadTexture(Refs.GUI_EXTRACTOR);
        MinecraftForgeClient.preloadTexture(Refs.GUI_INDUCTION);
        MinecraftForgeClient.preloadTexture(Refs.GUI_MACERATOR);
        MinecraftForgeClient.preloadTexture(Refs.GUI_COMPRESSOR);
        MinecraftForgeClient.preloadTexture(Refs.BLOCKS);
        MinecraftForgeClient.preloadTexture(Refs.ITEMS);
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }

    @Override
    public Object getGuiElementClient(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof TileEntityAdvancedMachine) {
            TileEntityAdvancedMachine advancedMachine = (TileEntityAdvancedMachine) te;
            return new GuiAdvancedMachine(player.inventory, advancedMachine);
        }
        return null;
    }
}
