package fr.emevel.locallink.network.packets;

import fr.emevel.locallink.network.serial.ByteBufferWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PacketLinkCreated implements Packet {

    private UUID folder;

    @Override
    public void write(ByteBufferWrapper buffer) {
        buffer.putUUID(folder);
    }

    @Override
    public void read(ByteBufferWrapper buffer) {
        folder = buffer.getUUID();
    }

    @Override
    public int getSize() {
        return UUID_SIZE;
    }
}
