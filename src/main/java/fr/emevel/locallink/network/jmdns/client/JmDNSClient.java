package fr.emevel.locallink.network.jmdns.client;

import fr.emevel.locallink.network.jmdns.LinkJmdns;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.function.BiConsumer;

public class JmDNSClient {

    private final JmDNSClientServiceListener serviceListener;
    private JmDNS jmDNS;

    public JmDNSClient(BiConsumer<InetSocketAddress, ServiceInfo> consumer, UUID uuid) {
        this.serviceListener = new JmDNSClientServiceListener(consumer, uuid);
    }

    public void registerListener() throws IOException {
        System.out.println("Registering JmDNS listener");
        this.jmDNS = LinkJmdns.initJmdnsLocal();
        this.jmDNS.addServiceListener(LinkJmdns.SERVICE_TYPE, serviceListener);
    }

    public void stop() throws IOException {
        System.out.println("Stopping JmDNS listener");
        if (jmDNS != null) {
            jmDNS.removeServiceListener(LinkJmdns.SERVICE_TYPE, serviceListener);
            jmDNS.close();
            jmDNS = null;
        }
    }

}
