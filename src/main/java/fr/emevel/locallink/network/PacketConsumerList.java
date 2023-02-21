package fr.emevel.locallink.network;

import fr.emevel.locallink.network.packets.Packet;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
@NoArgsConstructor
public class PacketConsumerList {

    private final Map<Class<? extends Packet>, Consumer<? extends Packet>> consumers = new HashMap<>();

    public <T extends Packet> void addConsumer(Class<T> packetClass, Consumer<T> consumer) {
        consumers.put(packetClass, consumer);
    }

    public <T extends Packet> void removeConsumer(Class<T> packetClass) {
        consumers.remove(packetClass);
    }

    public <T extends Packet> Consumer<T> getConsumer(Class<T> packetClass) {
        return (Consumer<T>) consumers.get(packetClass);
    }

    public <T extends Packet> void consumePacket(T packet) {
        Consumer<T> consumer = getConsumer((Class<T>) packet.getClass());

        if (consumer != null) {
            consumer.accept(packet);
        }
    }

}
