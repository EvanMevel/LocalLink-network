package fr.emevel.locallink.server;

import fr.emevel.jconfig.Save;
import fr.emevel.locallink.server.sync.SyncFolderList;
import lombok.Data;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@Data
public class LocalLinkServerData implements Serializable {

    @Save
    private UUID uuid = UUID.randomUUID();
    @Save
    private String name = getLocalHostName();
    @Save
    private SyncFolderList folders = new SyncFolderList();
    @Save
    private int port = 0;
    @Save(type = UserInfo.class)
    private Map<String, UserInfo> userFolders = new HashMap<>();
    @Save
    private InetAddress address = null;

    public LocalLinkServerData() {
    }

    public UserInfo getOrCreateUser(String uuid) {
        if (!userFolders.containsKey(uuid)) {
            userFolders.put(uuid, new UserInfo());
        }
        return userFolders.get(uuid);
    }

    private static String getLocalHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "Unknown";
        }
    }

    @Data
    public static class UserInfo {
        @Save(type = UUID.class)
        private List<UUID> folders = new ArrayList<>();
    }

}
