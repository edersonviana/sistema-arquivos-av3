
import filesystem.FileSystemSimulator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        FileSystemSimulator fs = new FileSystemSimulator();

        Scanner scanner = new Scanner(System.in);

        String currentPath = "/";

        System.out.println("::::: Simulador de Sistema de Arquivos com Journaling :::::");
        System.out.println("Comandos disponíveis: ls, mkdir, mkfile, rm, mv, cp, exit");
        System.out.println("Use caminhos absolutos (ex: /pasta/arquivo.txt)");

        // Loop do Shell
        while (true) {
            System.out.print(currentPath + " $ "); // Prompt de comando

            // Verifica se há uma próxima linha para ler (útil para evitar erros ao fechar o input)
            if (!scanner.hasNextLine()) {
                break;
            }

            String commandLine = scanner.nextLine().trim();
            if (commandLine.isEmpty()) continue;

            // Quebra a linha de comando em partes: [comando, argumento1, argumento2, ...]
            String[] parts = commandLine.split("\\s+");
            String command = parts[0].toLowerCase();

            try {
                switch (command) {
                    case "ls":
                        // ls [caminho]
                        String lsPath = parts.length > 1 ? parts[1] : currentPath;
                        fs.listDirectory(lsPath);
                        break;

                    case "mkdir":
                        // mkdir <caminho>
                        if (parts.length < 2) { System.out.println("Uso: mkdir <caminho>"); break; }
                        fs.createDirectory(parts[1]);
                        break;

                    case "mkfile":
                        // mkfile <caminho> <conteudo>
                        if (parts.length < 3) { System.out.println("Uso: mkfile <caminho> \"<conteudo>\""); break; }
                        // Nota: Esta implementação não lida perfeitamente com espaços no conteúdo sem aspas.
                        // Para simplificar, pegamos o restante como conteúdo.
                        String content = commandLine.substring(parts[0].length() + parts[1].length() + 2);
                        fs.createFile(parts[1], content);
                        break;

                    case "rm":
                        // rm <caminho> (Remove arquivo ou diretório vazio)
                        if (parts.length < 2) { System.out.println("Uso: rm <caminho>"); break; }
                        // Tenta remover como arquivo. Se falhar, tenta como diretório.
                        fs.deleteFile(parts[1]);
                        // Se não for encontrado como arquivo, tenta remover como diretório
                        // Uma implementação mais robusta verificaria se o caminho existe antes.
                        // Para este simulador, a chamada 'deleteFile' ou 'deleteDirectory'
                        // simplesmente imprime a mensagem de "não encontrado" se não existir.
                        // Você pode tentar:
                        // fs.deleteDirectory(parts[1]);
                        break;

                    case "mv":
                        if (parts.length < 3) { System.out.println("Uso: mv <caminho_origem> <novo_nome>"); break; }

                        String originalPath = parts[1];
                        String newName = parts[2];

                        boolean success = false;

                        // 1. Tenta renomear como arquivo
                        if (fs.renameFile(originalPath, newName)) {
                            success = true;
                        }
                        // 2. SE A PRIMEIRA TENTATIVA FALHOU, tenta renomear como diretório
                        else if (fs.renameDirectory(originalPath, newName)) {
                            success = true;
                        }

                        if (!success) {
                            System.out.println("Erro: Arquivo ou diretório não encontrado no caminho de origem.");
                        }
                        break;

                    case "cp":
                        // cp <caminho_origem> <caminho_destino>
                        if (parts.length < 3) { System.out.println("Uso: cp <caminho_origem> <caminho_destino>"); break; }
                        fs.copyFile(parts[1], parts[2]);
                        break;

                    case "exit":
                        System.out.println("Saindo do simulador. O Journal.log foi gravado.");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Comando desconhecido. Comandos: ls, mkdir, mkfile, rm, mv, cp, exit");
                }
            } catch (Exception e) {
                System.out.println("ERRO INTERNO: " + e.getMessage());
            }
        }
    }
}