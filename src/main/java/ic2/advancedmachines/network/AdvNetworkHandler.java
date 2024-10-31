package ic2.advancedmachines.network;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import ic2.advancedmachines.AdvancedMachines;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class AdvNetworkHandler implements IPacketHandler {

    @Override
    public void onPacketData(INetworkManager iNetworkManager, Packet250CustomPayload packet, Player player) {
        if (packet.data.length != 0) {
            DataInputStream buffer = new DataInputStream(new ByteArrayInputStream(packet.data));
            int packetType = -1;
            int x = 0;
            int y = 0;
            int z = 0;
            int buttonId = -1;
            try {
                packetType = buffer.readInt();
                x = buffer.readInt();
                y = buffer.readInt();
                z = buffer.readInt();
                buttonId = buffer.readInt();
            } catch (IOException e) {
                AdvancedMachines.LOGGER.info(String.format("Failed to read packet from server! More info: %s", e));
            }

            if (packetType == 0) {
                try {
                    World world = ((EntityPlayerMP)player).worldObj;
                    TileEntity tile = world.getBlockTileEntity(x, y, z);
                    if (tile instanceof IGuiListener) {
                        IGuiListener listener = (IGuiListener) tile;
                        listener.receiveGuiButton(buttonId);
                    }
                }
                catch (ClassCastException ignored) {}
                catch (NullPointerException ignored) {}
            }
        }
    }

    public void sendTileEntityGuiButtonUpdate(TileEntity tile, int buttonId) {}
}
