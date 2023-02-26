package fr.emevel.locallink.network.packets;

import fr.emevel.locallink.network.serial.ByteBufferWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PacketDeleteFiles implements Packet{

    private UUID folder;
    private List<String> files;

    @Override
    public void write(ByteBufferWrapper buffer) {
        buffer.putUUID(folder);
        buffer.putList(files);
    }

    @Override
    public void read(ByteBufferWrapper buffer) {
        folder = buffer.getUUID();
        files = buffer.getList(String::new);
    }

    @Override
    public int getSize() {
        return UUID_SIZE + listSize(files);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<PacketDeleteFiles> packets = new ArrayList<>();
        private PacketDeleteFiles current = null;
        private int maxFiles = 20;

        private void newCurrent(UUID folder) {
            current = new PacketDeleteFiles(folder, new ArrayList<>());
            packets.add(current);
        }

        public void addFile(UUID folder, String file) {
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
