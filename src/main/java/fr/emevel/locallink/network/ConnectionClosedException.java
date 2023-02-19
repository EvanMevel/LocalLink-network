package fr.emevel.locallink.network;

import java.io.IOException;

public class ConnectionClosedException extends IOException {

    public ConnectionClosedException() {
        super("Connection closed");
    }
}
