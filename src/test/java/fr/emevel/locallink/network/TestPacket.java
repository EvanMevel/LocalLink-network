package fr.emevel.locallink.network;

import fr.emevel.locallink.network.packets.Packet;
import fr.emevel.locallink.network.serial.ByteBufferWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TestPacket implements Packet {
    int test;

    @Override
    public int getSize() {
        return Integer.BYTES;
    }

    @Override
    public void write(ByteBufferWrapper buffer) {
        buffer.putInt(test);
    }

    @Override
    public void read(ByteBufferWrapper buffer) {
        test = buffer.getInt();
    }
}
