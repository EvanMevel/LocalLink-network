package fr.emevel.locallink.network.serial;

public class PacketMalformedException extends PacketException {

    public PacketMalformedException() {
    }

    public PacketMalformedException(String message) {
        super(message);
    }

    public PacketMalformedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PacketMalformedException(Throwable cause) {
        super(cause);
    }

    public PacketMalformedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
