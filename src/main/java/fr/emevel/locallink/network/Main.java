package fr.emevel.locallink.network;

import fr.emevel.locallink.network.serial.ByteBufferWrapper;
import fr.emevel.locallink.network.serial.PacketMalformedException;
import fr.emevel.locallink.network.serial.PacketParsingException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;

public class Main {

    @AllArgsConstructor
    @NoArgsConstructor
    public static class Test implements Packet {
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

    public static void main(String[] args) throws IOException, PacketParsingException, PacketMalformedException {

    }
}