package filesystem;

import java.io.*;

public class FileSystemStorage {

    private static final String DATA_FILE = "filesystem.dat";

    public static void save(FileSystemSimulator fs) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(fs);
        } catch (Exception e) {
            System.out.println("Erro ao salvar filesystem.dat: " + e.getMessage());
        }
    }

    // Carregar o estado
    public static FileSystemSimulator load() {

        java.io.File file = new java.io.File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("Nenhum filesystem.dat encontrado. Criando novo sistema...");
            return new FileSystemSimulator();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            return (FileSystemSimulator) ois.readObject();
        } catch (Exception e) {
            System.out.println("Erro ao carregar filesystem.dat: " + e.getMessage());
            return new FileSystemSimulator();
        }
    }
}
