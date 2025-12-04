package filesystem;

import java.util.HashMap;
import java.util.Map;

public class Directory {
    private String name;
    private Map<String, File> files = new HashMap<>();
    private Map<String, Directory> directories = new HashMap<>();

    public Directory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<String, File> getFiles() {
        return files;
    }

    public Map<String, Directory> getDirectories() {
        return directories;
    }

    public void addFile(File file) {
        files.put(file.getName(), file);
    }

    public void addDirectory(Directory directory) {
        directories.put(directory.getName(), directory);
    }

    public void removeFile(String name) {
        files.remove(name);
    }

    public void removeDirectory(String name) {
        directories.remove(name);
    }
}
