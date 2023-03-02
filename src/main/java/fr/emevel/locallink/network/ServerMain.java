package fr.emevel.locallink.network;

import fr.emevel.locallink.network.jmdns.server.JmDNSServerThread;
import fr.emevel.locallink.network.packets.Packet;
import fr.emevel.locallink.network.packets.PacketHandShake;
import fr.emevel.locallink.network.serial.ByteBufferWrapper;
import fr.emevel.locallink.network.server.NetworkServer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

public class ServerMain {

    @AllArgsConstructor
    @NoArgsConstructor
    public static class Test implements Packet {
        int test;

        @Override
        public int getSize() {
            return Integer.BYTES;
        }

        @Override
        public void write(ByteBufferWrapper buffer) {
            buffer.putInt(test);
        }

        @Override
        public void read(ByteBufferWrapper buffer) {
            test = buffer.getInt();
        }
    }

    public static void main(String[] args) throws Exception {
        JmDNSServerThread server = new JmDNSServerThread(4242);
        server.start();

        NetworkServer networkServer = new NetworkServer(4242) {
            @Override
            protected void clientDisconnected(LinkSocket client) {

            }

            @Override
            protected LinkSocket createClient(Socket sock) throws IOException {
                LinkSocket client = new LinkSocket(sock) {
                    @Override
                    public void onPacketReceived(Packet packet) {
                        System.out.println("Received packet: " + packet);
                        if (packet instanceof PacketHandShake) {
                            this.safeSendPacket(new PacketHandShake("Server", UUID.randomUUID(), Signatures.VERSION));
                        }
                    }
                };
                return client;
            }
        };
        networkServer.start();

        Scanner scanner = new Scanner(System.in);

        scanner.nextLine();

        networkServer.stop();

        server.stop();
    }
}