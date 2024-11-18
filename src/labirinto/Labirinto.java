// imports

package labirinto;
import gerarLabirinto.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Labirinto {
    // arrays para caminhar no labirinto: cima, direita, baixo, esquerda
    private final int[] DIRECOES_X = {-1, 0, 1, 0};
    private final int[] DIRECOES_Y = {0, 1, 0, -1};

    private final LabirintoGui gui;
    private final char[][] labirinto;
    private final int m;
    private final int n;
    private final Map<Integer, SeresRegiao> regioesSeres;
    private final Graph graph;


    public Labirinto(char[][] labirinto, LabirintoGui gui) {
        validarEntrada(labirinto, gui);

        this.gui = gui;
        this.labirinto = labirinto;
        this.m = labirinto.length;
        this.n = labirinto[0].length;
        this.regioesSeres = new HashMap<>();
        this.graph = new Graph(m * n);

        validarLabirinto();
    }

    // validando parametros
    private void validarEntrada(char[][] labirinto, LabirintoGui gui) {
        if (labirinto == null || gui == null)
            throw new IllegalArgumentException("Labirinto e GUI não podem ser nulos");

        if (labirinto.length == 0 || labirinto[0].length == 0)
            throw new IllegalArgumentException("Labirinto não pode estar vazio");
    }

    // labirinto bem formulado/sem caracteres estranhos
    private void validarLabirinto() {
        for (int i = 0; i < m; i++) {
            if (labirinto[i].length != n)
                throw new IllegalArgumentException("Todas as linhas devem ter o mesmo comprimento");

            for (int j = 0; j < n; j++) {
                if (!Character.isLetterOrDigit(labirinto[i][j]) && labirinto[i][j] != ' ')
                    throw new IllegalArgumentException(String.format("Caractere inválido na posição [%d][%d]: %c", i, j, labirinto[i][j]));
            }
        }
    }

    // cria o grafo que representa o labirinto, onde cada célula é um vértice
    private void construirGrafo() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++)
                processarCelula(new Coordenada(i, j));
        }
    }

    // procurando quais células estão conectadas
    private void processarCelula(Coordenada coord) {
        if (!Character.isLetterOrDigit(labirinto[coord.i][coord.j]))
            return;

        try {
            // converte o caractere hexa pra saber onde tem paredes
            int paredes = Character.digit(labirinto[coord.i][coord.j], 16);
            if (paredes < 0 || paredes > 15)
                throw new IllegalArgumentException("Valor inválido de parede");

            // em binário para achar as paredes
            String paredesBin = String.format("%4s", Integer.toBinaryString(paredes)).replace(' ', '0');

            // onde não tem paredes, podemos conectar os vizinhos
            for (int d = 0; d < 4; d++) {
                if (paredesBin.charAt(d) == '0')
                    conectarVizinho(coord, d);
            }
        } catch (NumberFormatException ignored) {
        }
    }

    // conecta uma célula com a vizinha (sem parede)
    private void conectarVizinho(Coordenada atual, int direcao) {
        Coordenada vizinho = new Coordenada(
                atual.i + DIRECOES_X[direcao],
                atual.j + DIRECOES_Y[direcao]
        );

        if (vizinho.isValid(m, n) && Character.isLetterOrDigit(labirinto[vizinho.i][vizinho.j]))
            graph.addEdge(atual.toVertex(n), vizinho.toVertex(n));
    }

    public void processarLabirinto() {
        try {
            construirGrafo();
            Map<Integer, Integer> verticeParaRegiao = identificarRegioes();
            atualizarInterface(verticeParaRegiao);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar labirinto: " + e.getMessage(), e);
        }
    }

    // descobre quais partes do labirinto estão conectadas entre si (regiões)
    private Map<Integer, Integer> identificarRegioes() {
        Map<Integer, Integer> verticeParaRegiao = new HashMap<>();
        int regiaoAtual = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                Coordenada coord = new Coordenada(i, j);
                int vertice = coord.toVertex(n);

                if (Character.isLetterOrDigit(labirinto[i][j]) && !verticeParaRegiao.containsKey(vertice))
                    processarRegiao(vertice, regiaoAtual++, verticeParaRegiao);
            }
        }
        return verticeParaRegiao;
    }

    // procura os seres de cada regiao
    private void processarRegiao(int s, int regiao, Map<Integer, Integer> verticeParaRegiao) {
        CaminhamentoProfundidade dfs = new CaminhamentoProfundidade(graph, s);

        for (int v = 0; v < graph.V(); v++) {
            if (dfs.hasPathTo(v)) {
                verticeParaRegiao.put(v, regiao);
                Coordenada coord = Coordenada.fromVertex(v, n);

                char celula = labirinto[coord.i][coord.j];
                if (Character.isUpperCase(celula))
                    regioesSeres.computeIfAbsent(regiao, k -> new SeresRegiao()).adicionarSer(celula);
            }
        }
    }

    private void atualizarInterface(Map<Integer, Integer> verticeParaRegiao) {
        int[][] regioes = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int vertice = new Coordenada(i, j).toVertex(n);
                regioes[i][j] = verticeParaRegiao.getOrDefault(vertice, -1);
            }
        }

        if (gui != null)
            gui.atualizarRegiao(regioes);
    }

    public void resultados() {
        try {
            if (regioesSeres.isEmpty()) {
                System.out.println("Nenhuma região encontrada.");
                return;
            }

            System.out.println("\n1. Número de regiões isoladas: " + regioesSeres.size());
            System.out.println("\n2. Detalhes de cada região:");

            // Imprime cabeçalho da tabela
            System.out.println("------------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-25s | %-15s | %-10s |\n",
                    "Região", "Ser mais frequente", "Ocorrências", "Símbolo");
            System.out.println("------------------------------------------------------------------------------");

            // Processa e exibe informações de cada região
            AtomicInteger numRegiao = new AtomicInteger(1);
            regioesSeres.keySet().stream()
                    .sorted()
                    .forEach(regiao -> {
                        try {
                            SeresRegiao sr = regioesSeres.get(regiao);
                            if (sr != null) {
                                char frequencia = sr.serMaisFrequente();
                                int ocorrencias = sr.getContagem().getOrDefault(frequencia, 0);

                                TipoSer tipo = TipoSer.fromChar(frequencia);
                                String nome = tipo != null ? tipo.getNome() : "Desconhecido";

                                System.out.printf("| %-10d | %-25s | %-15d | %-10c |\n",
                                        numRegiao.getAndIncrement(), nome, ocorrencias, frequencia);
                            }
                        } catch (Exception e) {
                            System.err.printf("Erro ao processar região %d: %s%n", regiao, e.getMessage());
                        }
                    });

            System.out.println("------------------------------------------------------------------------------");

        } catch (Exception e) {
            System.err.println("Erro ao exibir resultados: " + e.getMessage());
        }
    }
}