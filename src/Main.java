import filesystem.FileSystemSimulator;

public class Main {
    public static void main(String[] args) {
        FileSystemSimulator fs = new FileSystemSimulator();

        System.out.println("--- Teste de Criação e Listagem ---");
        fs.createDirectory("/docs");
        fs.createDirectory("/docs/textos");
        fs.createFile("/docs/textos/teste.txt", "Conteúdo de teste 1");
        fs.createFile("/docs/textos/outro.txt", "Conteúdo de teste 2");
        fs.listDirectory("/docs/textos");

        System.out.println("\n--- Teste de Cópia e Renomeação de Arquivo ---");
        fs.copyFile("/docs/textos/teste.txt", "/docs/copia.txt");
        fs.renameFile("/docs/copia.txt", "novo_nome.doc");
        fs.listDirectory("/docs");

        System.out.println("\n--- Teste de Renomeação e Deleção de Diretório ---");
        fs.renameDirectory("/docs/textos", "arquivos_antigos");
        fs.listDirectory("/docs");
        fs.deleteDirectory("/docs/arquivos_antigos");
        fs.listDirectory("/docs");

        System.out.println("\n--- Conteúdo do Journal (journal.log) ---");
    }
}