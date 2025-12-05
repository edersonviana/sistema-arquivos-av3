package filesystem;

import java.io.Serializable;
import java.time.LocalDateTime;

public class JournalEntry implements Serializable {
    private String operation;
    private String details;
    private LocalDateTime timestamp;

    public JournalEntry(String operation, String details) {
        this.operation = operation;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

    public String getOperation() {
        return operation;
    }

    public String getDetails() {
        return details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + operation + " - " + details;
    }
}
