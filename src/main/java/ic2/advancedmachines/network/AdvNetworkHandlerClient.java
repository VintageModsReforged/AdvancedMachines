package ic2.advancedmachines.network;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import ic2.advancedmachines.AdvancedMachines;
import ic2.advancedmachines.utils.Refs;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.io.*;

public class AdvNetworkHandlerClient extends AdvNetworkHandler {

    @Override
    public void onPacketData(INetworkManager iNetworkManager, Packet250CustomPayload packet, Player player) {
        if (packet.data.length != 0) {
            DataInputStream buffer = new DataInputStream(new ByteArrayInputStream(packet.data));
            int packetType = -1;
            int x = 0;
            int y = 0;
            int z = 0;
            try {
                packetType = buffer.readInt();
                x = buffer.readInt();
                y = buffer.readInt();
                z = buffer.readInt();
            } catch (IOException e) {
                AdvancedMachines.LOGGER.info(String.format("Failed to read packet from server! More info: %s", e));
            }

            if (packetType == 0) {
                Exception e;
                try {
                    World world = FMLClientHandler.instance().getClient().theWorld;
                    TileEntity tile = world.getBlockTileEntity(x, y, z);
                    if (tile instanceof IGuiListener) {
                        IGuiListener listener = (IGuiListener) tile;
                        listener.receiveDescriptionData(0, buffer);
                    }
                }
                catch (ClassCastException ex) { e = ex; }
                catch (NullPointerException ex) { e = ex; }
            }
        }
    }

    @Override
    public void sendTileEntityGuiButtonUpdate(TileEntity tile, int buttonId) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(buffer);
        try {
            outputStream.writeInt(0);
            outputStream.writeInt(tile.xCoord);
            outputStream.writeInt(tile.yCoord);
            outputStream.writeInt(tile.zCoord);
            outputStream.writeInt(buttonId);
            outputStream.close();
            Packet250CustomPayload packet = new Packet250CustomPayload();
            packet.channel = Refs.ID;
            packet.isChunkDataPacket = false;
            packet.data = buffer.toByteArray();
            packet.length = packet.data.length;
            PacketDispatcher.sendPacketToServer(packet);
        } catch (IOException e) {
            AdvancedMachines.LOGGER.info(String.format("Client failed to create packet for [%s, %s, %s]. More details: %s", tile.xCoord, tile.yCoord, tile.zCoord, e));
        }
    }
}
