import gerarLabirinto.*;
import labirinto.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner inputScanner = new Scanner(System.in);
        System.out.println("Digite o caminho do arquivo do labirinto (ex.: casos4/nomeDoCaso.txt):");
        String caminhoArquivo = inputScanner.nextLine();

        try (Scanner sc = new Scanner(new File(caminhoArquivo))) {
            if (!sc.hasNextInt()) throw new IllegalArgumentException("Formato inválido para o número de linhas.");
            int m = sc.nextInt();
            if (!sc.hasNextInt()) throw new IllegalArgumentException("Formato inválido para o número de colunas.");
            int n = sc.nextInt();
            char[][] labirinto = new char[m][n];

            // preenchendo a matriz
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (sc.hasNext()) {
                        labirinto[i][j] = sc.next().charAt(0);
                    }
                    else {
                        throw new IllegalStateException("Labirinto com formato inválido ou incompleto.");
                    }
                }
            }

            System.out.println("\nArquivo carregado com sucesso!");
            System.out.println("\nInicializando interface gráfica...");

            // inicializando a interface
            SwingUtilities.invokeLater(() -> {
                LabirintoGui gui = new LabirintoGui(labirinto);
                gui.setVisible(true);

                // processando o labirinto e atualizando a interface
                Labirinto lab = new Labirinto(labirinto, gui);
                lab.processarLabirinto();
                lab.resultados();
                gui.atualizarLabirinto();  // visualização do labirinto!
            });

        } catch (IOException e) {
            System.err.println("Erro ao tentar abrir o arquivo: " + e.getMessage());
            System.err.println("Certifique-se de que o caminho está correto e tente novamente.");
        } catch (Exception e) {
            System.err.println("Erro ao processar o arquivo: " + e.getMessage());
            System.err.println("Verifique o formato do arquivo e tente novamente.");
        } finally {
            inputScanner.close();
        }

        System.out.println("\nANÁLISE COMPLETA! CONFIRA OS RESULTADOS:");
    }
}