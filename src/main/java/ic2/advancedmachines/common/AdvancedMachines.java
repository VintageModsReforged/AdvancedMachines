package ic2.advancedmachines.common;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import ic2.advancedmachines.client.GuiAdvancedMachine;
import ic2.advancedmachines.common.tiles.TileEntityAdvancedInduction;
import ic2.advancedmachines.common.tiles.TileEntityCentrifugeExtractor;
import ic2.advancedmachines.common.tiles.TileEntityRotaryMacerator;
import ic2.advancedmachines.common.tiles.TileEntitySingularityCompressor;
import ic2.advancedmachines.common.tiles.base.TileEntityAdvancedMachine;
import ic2.advancedmachines.lang.LangHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

@Mod(modid = "AdvancedMachines", name = "IC2 Advanced Machines Addon", version = "4.7e", dependencies = "required-after:IC2")
@NetworkMod(clientSideRequired = true)
public class AdvancedMachines implements IGuiHandler, IProxy {

    @SidedProxy(clientSide = "ic2.advancedmachines.client.AdvancedMachinesClient", serverSide = "ic2.advancedmachines.common.AdvancedMachines")
    public static IProxy proxy;

    public static AdvancedMachines INSTANCE;

    public AdvancedMachines() {}

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent e) {
        INSTANCE = this;
        AdvancedMachinesConfig.init();
        LangHelper.init();
        BlocksItems.init();
    }

    @Mod.Init
    public void init(FMLInitializationEvent e) {
        proxy.load();
        NetworkRegistry.instance().registerGuiHandler(this, this);
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent e) {
        AdvancedMachinesRecipes.init();
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof TileEntityAdvancedMachine) {
            TileEntityAdvancedMachine advancedMachine = (TileEntityAdvancedMachine) te;
            return advancedMachine.getGuiContainer(player.inventory);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return proxy.getGuiElementForClient(ID, player, world, x, y, z);
    }

    // PROXY SERVERSIDE NOOP METHODS
    @Override
    public void load() {
        // NOOP
    }

    @Override
    public Object getGuiElementForClient(int ID, EntityPlayer player, World world, int x, int y, int z) {
        // NOOP
        return null;
    }
}
