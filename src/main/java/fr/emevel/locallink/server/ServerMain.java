package fr.emevel.locallink.server;

import fr.emevel.jconfig.LocalFileSource;

import java.io.*;
import java.util.Scanner;

public class ServerMain {

    public static LocalLinkServer server;

    public static void saveDataToFile(LocalLinkServerData data, File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))){
            oos.writeObject(data);
        }
    }

    public static LocalLinkServerData loadDataFromFile(File file) throws IOException {
        if (!file.exists()) {
            LocalLinkServerData data = new LocalLinkServerData();
            saveDataToFile(data, file);
            return data;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            return (LocalLinkServerData) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        File file = new File("server.json");

        LocalFileSource source = LocalFileSource.builder(file).json(2).saveIfNotExists(true).build();

        LocalLinkServerData data = source.load(LocalLinkServerData.class);

        Runnable dataSaver = () -> {
            try {
                source.save(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        server = new LocalLinkServer(data, dataSaver);

        server.start();

        Scanner scanner = new Scanner(System.in);

        scanner.nextLine();

        server.stop();
    }
}