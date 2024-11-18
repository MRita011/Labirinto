// imports
package labirinto;

public class UnionFind {
    private final int[] parent;
    private final int[] rank;
    private int count;

    public UnionFind(int n) {
        if (n < 0) throw new IllegalArgumentException("Número de elementos não pode ser negativo!!");
        count = n;
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int p) {
        validar(p);
        while (p != parent[p]) {
            // todos os elementos apontam para raiz
            parent[p] = parent[parent[p]];
            p = parent[p];
        }
        return p;
    }

    public int count() {
        return count;
    }

    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    public void union(int p, int q) {
        int raizP = find(p);
        int raizQ = find(q);
        if (raizP == raizQ) return;

        if (rank[raizP] < rank[raizQ])
            parent[raizP] = raizQ;

        else if (rank[raizP] > rank[raizQ])
            parent[raizQ] = raizP;

        else {
            parent[raizQ] = raizP;
            rank[raizP]++;
        }
        count--;
    }

    private void validar(int p) {
        int n = parent.length;
        if (p < 0 || p >= n)
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n-1));
    }
}