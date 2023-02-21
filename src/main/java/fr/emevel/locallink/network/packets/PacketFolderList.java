package fr.emevel.locallink.network.packets;

import fr.emevel.locallink.network.serial.ByteBufferWrapper;
import fr.emevel.locallink.network.serial.NetworkSerializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PacketFolderList implements Packet {

    private List<Folder> folders;

    @Override
    public void write(ByteBufferWrapper buffer) {
        buffer.putList(folders);
    }

    @Override
    public void read(ByteBufferWrapper buffer) {
        folders = buffer.getList(Folder::new);
    }

    @Override
    public int getSize() {
        return listSize(folders);
    }

    @Data
    public static class Folder implements NetworkSerializable {

        private String name;
        private List<Folder> childs = new ArrayList<>(5);

        public Folder(String name) {
            this.name = name;
        }

        public Folder() {
        }

        @Override
        public void write(ByteBufferWrapper buffer) {
            buffer.putString(name);
            buffer.putList(childs);
        }

        @Override
        public void read(ByteBufferWrapper buffer) {
            name = buffer.getString();
            childs = buffer.getList(Folder::new);
        }

        @Override
        public int getSize() {
            return stringSize(name) + listSize(childs);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final PacketFolderList packet = new PacketFolderList();

        private void addFolder(List<Folder> folders, File folder) {
            for (File file : folder.listFiles()) {
                if (!file.isDirectory()) {
                    continue;
                }
                Folder fold = new Folder(file.getName());
                addFolder(fold.getChilds(), file);
                folders.add(fold);
            }
        }

        public Builder addFolder(File folder) {
            if (!folder.isDirectory()) {
                return this;
            }
            List<Folder> folders = new ArrayList<>(5);
            addFolder(folders, folder);
            packet.setFolders(folders);
            return this;
        }

        public PacketFolderList build() {
            return packet;
        }

    }

}
