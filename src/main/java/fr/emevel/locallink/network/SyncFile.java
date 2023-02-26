package fr.emevel.locallink.network;

import fr.emevel.locallink.network.serial.ByteBufferWrapper;
import fr.emevel.locallink.network.serial.NetworkSerializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncFile implements NetworkSerializable {

    private static final MessageDigest SHA256_DIGEST;
    private static final int BUFFER_SIZE = 8192;
    static {
        try {
            SHA256_DIGEST = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFileChecksum(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] byteArray = new byte[BUFFER_SIZE];
            int bytesCount = 0;

            //Read file data and update in message digest
            while ((bytesCount = fis.read(byteArray)) != -1) {
                SHA256_DIGEST.update(byteArray, 0, bytesCount);
            };
        }

        //Get the hash's bytes
        byte[] bytes = SHA256_DIGEST.digest();

        SHA256_DIGEST.reset();

        //we have bytes in decimal format, we need convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    private String name;
    private String sha256;

    public SyncFile(File file) throws IOException {
        this.name = file.getName();
        this.sha256 = getFileChecksum(file);
    }

    @Override
    public void write(ByteBufferWrapper buffer) {
        buffer.putString(name);
        buffer.putString(sha256);
    }

    @Override
    public void read(ByteBufferWrapper buffer) {
        name = buffer.getString();
        sha256 = buffer.getString();
    }

    @Override
    public int getSize() {
        return stringSize(name) + stringSize(sha256);
    }
}
