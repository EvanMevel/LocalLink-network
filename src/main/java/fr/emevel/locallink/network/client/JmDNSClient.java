package fr.emevel.locallink.network.client;

import fr.emevel.locallink.network.LinkJmdns;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.function.BiConsumer;

public class JmDNSClient {

    private final JmDNSClientServiceListener serviceListener;
    private JmDNS jmDNS;

    public JmDNSClient(BiConsumer<InetSocketAddress, ServiceInfo> consumer) {
        this.serviceListener = new JmDNSClientServiceListener(consumer);
    }

    public void registerListener() throws IOException {
        this.jmDNS = LinkJmdns.initJmdnsLocal();
        this.jmDNS.addServiceListener(LinkJmdns.SERVICE_TYPE, serviceListener);
    }

    public void stop() throws IOException {
        if (jmDNS != null) {
            jmDNS.removeServiceListener(LinkJmdns.SERVICE_TYPE, serviceListener);
            jmDNS.close();
            jmDNS = null;
        }
    }

}
