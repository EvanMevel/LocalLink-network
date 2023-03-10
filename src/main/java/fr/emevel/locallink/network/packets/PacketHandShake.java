package fr.emevel.locallink.network.packets;

import fr.emevel.locallink.network.serial.ByteBufferWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PacketHandShake implements Packet {

    private String name;
    private UUID uuid;
    private String version;

    @Override
    public void write(ByteBufferWrapper buffer) {
        buffer.putString(name);
        buffer.putUUID(uuid);
        buffer.putString(version);
    }

    @Override
    public void read(ByteBufferWrapper buffer) {
        name = buffer.getString();
        uuid = buffer.getUUID();
        version = buffer.getString();
    }

    @Override
    public int getSize() {
        return stringSize(name) + UUID_SIZE + stringSize(version);
    }
}
