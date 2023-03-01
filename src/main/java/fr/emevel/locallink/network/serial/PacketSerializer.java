package fr.emevel.locallink.network.serial;

import fr.emevel.locallink.network.packets.Packet;
import fr.emevel.locallink.network.packets.*;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.lang.reflect.InvocationTargetException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;

public class PacketSerializer {

    private static final BidiMap<Integer, Class<? extends Packet>> packets = new DualHashBidiMap<>();

    public static void registerPacket(int id, Class<? extends Packet> packet) {
        packets.put(id, packet);
    }

    public static Packet deserialize(ByteBufferWrapper buffer) {
        int id = buffer.getInt();
        Class<? extends Packet> packetClass = packets.get(id);
        if (packetClass == null) {
            throw new PacketParsingException("Unknown packet id: " + id);
        }
        try {
            Packet packet = packetClass.getDeclaredConstructor().newInstance();
            packet.read(buffer);
            return packet;
        } catch (InstantiationException e) {
            throw new PacketParsingException("Packet with class " + packetClass.getName() + " is abstract", e);
        } catch (InvocationTargetException e) {
            throw new PacketParsingException("Error while instantiating packet class " + packetClass.getName(), e.getCause());
        } catch (NoSuchMethodException e) {
            throw new PacketParsingException("Packet class " + packetClass.getName() + " does not have a default constructor");
        } catch (IllegalAccessException e) {
            throw new PacketParsingException("Packet class " + packetClass.getName() + " does not have a public default constructor");
        } catch (BufferUnderflowException e) {
            throw new PacketParsingException("Packet seems to be malformed", e);
        }
    }

    public static void serialize(Packet packet, ByteBufferWrapper buffer) {
        Integer id = packets.getKey(packet.getClass());
        if (id == null) {
            throw new PacketParsingException("Unknown packet class: " + packet.getClass().getName());
        }
        try {
            buffer.putInt(id);
            packet.write(buffer);
        } catch (BufferOverflowException e) {
            throw new PacketParsingException("Packet seems to be malformed", e);
        }
    }

    public static ByteBufferWrapper serialize(Packet packet) {
        Integer id = packets.getKey(packet.getClass());
        if (id == null) {
            throw new PacketParsingException("Unknown packet class: " + packet.getClass().getName());
        }
        ByteBufferWrapper buffer = new ByteBufferWrapper(packet.getSize() + Integer.BYTES);
        try {
            buffer.putInt(id);
            packet.write(buffer);
        } catch (BufferOverflowException e) {
            throw new PacketParsingException("Packet seems to be malformed", e);
        }
        return buffer;
    }

    static {
        registerPacket(0, PacketHandShake.class);
        registerPacket(1, PacketAskFolders.class);
        registerPacket(2, PacketFolderList.class);
        registerPacket(3, PacketAskFiles.class);
        registerPacket(4, PacketFileList.class);
        registerPacket(5, PacketSendFile.class);
        registerPacket(6, PacketDeleteFiles.class);
        registerPacket(7, PacketCreateLink.class);
    }

}
