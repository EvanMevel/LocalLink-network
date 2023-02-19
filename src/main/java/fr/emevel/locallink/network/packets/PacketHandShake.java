package fr.emevel.locallink.network.packets;

import fr.emevel.locallink.network.Packet;
import fr.emevel.locallink.network.serial.ByteBufferWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PacketHandShake implements Packet {

    private String name;
    private String version;

    @Override
    public void write(ByteBufferWrapper buffer) {
        buffer.putString(name);
        buffer.putString(version);
    }

    @Override
    public void read(ByteBufferWrapper buffer) {
        name = buffer.getString();
        version = buffer.getString();
    }

    @Override
    public int getSize() {
        return stringSize(name) + stringSize(version);
    }
}
