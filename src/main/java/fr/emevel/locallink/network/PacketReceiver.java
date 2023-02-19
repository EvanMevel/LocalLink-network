package fr.emevel.locallink.network;

import fr.emevel.locallink.network.serial.ByteBufferWrapper;
import fr.emevel.locallink.network.serial.PacketParsingException;
import fr.emevel.locallink.network.serial.PacketSerializer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public interface PacketReceiver {

    OutputStream getOutputStream();

    default void sendPacket(Packet packet) throws IOException, PacketParsingException {
        ByteBufferWrapper bufferWrapper = PacketSerializer.serialize(packet);
        byte[] buffer = bufferWrapper.array();

        getOutputStream().write(Signatures.SIGNATURE_BYTES);
        getOutputStream().write(ByteBuffer.allocate(Integer.BYTES).putInt(buffer.length).array());
        getOutputStream().write(buffer);
        getOutputStream().flush();
    }

    default void sendPacket(Iterable<Packet> packets) throws IOException, PacketParsingException {
        for (Packet packet : packets) {
            sendPacket(packet);
        }
    }

    default void sendPacket(Packet... packets) throws IOException, PacketParsingException {
        for (Packet packet : packets) {
            sendPacket(packet);
        }
    }

}
