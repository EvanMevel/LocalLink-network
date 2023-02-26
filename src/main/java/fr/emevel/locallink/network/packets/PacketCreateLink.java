package fr.emevel.locallink.network.packets;

import fr.emevel.locallink.network.serial.ByteBufferWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PacketCreateLink implements Packet{

    private UUID folderUuid;
    private String folderName;

    @Override
    public void write(ByteBufferWrapper buffer) {
        buffer.putUUID(folderUuid);
        buffer.putString(folderName);
    }

    @Override
    public void read(ByteBufferWrapper buffer) {
        folderUuid = buffer.getUUID();
        folderName = buffer.getString();
    }

    @Override
    public int getSize() {
        return UUID_SIZE + stringSize(folderName);
    }
}
