package fr.emevel.locallink.network.jmdns.client;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.jmdns.impl.util.ByteWrangler;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public class JmDNSClientServiceListener implements ServiceListener {

    private final BiConsumer<InetSocketAddress, ServiceInfo> consumer;
    private final UUID uuid;

    JmDNSClientServiceListener(BiConsumer<InetSocketAddress, ServiceInfo> consumer, UUID uuid) {
        this.consumer = consumer;
        this.uuid = uuid;
    }

    @Override
    public void serviceAdded(ServiceEvent serviceEvent) {
        serviceResolved(serviceEvent);
    }

    @Override
    public void serviceRemoved(ServiceEvent serviceEvent) {
        System.out.println("Service removed: " + serviceEvent.getName());
    }

    @Override
    public void serviceResolved(ServiceEvent event) {
        // Happens when listener was registered before service
        // Events gets triggered 2 times, the first time with no data
        // The second time with all the data, so we ignore the first one
        if (event.getInfo() != null && event.getInfo().getInetAddresses().length > 0) {
            System.out.println("Service resolved: " + event.getName() + " at " +
                    event.getInfo().getInetAddresses()[0] + ":" + event.getInfo().getPort());
        }
        if (event.getInfo().hasData() && !event.getInfo().getPropertyNames().hasMoreElements()) {
            return;
        }
        Map<String, byte[]> properties = new HashMap<>();
        try {
            ByteWrangler.readProperties(properties, event.getInfo().getTextBytes());
        } catch (Exception e) {
            System.out.println("Error while reading properties from service at " + event.getInfo().getInetAddresses()[0] + ":" + event.getInfo().getPort());
            return;
        }
        if (!properties.containsKey("uuid")) {
            System.out.println("Service at " + event.getInfo().getInetAddresses()[0] + ":" + event.getInfo().getPort() + " does not have a UUID");
            return;
        }
        UUID serviceUUID = UUID.fromString(new String(properties.get("uuid")));
        if (uuid.equals(serviceUUID)) {
            System.out.println("Discovered ourselves at " + event.getInfo().getInetAddresses()[0] + ":" + event.getInfo().getPort());
            return;
        }
        InetSocketAddress address = new InetSocketAddress(event.getInfo().getInetAddresses()[0], event.getInfo().getPort());
        consumer.accept(address, event.getInfo());
    }
}
