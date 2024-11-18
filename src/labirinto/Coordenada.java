package labirinto;

public class Coordenada {
    final int i;
    final int j;

    public Coordenada(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int toVertex(int n) {
        return i * n + j;
    }

    public static Coordenada fromVertex(int v, int n) {
        return new Coordenada(v/n, v % n);
    }

    public boolean isValid(int m, int n) {
        return i >= 0 && i < m && j >= 0 && j < n;
    }
}