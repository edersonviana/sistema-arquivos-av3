package filesystem;

import java.io.Serializable;

public class FileSystemSimulator implements Serializable {

    private Directory root;
    private Journal journal;

    public FileSystemSimulator() {
        this.root = new Directory("/");
        this.journal = new Journal();
        this.journal.loadFromDisk();  // carrega entradas antigas
    }


    private Directory navigate(String path) {
        if (path.equals("/") || path.isEmpty()) return root;

        String[] parts = path.split("/");
        Directory current = root;

        for (String part : parts) {
            if (part.isEmpty()) continue;
            if (!current.getDirectories().containsKey(part)) return null;
            current = current.getDirectories().get(part);
        }

        return current;
    }

    private String getParentPath(String path) {
        int idx = path.lastIndexOf("/");
        if (idx <= 0) return "/";
        return path.substring(0, idx);
    }

    private String getName(String path) {
        int idx = path.lastIndexOf("/");
        return path.substring(idx + 1);
    }

    // ------------------- DIRECTORIES ---------------------

    public void createDirectory(String path) {
        String parentPath = getParentPath(path);
        String name = getName(path);

        Directory parent = navigate(parentPath);
        if (parent == null) {
            System.out.println("Diretório pai não existe.");
            return;
        }

        if (parent.getDirectories().containsKey(name)) {
            System.out.println("Diretório já existe.");
            return;
        }

        parent.addDirectory(new Directory(name));
        journal.log("CREATE_DIR", path);
    }

    public void deleteDirectory(String path) {
        String parentPath = getParentPath(path);
        String name = getName(path);

        Directory parent = navigate(parentPath);
        if (parent == null || !parent.getDirectories().containsKey(name)) {
            System.out.println("Diretório não encontrado.");
            return;
        }

        parent.removeDirectory(name);
        journal.log("DELETE_DIR", path);
    }

    public boolean renameDirectory(String path, String newName) {
        String parentPath = getParentPath(path);
        String oldName = getName(path);

        Directory parent = navigate(parentPath);

        if (parent == null || !parent.getDirectories().containsKey(oldName)) {
            return false;
        }

        // Recupera o diretório
        Directory dir = parent.getDirectories().remove(oldName);

        dir.setName(newName);

        // Reinsere com o novo nome
        parent.getDirectories().put(newName, dir);

        // Registro no journal
        journal.log("RENAME_DIR", path + " -> " + newName);

        return true;
    }


    public void listDirectory(String path) {
        Directory dir = navigate(path);

        if (dir == null) {
            System.out.println("Diretório não encontrado.");
            return;
        }

        System.out.println("Diretórios:");
        dir.getDirectories().keySet().forEach(d -> System.out.println(" - " + d));

        System.out.println("Arquivos:");
        dir.getFiles().keySet().forEach(f -> System.out.println(" - " + f));
    }

    public void list(String path) {
        listDirectory(path);
    }


    // ------------------- FILES ---------------------

    public void createFile(String path, String content) {
        String parentPath = getParentPath(path);
        String name = getName(path);

        Directory parent = navigate(parentPath);
        if (parent == null) {
            System.out.println("Diretório pai não existe.");
            return;
        }

        parent.addFile(new File(name, content));
        journal.log("CREATE_FILE", path);
    }

    public void deleteFile(String path) {
        String parentPath = getParentPath(path);
        String name = getName(path);

        Directory parent = navigate(parentPath);
        if (parent == null || !parent.getFiles().containsKey(name)) {
            System.out.println("Arquivo não encontrado.");
            return;
        }

        parent.removeFile(name);
        journal.log("DELETE_FILE", path);
    }

    public boolean renameFile(String path, String newName) {
        String parentPath = getParentPath(path);
        String name = getName(path);

        Directory parent = navigate(parentPath);

        if (parent == null || !parent.getFiles().containsKey(name)) {
            return false;
        }

        File file = parent.getFiles().remove(name);
        file.setName(newName);
        parent.addFile(file);

        journal.log("RENAME_FILE", path + " -> " + newName);
        return true;
    }

    public boolean copyFile(String srcPath, String destPath) {
        String srcParent = getParentPath(srcPath);
        String srcName = getName(srcPath);

        Directory srcDir = navigate(srcParent);
        if (srcDir == null || !srcDir.getFiles().containsKey(srcName)) {
            System.out.println("Arquivo origem não encontrado: " + srcPath);
            return false;
        }

        File original = srcDir.getFiles().get(srcName);

        Directory destDir = navigate(destPath);
        String destParentPath;
        String destName;

        if (destDir != null) {
            destParentPath = destPath;
            if (destParentPath.equals("/")) destParentPath = "/";
            destName = srcName;
        } else {
            if (destPath.endsWith("/")) {
                System.out.println("Diretório destino não existe: " + destPath);
                return false;
            }
            destParentPath = getParentPath(destPath);
            destName = getName(destPath);
            destDir = navigate(destParentPath);
            if (destDir == null) {
                System.out.println("Diretório destino não existe: " + destParentPath);
                return false;
            }
        }

        Directory parentDest = navigate(destParentPath);
        if (parentDest.getFiles().containsKey(destName)) {
            System.out.println("Arquivo destino já existe: " + (destParentPath.endsWith("/") ? destParentPath + destName : destParentPath + "/" + destName));
            return false;
        }

        parentDest.addFile(new File(destName, original.getContent()));
        journal.log("COPY_FILE", srcPath + " -> " + (destParentPath.endsWith("/") ? destParentPath + destName : destParentPath + "/" + destName));
        FileSystemStorage.save(this);
        return true;
    }

    public void readFile(String path) {
        String parentPath = getParentPath(path);
        String name = getName(path);

        Directory parent = navigate(parentPath);

        if (parent == null) {
            System.out.println("Diretório pai não encontrado.");
            return;
        }

        if (!parent.getFiles().containsKey(name)) {
            System.out.println("Arquivo não existe.");
            return;
        }

        File file = parent.getFiles().get(name);

        System.out.println("\n===== CONTEÚDO DO ARQUIVO =====");
        System.out.println(file.getContent());
        System.out.println("================================\n");
    }

}

