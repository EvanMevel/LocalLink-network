package fr.emevel.locallink.network.server;

import fr.emevel.locallink.network.LinkSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public abstract class NetworkServer {

    private final ServerSocket socket;
    private final Thread acceptThread;
    private final List<LinkSocket> clients = new ArrayList<>();

    public NetworkServer(int port) throws IOException {
        this.socket = new ServerSocket(port);
        this.acceptThread = new Thread(this::accept, "NetworkServer-Accept");
    }

    public int getPort() {
        return socket.getLocalPort();
    }

    public void start() {
        System.out.println("Started server on port " + socket.getLocalPort());
        acceptThread.start();
    }

    public void stop() throws IOException {
        System.out.println("Stopping server");
        socket.close();
        acceptThread.interrupt();
        for (LinkSocket client : clients) {
            if (!client.isClosed()) {
                client.stop();
            }
        }
    }

    public void cleanupClients() {
        List<LinkSocket> toRemove = new ArrayList<>();
        for (LinkSocket serverClient : clients) {
            if (serverClient.isClosed()) {
                toRemove.add(serverClient);
            }
        }
        clients.removeAll(toRemove);
    }

    protected abstract LinkSocket createClient(Socket sock) throws IOException;

    protected void acceptClient(Socket sock) throws IOException {
        cleanupClients();

        System.out.println("Accepted client " + sock.getInetAddress() + ":" + sock.getPort());

        LinkSocket client = createClient(sock);
        clients.add(client);
        client.start();
    }

    protected void accept() {
        while (!socket.isClosed()) {
            try {
                Socket client = socket.accept();
                acceptClient(client);
            } catch (SocketException e) {
                if (socket.isClosed()) {
                    return;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
