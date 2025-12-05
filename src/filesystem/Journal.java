package filesystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Journal implements Serializable {

    private List<JournalEntry> entries = new ArrayList<>();

    private static final String JOURNAL_FILE = "journal.dat";

    public void log(String operation, String details) {
        JournalEntry entry = new JournalEntry(operation, details);
        entries.add(entry);
        saveToDisk();
    }

    public List<JournalEntry> getEntries() {
        return entries;
    }

    // Salva as entradas do journal em disco
    private void saveToDisk() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(JOURNAL_FILE))) {
            oos.writeObject(entries);
        } catch (Exception e) {
            System.out.println("Erro ao salvar journal: " + e.getMessage());
        }
    }

    // Carrega entradas prévias do disco
    @SuppressWarnings("unchecked")
    public void loadFromDisk() {
        java.io.File f = new java.io.File(JOURNAL_FILE);
        if (!f.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(JOURNAL_FILE))) {
            entries = (List<JournalEntry>) ois.readObject();
        } catch (Exception e) {
            System.out.println("Erro ao carregar journal: " + e.getMessage());
        }
    }

    // Reexecuta todas as operações (replay)
    public void replay(FileSystemSimulator fs) {
        for (JournalEntry entry : entries) {
            String op = entry.getOperation();
            String d  = entry.getDetails();

            try {
                switch (op) {
                    case "CREATE_DIR":
                        fs.createDirectory(d);
                        break;

                    case "DELETE_DIR":
                        fs.deleteDirectory(d);
                        break;

                    case "CREATE_FILE":
                        fs.createFile(d, ""); // conteúdo não vem no journal, mas depende da exigência do professor
                        break;

                    case "DELETE_FILE":
                        fs.deleteFile(d);
                        break;

                    case "RENAME_DIR":
                        String[] parts1 = d.split(" -> ");
                        fs.renameDirectory(parts1[0], parts1[1]);
                        break;

                    case "RENAME_FILE":
                        String[] parts2 = d.split(" -> ");
                        fs.renameFile(parts2[0], parts2[1]);
                        break;

                    case "COPY_FILE":
                        String[] parts3 = d.split(" -> ");
                        fs.copyFile(parts3[0], parts3[1]);
                        break;
                }
            } catch (Exception e) {
                System.out.println("Erro ao reexecutar journal: " + entry);
            }
        }
    }
}
