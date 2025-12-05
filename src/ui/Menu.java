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
            System.out.println("2. Apagar diretório");
            System.out.println("3. Renomear diretório");
            System.out.println("4. Criar arquivo");
            System.out.println("5. Apagar arquivo");
            System.out.println("6. Renomear arquivo");
            System.out.println("7. Copiar arquivo");
            System.out.println("8. Ler conteúdo do arquivo");
            System.out.println("9. Listar conteúdo (ls)");
            System.out.println("10. Salvar sistema");
            System.out.println("11. Recarregar do arquivo");
            System.out.println("0. Sair");

            System.out.print("Escolha uma opção: ");

            String input = scanner.nextLine();
            int opcao;

            try {
                opcao = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1 -> criarDiretorio();
                case 2 -> apagarDiretorio();
                case 3 -> renomearDiretorio();
                case 4 -> criarArquivo();
                case 5 -> apagarArquivo();
                case 6 -> renomearArquivo();
                case 7 -> copiarArquivo();
                case 8 -> lerArquivo();
                case 9 -> listar();
                case 10 -> salvar();
                case 11 -> recarregar();
                case 0 -> {
                    salvar();
                    System.out.println("Saindo...");
                    return;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }


    private void criarDiretorio() {
        System.out.print("Informe o caminho do novo diretório (ex: /home/docs): ");
        String caminho = scanner.nextLine();
        fs.createDirectory(caminho);
    }

    private void apagarDiretorio() {
        System.out.print("Informe o caminho do diretório a apagar: ");
        String caminho = scanner.nextLine();
        fs.deleteDirectory(caminho);
    }

    private void renomearDiretorio() {
        System.out.print("Informe o caminho atual do diretório: ");
        String caminho = scanner.nextLine();
        System.out.print("Informe o novo NOME (não o caminho completo): ");
        String novoNome = scanner.nextLine();

        boolean sucesso = fs.renameDirectory(caminho, novoNome);
        if (sucesso) {
            System.out.println("Diretório renomeado com sucesso.");
        } else {
            System.out.println("Falha ao renomear (Verifique se o diretório existe).");
        }
    }

    private void criarArquivo() {
        System.out.print("Informe o caminho do arquivo (ex: /home/texto.txt): ");
        String caminho = scanner.nextLine();
        System.out.print("Conteúdo do arquivo: ");
        String conteudo = scanner.nextLine();
        fs.createFile(caminho, conteudo);
    }

    private void apagarArquivo() {
        System.out.print("Informe o caminho do arquivo a apagar: ");
        String caminho = scanner.nextLine();
        fs.deleteFile(caminho);
    }

    private void renomearArquivo() {
        System.out.print("Informe o caminho atual do arquivo: ");
        String caminho = scanner.nextLine();
        System.out.print("Informe o novo NOME do arquivo: ");
        String novoNome = scanner.nextLine();

        boolean sucesso = fs.renameFile(caminho, novoNome);
        if (sucesso) {
            System.out.println("Arquivo renomeado com sucesso.");
        } else {
            System.out.println("Falha ao renomear (Verifique se o arquivo existe).");
        }
    }

    private void copiarArquivo() {
        System.out.print("Informe o caminho do arquivo ORIGINAL: ");
        String origem = scanner.nextLine();
        System.out.print("Informe o caminho de DESTINO (completo, com nome): ");
        String destino = scanner.nextLine();

        fs.copyFile(origem, destino);
    }

    private void lerArquivo() {
        System.out.print("Informe o caminho do arquivo para ler: ");
        String caminho = scanner.nextLine();
        fs.readFile(caminho);
    }

    private void listar() {
        System.out.print("Informe o caminho para listar (ex: / ou /home): ");
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
}