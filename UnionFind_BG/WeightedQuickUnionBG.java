public class WeightedQuickUnionBG {
    private int[] id;
    private int[] size;
    private int   count;
    
    public WeightedQuickUnionBG (int N) {
        count = N;
        id = new int[N];
        size = new int[N];
        for (int i = 0; i < N; i++) {
            id[i] = i;
            size[i] = 1;
        }
    }
    
    public boolean connected (int p, int q) {
        return find(p) == find(q);
    }
    
    public int count () {
        return count;
    }
    
    public int find (int p) {
        while (p != id[p])
            p = id[p];
        return p;
    }
    
    public int get (int p) {
        return id[p];
    }
    
    public void union (int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return;
        
        if (size[rootP] < size[rootQ]) {
            id[rootP] = rootQ;
            size[rootQ] += size[rootP];
        } else {
            id[rootQ] = rootP;
            size[rootP] += size[rootQ];
        }
        count--;
    }
    
    public static void main (String[] args) {
        int N = StdIn.readInt();
        WeightedQuickUnionBG bg = new WeightedQuickUnionBG(N);
        StdOut.println();
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (bg.connected(p,q)) continue;
            bg.union(p, q);
            StdOut.print(p + " " + q + " ");
            for (int j = 0; j < N; j++)
                StdOut.print(bg.get(j) + " ");
            StdOut.println();
        }
        StdOut.println();
        StdOut.println(bg.count() + " components");
        for (int j = 0; j < N; j++)
            StdOut.print(bg.get(j) + " ");
        StdOut.println();
    }
}
