package fr.emevel.locallink.network;

import fr.emevel.locallink.network.jmdns.client.JmDNSClient;
import fr.emevel.locallink.network.packets.Packet;
import fr.emevel.locallink.network.packets.PacketHandShake;

import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;
import java.util.function.BiConsumer;

public class ClientMain {

    private static LinkSocket linkSocket;

    public static void main(String[] args) throws Exception {
        JmDNSClient client = new JmDNSClient(new BiConsumer<InetSocketAddress, ServiceInfo>() {
            @Override
            public void accept(InetSocketAddress inetSocketAddress, ServiceInfo serviceInfo) {
                System.out.println("Service found: " + serviceInfo);

                try {
                    Socket socket = new Socket(inetSocketAddress.getAddress(), inetSocketAddress.getPort());
                    linkSocket = new LinkSocket(socket) {
                        @Override
                        protected void onPacketReceived(Packet packet) throws IOException {
                            System.out.println("Received packet: " + packet);
                        }
                    };
                    linkSocket.start();

                    linkSocket.sendPacket(new PacketHandShake("Client", UUID.randomUUID(), Signatures.VERSION));

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, UUID.randomUUID());
        client.registerListener();

        Scanner scanner = new Scanner(System.in);

        scanner.nextLine();

        if (linkSocket != null) {
            linkSocket.stop();
        }
        client.stop();
    }

}
