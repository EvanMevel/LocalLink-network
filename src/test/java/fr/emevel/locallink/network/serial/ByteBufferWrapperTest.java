package fr.emevel.locallink.network.serial;

import fr.emevel.locallink.network.TestPacket;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ByteBufferWrapperTest {

    @Test
    void testString() {
        String test = "This string is a test";
        ByteBufferWrapper buffer = new ByteBufferWrapper(1024);
        buffer.putString(test);

        buffer.flip();

        String result = buffer.getString();
        assertEquals(test, result);
    }

    @Test
    void testNetSerializable() {
        TestPacket test = new TestPacket(42);
        ByteBufferWrapper buffer = new ByteBufferWrapper(1024);
        buffer.putSerializable(test);

        buffer.flip();

        TestPacket result = buffer.getSerializable(TestPacket::new);
        assertEquals(test, result);
    }

    @Test
    void testStringList() {
        List<String> test = new ArrayList<>();
        test.add("This string is a test");
        test.add("This string is also a test");

        ByteBufferWrapper buffer = new ByteBufferWrapper(1024);
        buffer.putList(test);

        buffer.flip();

        List<String> result = buffer.getList(String::new);
        assertEquals(test, result);
    }

    @Test
    void testNetSerializableList() {
        List<TestPacket> test = new ArrayList<>();
        test.add(new TestPacket(42));
        test.add(new TestPacket(43));

        ByteBufferWrapper buffer = new ByteBufferWrapper(1024);
        buffer.putList(test);

        buffer.flip();

        List<TestPacket> result = buffer.getList(TestPacket::new);
        assertEquals(test, result);
    }
}