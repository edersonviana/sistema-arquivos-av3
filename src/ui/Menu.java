package ui;

import filesystem.FileSystemSimulator;
import filesystem.FileSystemStorage;

import java.util.Scanner;

public class Menu {

    private FileSystemSimulator fs;
    private Scanner scanner = new Scanner(System.in);

    public Menu() {
        System.out.println("Carregando sistema de arquivos...");
        fs = FileSystemStorage.load();
    }

    public void start() {
        while (true) {
            System.out.println("\n===== MENU DO SISTEMA DE ARQUIVOS =====");
            System.out.println("1. Criar diretório");
            System.out.println("2. Criar arquivo");
            System.out.println("3. Listar conteúdo");
            System.out.println("4. Salvar sistema");
            System.out.println("5. Recarregar do arquivo");
            System.out.println("6. Ler arquivo");
            System.out.println("7. Sair");

            System.out.print("Escolha uma opção: ");

            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> criarDiretorio();
                case 2 -> criarArquivo();
                case 3 -> listar();
                case 4 -> salvar();
                case 5 -> recarregar();
                case 6 -> lerArquivo();
                case 7 -> {
                    salvar();
                    System.out.println("Saindo...");
                    return;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void criarDiretorio() {
        System.out.print("Informe o caminho do diretório: ");
        String caminho = scanner.nextLine();
        fs.createDirectory(caminho);
    }

    private void criarArquivo() {
        System.out.print("Informe o caminho do arquivo: ");
        String caminho = scanner.nextLine();
        System.out.print("Conteúdo do arquivo: ");
        String conteudo = scanner.nextLine();
        fs.createFile(caminho, conteudo);
    }

    private void listar() {
        System.out.print("Informe o caminho para listar: ");
        String caminho = scanner.nextLine();
        fs.list(caminho);
    }

    private void salvar() {
        System.out.println("Salvando estado...");
        FileSystemStorage.save(fs);
        System.out.println("Sistema salvo com sucesso!");
    }

    private void recarregar() {
        System.out.println("Recarregando sistema do arquivo...");
        fs = FileSystemStorage.load();
        System.out.println("Sistema recarregado!");
    }

    private void lerArquivo() {
        System.out.print("Informe o caminho do arquivo: ");
        String caminho = scanner.nextLine();
        fs.readFile(caminho);
    }

}
