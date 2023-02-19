package fr.emevel.locallink.network.serial;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;

public interface NetworkSerializable extends Serializable {

    void write(ByteBufferWrapper buffer);

    void read(ByteBufferWrapper buffer);

    int getSize();

    default int stringSize(String string) {
        return sizeString(string);
    }

    default <T> int listSize(List<T> list) {
        return sizeList(list);
    }

    static int sizeString(String string) {
        return Integer.BYTES + string.getBytes(StandardCharsets.UTF_8).length;
    }

    static <T> int sizeList(List<T> list) {
        int size = Integer.BYTES;
        for (T element : list) {
            if (element instanceof NetworkSerializable) {
                size += ((NetworkSerializable) element).getSize();
            } else if (element instanceof String) {
                size += sizeString((String) element);
            } else {
                throw new IllegalArgumentException("Cannot get size of list of " + element.getClass().getName());
            }
        }
        return size;
    }

}
