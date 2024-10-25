package ic2.advancedmachines;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import ic2.advancedmachines.proxy.CommonProxy;
import ic2.advancedmachines.blocks.tiles.base.TileEntityAdvancedMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

@Mod(modid = Refs.ID, name = Refs.NAME, version = Refs.VERSION, acceptedMinecraftVersions = Refs.MC_VERSION, dependencies = Refs.DEPS)
@NetworkMod(clientSideRequired = true)
public class AdvancedMachines implements IGuiHandler {

    @SidedProxy(clientSide = Refs.PROXY_CLIENT, serverSide = Refs.PROXY_COMMON)
    public static CommonProxy proxy;

    public static AdvancedMachines INSTANCE;

    public AdvancedMachines() {}

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent e) {
        INSTANCE = this;
        proxy.preInit(e);
    }

    @Mod.Init
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
        NetworkRegistry.instance().registerGuiHandler(this, this);
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof TileEntityAdvancedMachine) {
            TileEntityAdvancedMachine advancedMachine = (TileEntityAdvancedMachine) te;
            return advancedMachine.getGuiContainer(player);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return proxy.getGuiElementClient(ID, player, world, x, y, z);
    }
}
