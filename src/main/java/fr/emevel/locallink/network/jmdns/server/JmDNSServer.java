package fr.emevel.locallink.network.jmdns.server;

import fr.emevel.locallink.network.jmdns.LinkJmdns;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;

/**
 * This class is meant to be used in a {@link fr.emevel.locallink.network.jmdns.server.JmDNSServerThread}
 */
public class JmDNSServer {

    private final int port;
    private JmDNS jmDNS = null;

    JmDNSServer(int port) {
        this.port = port;
    }

    public void registerService() throws IOException {
        if (jmDNS != null) {
            throw new IllegalStateException("Service already registered");
        }
        jmDNS = LinkJmdns.initJmdnsLocal();
        ServiceInfo serviceInfo = ServiceInfo.create(LinkJmdns.SERVICE_TYPE, "LocalLink", port, "LocalLink");
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
