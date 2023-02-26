package fr.emevel.locallink.network.packets;

import fr.emevel.locallink.network.serial.ByteBufferWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PacketSendFile implements Packet {

    private String folder;
    private String name;
    private int port;
    private long length;

    @Override
    public void write(ByteBufferWrapper buffer) {
        buffer.putString(folder);
        buffer.putString(name);
        buffer.putInt(port);
        buffer.putLong(length);
    }

    @Override
    public void read(ByteBufferWrapper buffer) {
        folder = buffer.getString();
        name = buffer.getString();
        port = buffer.getInt();
        length = buffer.getLong();
    }

    @Override
    public int getSize() {
        return stringSize(folder) + stringSize(name) + Integer.BYTES + Long.BYTES;
    }
}
