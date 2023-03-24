package fr.emevel.locallink.network.jmdns.server;

import fr.emevel.locallink.network.jmdns.LinkJmdns;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * This class is meant to be used in a {@link fr.emevel.locallink.network.jmdns.server.JmDNSServerThread}
 */
public class JmDNSServer {

    private final int port;
    private final UUID uuid;
    private JmDNS jmDNS = null;

    JmDNSServer(int port, UUID uuid) {
        this.port = port;
        this.uuid = uuid;
    }

    public void registerService() throws IOException {
        if (jmDNS != null) {
            throw new IllegalStateException("Service already registered");
        }
        jmDNS = LinkJmdns.initJmdnsLocal();
        ServiceInfo serviceInfo = ServiceInfo.create(
                LinkJmdns.SERVICE_TYPE,
                "LocalLink-" + uuid.toString(), port,
                0, 0, Map.of("uuid", uuid.toString()));
        jmDNS.registerService(serviceInfo);
    }

    public void stop() throws IOException {
        if (jmDNS != null) {
            jmDNS.unregisterAllServices();
            jmDNS.close();
            jmDNS = null;
        }
    }


}
