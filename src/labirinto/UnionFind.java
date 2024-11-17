package labirinto;

public class UnionFind {
    private int[] parent, size;

    // inicializa a estrutura com n elementos
    public UnionFind(int n) {
        parent = new int[n];
        size = new int[n];

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    // encontra o pai do conjunto que contém x
    public int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    // une dois conjuntos com base no tamanho
    public void union(int x, int y) {
        int raizX = find(x);
        int raizY = find(y);

        if (raizX != raizY) {
            if (size[raizX] < size[raizY]) {
                parent[raizX] = raizY;
                size[raizY] += size[raizX];
            } else {
                parent[raizY] = raizX;
                size[raizX] += size[raizY];
            }
        }
    }
}