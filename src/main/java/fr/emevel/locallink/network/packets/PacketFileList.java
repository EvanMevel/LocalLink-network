package fr.emevel.locallink.network.packets;

import fr.emevel.locallink.network.SyncFile;
import fr.emevel.locallink.network.serial.ByteBufferWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacketFileList implements Packet {

    private UUID folder;
    private boolean end;
    private List<SyncFile> files;

    @Override
    public void write(ByteBufferWrapper buffer) {
        buffer.putUUID(folder);
        buffer.putBoolean(end);
        buffer.putList(files);
    }

    @Override
    public void read(ByteBufferWrapper buffer) {
        folder = buffer.getUUID();
        end = buffer.getBoolean();
        files = buffer.getList(SyncFile::new);
    }

    @Override
    public int getSize() {
        return UUID_SIZE + BOOLEAN_SIZE + listSize(files);
    }

    public static Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor
    public static class Builder {
        private final List<PacketFileList> packets = new ArrayList<>();
        private PacketFileList current = null;
        private int maxFiles = 20;

        private void newCurrent(UUID folder) {
            current = new PacketFileList(folder, false, new ArrayList<>());
            packets.add(current);
        }

        public void addFile(UUID folder, File file) throws IOException {
            if (current == null || current.getFiles().size() >= maxFiles) {
                newCurrent(folder);
            }
            current.getFiles().add(new SyncFile(file));
        }

        public Builder processFolder(UUID id, File folder) throws IOException {
            if (!folder.isDirectory()) {
                return this;
            } else if (current == null) {
                newCurrent(id);
            }
            for (File child : folder.listFiles()) {
                if (child.isDirectory()) {
                    continue;
                }
                addFile(id, child);
            }
            return this;
        }

        public Builder setMaxFiles(int maxFiles) {
            this.maxFiles = maxFiles;
            return this;
        }

        public List<PacketFileList> build() {
            if (current != null) {
                current.setEnd(true);
            }
            return packets;
        }

    }
}
