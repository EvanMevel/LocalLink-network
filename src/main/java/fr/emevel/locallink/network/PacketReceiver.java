package fr.emevel.locallink.network;

import fr.emevel.locallink.network.packets.Packet;
import fr.emevel.locallink.network.serial.ByteBufferWrapper;
import fr.emevel.locallink.network.serial.PacketParsingException;
import fr.emevel.locallink.network.serial.PacketSerializer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

public interface PacketReceiver {

    OutputStream getOutputStream();

    boolean isClosed();

    default void sendPacket(Packet packet) throws IOException {
        System.out.println("Sending packet " + packet);

        int packetSize = packet.getSize();

        int size = Byte.BYTES // Signature
                + Integer.BYTES // Size
                + Integer.BYTES // Packet ID
                + packetSize; // Packet size

        ByteBufferWrapper bufferWrapper = new ByteBufferWrapper(size);

        bufferWrapper.put(Signatures.SIGNATURE_BYTES); // Signature
        bufferWrapper.putInt(Integer.BYTES + packetSize); // Size

        PacketSerializer.serialize(packet, bufferWrapper); // Packet ID + Packet data

        byte[] buffer = bufferWrapper.array();

        getOutputStream().write(buffer);
        getOutputStream().flush();
    }

    default void sendPacket(Iterable<? extends Packet> packets) throws IOException {
        for (Packet packet : packets) {
            sendPacket(packet);
        }
    }

    default void sendPacket(Packet... packets) throws IOException {
        for (Packet packet : packets) {
            sendPacket(packet);
        }
    }

    default void safeSendPacket(Packet packet) {
        try {
            sendPacket(packet);
        } catch (SocketException e) {
            if (isClosed()) {
                return;
            }
            System.err.println("Socket exception while sending packet " + packet);
            e.printStackTrace();
        } catch (IOException e) {
            if (isClosed()) {
                return;
            }
            System.err.println("IO exception while sending packet " + packet);
            e.printStackTrace();
        } catch (PacketParsingException e) {
            System.err.println("Packet parsing error, this is likely a developer error. Please report this to the developer.");
            e.printStackTrace();
        }
    }

    default void safeSendPacket(Packet... packets) {
        for (Packet packet : packets) {
            safeSendPacket(packet);
        }
    }

    default void safeSendPacket(Iterable<? extends Packet> packets) {
        for (Packet packet : packets) {
            safeSendPacket(packet);
        }
    }

}
