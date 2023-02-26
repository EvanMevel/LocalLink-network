package fr.emevel.locallink.network.packets;

import fr.emevel.locallink.network.serial.ByteBufferWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PacketAskFiles implements Packet {

    private String folder = "";

    @Override
    public void write(ByteBufferWrapper buffer) {
        buffer.putString(folder);
    }

    @Override
    public void read(ByteBufferWrapper buffer) {
        folder = buffer.getString();
    }

    @Override
    public int getSize() {
        return stringSize(folder);
    }
}
