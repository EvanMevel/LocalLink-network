package fr.emevel.locallink.network.serial;

public class PacketParsingException extends RuntimeException {

    public PacketParsingException() {
    }

    public PacketParsingException(String message) {
        super(message);
    }

    public PacketParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public PacketParsingException(Throwable cause) {
        super(cause);
    }

    public PacketParsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
