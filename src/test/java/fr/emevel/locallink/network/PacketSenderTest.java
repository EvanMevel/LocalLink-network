package fr.emevel.locallink.network;

import fr.emevel.locallink.network.serial.PacketMalformedException;
import fr.emevel.locallink.network.serial.PacketParsingException;
import fr.emevel.locallink.network.serial.PacketSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.*;

class PacketSenderTest {

    ByteArrayOutputStream bos = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        PacketSerializer.registerPacket(-1, TestPacket.class);
    }

    @Test
    void testReceivePacket() throws IOException, PacketParsingException, PacketMalformedException {
        PacketReceiver receiver = new PacketReceiver() {
            @Override
            public OutputStream getOutputStream() {
                return bos;
            }

            @Override
            public boolean isClosed() {
                return false;
            }
        };
        TestPacket test = new TestPacket(42);
        receiver.sendPacket(test);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        PacketSender sender = new PacketSender() {
            @Override
            public ByteArrayInputStream getInputStream() {
                return bis;
            }
        };
        TestPacket packet = (TestPacket) sender.receivePacket();
        assertEquals(42, packet.test);
    }
}