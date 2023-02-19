package fr.emevel.locallink.network.serial;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ByteBufferWrapper {

    private final ByteBuffer buffer;

    public ByteBufferWrapper(int size) {
        this.buffer = ByteBuffer.allocate(size);
    }

    public void flip() {
        buffer.flip();
    }

    public byte[] array() {
        return buffer.array();
    }

    public void put(byte[] bytes) {
        buffer.put(bytes);
    }

    public byte[] get(int size) {
        byte[] bytes = new byte[size];
        buffer.get(bytes);
        return bytes;
    }

    public void putString(String string) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        buffer.putInt(bytes.length);
        buffer.put(bytes);
    }

    public String getString() {
        int size = buffer.getInt();
        byte[] bytes = new byte[size];
        buffer.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public void putInt(int i) {
        buffer.putInt(i);
    }

    public int getInt() {
        return buffer.getInt();
    }

    public void putLong(long l) {
        buffer.putLong(l);
    }

    public long getLong() {
        return buffer.getLong();
    }

    public void putDouble(double d) {
        buffer.putDouble(d);
    }

    public double getDouble() {
        return buffer.getDouble();
    }

    public void putFloat(float f) {
        buffer.putFloat(f);
    }

    public float getFloat() {
        return buffer.getFloat();
    }

    public void putBoolean(boolean b) {
        buffer.put((byte) (b ? 1 : 0));
    }

    public boolean getBoolean() {
        return buffer.get() != 0;
    }

    public <T extends NetworkSerializable> void putSerializable(T serializable) {
        serializable.write(this);
    }

    public <T extends NetworkSerializable> T getSerializable(Supplier<T> sup) {
        T t = sup.get();
        t.read(this);
        return t;
    }

    /**
     * We cant cast a T parameter to a T parameter extending a class, so we need to use a raw type and cast.
     * It is dirty so this method should only be used when we are sure that the type is a NetworkSerializable.
     * @param sup the supplier of the object to read
     * @return the object read
     * @param <T> NetworkSerializable the type of the object to read
     */
    @SuppressWarnings("unchecked")
    private <T> T getSerializable0(Supplier<T> sup) {
        NetworkSerializable t = (NetworkSerializable) sup.get();
        t.read(this);
        return (T) t;
    }

    @SuppressWarnings("unchecked")
    public <T> void putList(List<T> list) {
        buffer.putInt(list.size());
        if (list.isEmpty()) {
            return;
        }
        Consumer<T> consumer = null;
        T first = list.get(0);
        if (first instanceof String) {
            consumer = (Consumer<T>) ((Consumer<String>) this::putString);
        } else if (first instanceof NetworkSerializable) {
            consumer = (Consumer<T>) ((Consumer<NetworkSerializable>) this::putSerializable);
        }
        if (consumer == null) {
            throw new IllegalArgumentException("Unsupported list type: " + first.getClass());
        }
        for (T t : list) {
            consumer.accept(t);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getList(Supplier<T> sup) {
        int size = buffer.getInt();
        if (size == 0) {
            return List.of();
        }
        Supplier<T> supplier;
        T first = sup.get();
        if (first instanceof String) {
            supplier = (Supplier<T>) ((Supplier<String>) this::getString);
        } else if (first instanceof NetworkSerializable) {
            supplier = () -> getSerializable0(sup);
        } else {
            throw new IllegalArgumentException("Unsupported list type: " + first.getClass());
        }
        List<T> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(supplier.get());
        }
        return list;
    }

}
