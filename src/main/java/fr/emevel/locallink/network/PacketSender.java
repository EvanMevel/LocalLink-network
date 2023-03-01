package fr.emevel.locallink.network;

import fr.emevel.locallink.network.packets.Packet;
import fr.emevel.locallink.network.serial.ByteBufferWrapper;
import fr.emevel.locallink.network.serial.PacketMalformedException;
import fr.emevel.locallink.network.serial.PacketSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public interface PacketSender {

    InputStream getInputStream();

    default Packet receivePacket() throws IOException, PacketMalformedException {
        byte read = (byte) getInputStream().read();
        if (read == -1) {
            throw new ConnectionClosedException();
        }
        if (read != Signatures.SIGNATURE) {
            throw new PacketMalformedException("Invalid signature");
        }
        ByteBuffer sizeBuffer = ByteBuffer.allocate(Integer.BYTES);
        int sizeRead = getInputStream().read(sizeBuffer.array(), 0, Integer.BYTES);
        if (sizeRead != Integer.BYTES) {
            throw new PacketMalformedException("Invalid packet size");
        }
        int size = sizeBuffer.getInt();
        if (size < 0) {
            throw new PacketMalformedException("Invalid packet size");
        }
        ByteBufferWrapper packetBuffer = new ByteBufferWrapper(size);
        int packetRead = getInputStream().read(packetBuffer.array());
        if (packetRead != size) {
            throw new PacketMalformedException("Invalid packet size");
        }
        return PacketSerializer.deserialize(packetBuffer);
    }

}
