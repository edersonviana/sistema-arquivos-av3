package filesystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Journal implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<JournalEntry> entries = new ArrayList<>();

    private static final String JOURNAL_FILE = "journal.log";

    public void log(String operation, String details) {
        JournalEntry entry = new JournalEntry(operation, details);
        entries.add(entry);
        saveToDisk(entry); // Passa a entry para salvar APENAS ela
    }

    public List<JournalEntry> getEntries() {
        return entries;
    }

    private void saveToDisk(JournalEntry entry) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(JOURNAL_FILE, true))) {
            // O JournalEntry.toString() será escrito como uma nova linha de texto.
            writer.write(entry.toString());
            writer.newLine(); // Add uma quebra de linha
        } catch (IOException e) {
            System.out.println("Erro ao salvar journal no disco: " + e.getMessage());
        }
    }


    public void loadFromDisk() {
        System.out.println("Journal em modo texto: a recuperação (load) não está implementada.");
    }

    public void replay(FileSystemSimulator fs) {
        System.out.println("Replay do Journal não implementado em modo texto, mas mantido para demonstração.");
    }
}