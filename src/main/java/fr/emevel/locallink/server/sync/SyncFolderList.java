package fr.emevel.locallink.server.sync;

import fr.emevel.jconfig.Save;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SyncFolderList implements Serializable {

    @Getter
    @Save(type = LocalSyncFolder.class)
    private List<LocalSyncFolder> localFolders = new ArrayList<>();

    public SyncFolder getFolder(UUID id) {
        for (LocalSyncFolder folder : localFolders) {
            if (folder.getUuid().equals(id)) {
                return folder;
            }
        }
        return null;
    }

    public void addFolder(LocalSyncFolder folder) {
        localFolders.add(folder);
    }

}
