public class QuickFindBG {
    private int[] id;
    private int count;
    
    public QuickFindBG (int N) {
        count = N;
        id = new int[N];
        for (int j = 0; j < N; j++)
            id[j] = j;
    }
    
    public int count () {
        return count;
    }
    
    public int find (int p) {
        return id[p];
    }
    
    public boolean connected (int p, int q) {
        return id[p] == id[q];
    }
    
    public void union (int p, int q) {
        if (connected(p, q)) return;
        int pid = id[p];
        for (int j = 0; j < id.length; j++)
            if (id[j] == pid) id[j] = id[q];
        count --;
    }
    
    public static void main (String[] args) {
        int N = StdIn.readInt();
        QuickFindBG bg = new QuickFindBG(N);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (bg.connected(p,q)) continue;
            bg.union(p, q);
            StdOut.println(p + " " + q);
        }
        StdOut.println(bg.count() + " components");
        StdOut.println("QuickFind Array:");
        for (int j = 0; j < N; j++)
            StdOut.println(j+1 + ": " + bg.find(j));
    }
}
