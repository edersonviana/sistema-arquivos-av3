package filesystem;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Journal {

    private final String logFile = "journal.log";

    public void log(String operation, String target) {
        try (FileWriter fw = new FileWriter(logFile, true)) {
            fw.write(LocalDateTime.now() + " | " + operation + " | " + target + "\n");
        } catch (IOException e) {
            System.out.println("Erro ao escrever no journal: " + e.getMessage());
        }
    }
}
