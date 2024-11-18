package labirinto;
import java.util.Stack;

public class CaminhamentoProfundidade {
    private boolean[] marked;
    private int[] edgeTo;
    private int s;

    public CaminhamentoProfundidade(Graph g, int s){
        this.s = s;
        marked = new boolean[g.V()];
        edgeTo = new int[g.V()];
        dfs(g,s);
    }

    private void dfs (Graph g, int v){
        marked[v] = true;
        for (int w : g.adj(v)){
            if (!marked[w]){
                edgeTo[w] = v;
                dfs(g,w);
            }
        }
    }

    public boolean hasPathTo (int v){
        return marked[v];
    }

    public Iterable<Integer> pathTo (int v){
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<>();
        while(v != s){
            path.push(v);
            v = edgeTo[v];
        }
        path.push(s);
        return path;
    }
}
