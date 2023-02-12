package fr.emevel.locallink.network.client;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.net.InetSocketAddress;
import java.util.function.BiConsumer;

public class JmDNSClientServiceListener implements ServiceListener {


    private final BiConsumer<InetSocketAddress, ServiceInfo> consumer;

    public JmDNSClientServiceListener(BiConsumer<InetSocketAddress, ServiceInfo> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void serviceAdded(ServiceEvent serviceEvent) {

    }

    @Override
    public void serviceRemoved(ServiceEvent serviceEvent) {

    }

    @Override
    public void serviceResolved(ServiceEvent event) {
        // Happens when listener was registered before service
        // Events gets triggered 2 times, the first time with no data
        // The second time with all the data, so we ignore the first one
        if (event.getInfo().hasData() && !event.getInfo().getPropertyNames().hasMoreElements()) {
            return;
        }
        InetSocketAddress address = new InetSocketAddress(event.getInfo().getInetAddresses()[0], event.getInfo().getPort());
        consumer.accept(address, event.getInfo());
    }
}
