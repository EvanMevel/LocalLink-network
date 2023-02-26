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

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacketFileList implements Packet {

    private String folder;
    private boolean end;
    private List<SyncFile> files;

    @Override
    public void write(ByteBufferWrapper buffer) {
        buffer.putString(folder);
        buffer.putBoolean(end);
        buffer.putList(files);
    }

    @Override
    public void read(ByteBufferWrapper buffer) {
        folder = buffer.getString();
        end = buffer.getBoolean();
        files = buffer.getList(SyncFile::new);
    }

    @Override
    public int getSize() {
        return stringSize(folder) + 1 + listSize(files);
    }

    public static Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor
    public static class Builder {
        private final List<PacketFileList> packets = new ArrayList<>();
        private PacketFileList current = null;
        private int maxFiles = 20;

        private void newCurrent(String name) {
            current = new PacketFileList(name, false, new ArrayList<>());
            packets.add(current);
        }

        public void addFile(File folder, File file) throws IOException {
            if (current == null || current.getFiles().size() >= maxFiles) {
                newCurrent(folder.getName());
            }
            current.getFiles().add(new SyncFile(file));
        }

        public Builder processFolder(File folder) throws IOException {
            if (!folder.isDirectory()) {
                return this;
            } else if (current == null) {
                newCurrent(folder.getName());
            }
            for (File child : folder.listFiles()) {
                if (child.isDirectory()) {
                    continue;
                }
                addFile(folder, child);
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
