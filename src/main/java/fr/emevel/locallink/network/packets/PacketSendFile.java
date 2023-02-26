package fr.emevel.locallink.network.packets;

import fr.emevel.locallink.network.serial.ByteBufferWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PacketSendFile implements Packet {

    private UUID folder;
    private String name;
    private int port;
    private long length;

    @Override
    public void write(ByteBufferWrapper buffer) {
        buffer.putUUID(folder);
        buffer.putString(name);
        buffer.putInt(port);
        buffer.putLong(length);
    }

    @Override
    public void read(ByteBufferWrapper buffer) {
        folder = buffer.getUUID();
        name = buffer.getString();
        port = buffer.getInt();
        length = buffer.getLong();
    }

    @Override
    public int getSize() {
        return UUID_SIZE + stringSize(name) + Integer.BYTES + Long.BYTES;
    }
}
