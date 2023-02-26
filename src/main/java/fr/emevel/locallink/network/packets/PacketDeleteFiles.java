package fr.emevel.locallink.network.packets;

import fr.emevel.locallink.network.serial.ByteBufferWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PacketDeleteFiles implements Packet{

    private String folder;
    private List<String> files;

    @Override
    public void write(ByteBufferWrapper buffer) {
        buffer.putString(folder);
        buffer.putList(files);
    }

    @Override
    public void read(ByteBufferWrapper buffer) {
        folder = buffer.getString();
        files = buffer.getList(String::new);
    }

    @Override
    public int getSize() {
        return stringSize(folder) + listSize(files);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<PacketDeleteFiles> packets = new ArrayList<>();
        private PacketDeleteFiles current = null;
        private int maxFiles = 20;

        private void newCurrent(String name) {
            current = new PacketDeleteFiles(name, new ArrayList<>());
            packets.add(current);
        }

        public void addFile(String folder, String file) {
            if (current == null || current.getFiles().size() >= maxFiles) {
                newCurrent(folder);
            }
            current.getFiles().add(file);
        }

        public List<PacketDeleteFiles> build() {
            return packets;
        }

        public int size() {
            return packets.size();
        }

    }

}
