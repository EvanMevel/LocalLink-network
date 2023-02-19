package fr.emevel.locallink.network;

import fr.emevel.locallink.network.serial.PacketMalformedException;
import fr.emevel.locallink.network.serial.PacketParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public abstract class LinkSocket implements PacketReceiver, PacketSender {

    private final Socket socket;
    private final Thread thread;
    private final OutputStream outputStream;
    private final InputStream inputStream;

    public LinkSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.outputStream = socket.getOutputStream();
        this.inputStream = socket.getInputStream();
        this.thread = new Thread(this::run);
    }

    public void start() {
        thread.start();
    }

    public void stop() throws IOException {
        socket.close();
        thread.interrupt();
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    protected abstract void onPacketReceived(Packet packet) throws IOException;

    protected void run() {
        while (!socket.isClosed()) {
            try {
                Packet packet = receivePacket();
                onPacketReceived(packet);
            } catch (SocketException e) {
                if (socket.isClosed()) {
                    return;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (PacketMalformedException e) {
                throw new RuntimeException(e);
            } catch (PacketParsingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }
}
