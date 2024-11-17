package labirinto;

import gerarLabirinto.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Labirinto {
    private LabirintoGui gui;
    private char[][] labirinto;
    private int m, n;
    private final int[] dx = {-1, 0, 1, 0};
    private final int[] dy = {0, 1, 0, -1};
    private Map<Integer, SeresRegiao> regioesSeres;
    private Graph graph;

    public Labirinto(char[][] labirinto, LabirintoGui gui) {
        this.gui = gui;
        this.labirinto = labirinto;
        this.m = labirinto.length;
        this.n = labirinto[0].length;
        this.regioesSeres = new HashMap<>();
        this.graph = new Graph(m * n);
    }

    private String identificarSer(char ser) {
        switch (ser) {
            case 'A': return "Anão";
            case 'B': return "Bruxa";
            case 'C': return "Cavaleiro";
            case 'D': return "Duende";
            case 'E': return "Elfo";
            case 'F': return "Feijão";
            default: return "Desconhecido";
        }
    }

    // converte coordenadas i,j para vértice do grafo
    private int coordToVertex(int i, int j) {
        return i * n + j;
    }

    // converte vértice para coordenadas i,j
    private int[] vertexToCoord(int v) {
        return new int[]{v / n, v % n};
    }

    private void construirGrafo() {
        //construindo arestas do grafo (conexoes do labirinto)
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (Character.isLetterOrDigit(labirinto[i][j])) {
                    int verticeAtual = coordToVertex(i, j);

                    try {
                        int paredes = Character.digit(labirinto[i][j], 16);
                        String bin = String.format("%4s", Integer.toBinaryString(paredes)).replace(' ', '0');

                        // validando as 4 direções possíveis
                        for (int d = 0; d < 4; d++) {
                            if (bin.charAt(d) == '0') { // sem parede nesta direção
                                int ni = i + dx[d];
                                int nj = j + dy[d];

                                if (ni >= 0 && ni < m && nj >= 0 && nj < n &&
                                        Character.isLetterOrDigit(labirinto[ni][nj])) {
                                    int verticeVizinho = coordToVertex(ni, nj);
                                    // adiciona aresta entre os vértices
                                    graph.addEdge(verticeAtual, verticeVizinho);
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        // ignora caracteres que não são hexa
                        continue;
                    }
                }
            }
        }
    }

    public void processarLabirinto() {
        construirGrafo();

        // armazenando as regiões encontradas
        Map<Integer, Integer> verticeParaRegiao = new HashMap<>();
        int regiaoAtual = 0;

        // processa cada vértice do grafo
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int vertice = coordToVertex(i, j);

                // se é uma célula válida e ainda não foi visitada
                if (Character.isLetterOrDigit(labirinto[i][j]) && !verticeParaRegiao.containsKey(vertice)) {
                    // entao inicia o dfs
                    CaminhamentoProfundidade dfs = new CaminhamentoProfundidade(graph, vertice);

                    // processa todos os vértices possiveis
                    for (int v = 0; v < graph.V(); v++) {
                        if (dfs.hasPathTo(v)) {
                            int[] coord = vertexToCoord(v);
                            verticeParaRegiao.put(v, regiaoAtual);

                            // se encontrar um ser (letra maiúscula), adiciona à região
                            if (Character.isUpperCase(labirinto[coord[0]][coord[1]]))
                                regioesSeres.computeIfAbsent(regiaoAtual, k -> new SeresRegiao()).adicionarSer(labirinto[coord[0]][coord[1]]);
                        }
                    }
                    regiaoAtual++;
                }
            }
        }

        // cria matriz de regiões para a interface gráfica
        int[][] regioes = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int vertice = coordToVertex(i, j);
                regioes[i][j] = verticeParaRegiao.getOrDefault(vertice, -1);
            }
        }
        gui.atualizarRegiao(regioes);
    }

    private int contarRegioes() {
        Set<Integer> regioesUnicas = new HashSet<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (Character.isUpperCase(labirinto[i][j])) {
                    int vertice = coordToVertex(i, j);
                    CaminhamentoProfundidade dfs = new CaminhamentoProfundidade(graph, vertice);
                    regioesUnicas.add(vertice);
                }
            }
        }
        return regioesUnicas.size();
    }

    public void resultados() {
        System.out.println("\n1. Número de regiões isoladas: " + regioesSeres.size());
        System.out.println("\n2. Detalhes de cada região:");

        System.out.println("------------------------------------------------------------------------------");
        System.out.printf("| %-10s | %-25s | %-15s | %-10s |\n", "Região", "Ser mais frequente", "Ocorrências", "Símbolo");
        System.out.println("------------------------------------------------------------------------------");

        AtomicInteger numeroRegiao = new AtomicInteger(1);

        regioesSeres.keySet().stream().sorted().forEach(regiao -> {
            SeresRegiao seresRegiao = regioesSeres.get(regiao);
            char serFrequente = seresRegiao.serMaisFrequente();
            int ocorrencias = seresRegiao.getContagem().get(serFrequente);
            String serNome = identificarSer(serFrequente);
            System.out.printf("| %-10d | %-25s | %-15d | %-10c |\n",
                    numeroRegiao.getAndIncrement(), serNome, ocorrencias, serFrequente);
        });
        System.out.println("------------------------------------------------------------------------------");
    }
}