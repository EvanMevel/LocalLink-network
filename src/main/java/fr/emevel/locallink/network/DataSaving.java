package fr.emevel.locallink.network;

import lombok.AllArgsConstructor;

import java.io.*;
import java.util.function.Supplier;

public interface DataSaving<T> {

    T load(Supplier<T> defaultData) throws IOException;

    void save(T data) throws IOException;

    default Runnable saver(T data) {
        return () -> {
            try {
                save(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    static <T> DataSaving<T> localFile(File file) {
        return new LocalFile<>(file);
    }

    @AllArgsConstructor
    class LocalFile<T> implements DataSaving<T> {

        private final File file;

        @Override
        public T load(Supplier<T> defaultData) throws IOException {
            if (!file.exists()) {
                T data = defaultData.get();
                save(data);
                return data;
            }
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
                return (T) ois.readObject();
            } catch (ReflectiveOperationException | InvalidClassException e) {
                System.out.println("Class not found, deleting file");
                file.delete();
                T data = defaultData.get();
                save(data);
                return data;
            }
        }

        @Override
        public void save(T data) throws IOException {
            if (!file.exists()) {
                file.createNewFile();
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))){
                oos.writeObject(data);
            }
        }
    }

}
