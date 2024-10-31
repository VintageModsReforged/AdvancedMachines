package ic2.advancedmachines.network;

import java.io.DataInputStream;

public interface IGuiListener {

    void receiveDescriptionData(int packetID, DataInputStream stream);
    void receiveGuiButton(int buttonId);
}
