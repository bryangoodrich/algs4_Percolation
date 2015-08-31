/*****************************************************************************
 * Author:       Bryan Goodrich
 * Written:      2/4/2015
 * Last updated: 2/8/2015
 * 
 * Compilation:  javac Percolation.java
 * Execution:    java Percolation input.txt
 * Dependencies: StdOut.java In.java
 * 
 * Models a percolating system to be used in simulations or exploration.
 * 
 ****************************************************************************/

/**
 * <i>Percolation</i> Model.
 * 
 * This class provides methods for modeling a grid based percolating system.
 * The API here is designed by Robert Sedgewick and Kevin Wayne as part of
 * their online <i>Coursera</i> class.
 * 
 * @author Bryan Goodrich
 */
public class Percolation {
    
    // positional pointers to virtual top and bottom of UF data structure
    private final int vTop, vBottom;
    
    // a union-find data structure for maintaining connections in system
    private WeightedQuickUnionUF uf;
    
    // a 2D array for managing system state (open/closed)
    private boolean[][] grid;
    
    /**
     * Creates a percolation system
     * 
     * The model is a square NxN state grid that uses a union-find
     * data structure to manage the site (cell) connections. The top
     * and bottom layers of the grid are attached to virtual sites in
     * the union-find structure at initialization, providing for easier
     * ways to identify percolation across the system
     * 
     * @param N  the size of the square state grid for this model
     */    
    public Percolation(int N) {
        if (N <= 0) 
            throw new IllegalArgumentException("Invalid argument: non-positive N");
        this.uf = new WeightedQuickUnionUF(N*N+2);
        this.grid = new boolean[N][N];
        this.vBottom = N*N+1;
        this.vTop = 0;
        
        // Connect the bottom and top rows to their virtual links
        if (N > 1) {
            for (int k = 1; k <= N; k++) {
                this.uf.union(xyTo1D(N, k), vBottom);
                this.uf.union(xyTo1D(1, k), vTop);
            }
        }
    }
    
    /**
     * Opens a site at the given coordinates
     * 
     * Opening a site includes both changing its grid state as well as
     * connecting it in the union-find structure to its grid neighbors above,
     * below, left, and right if those neighbors are also open.
     * 
     * @param i  x-coordinate of site beginning from northwest corner
     * @param j  y-coordinate of site beginning from northwest corner
     */    
    public void open(int i, int j) {
        grid[i-1][j-1] = true;
        
        if (grid.length == 1) {
            this.uf.union(xyTo1D(i, j), vBottom);
            this.uf.union(xyTo1D(i, j), vTop);
        }
        
        int p = xyTo1D(i, j);
        unionNeighbor(p, i-1, j);  // above
        unionNeighbor(p, i, j-1);  // left
        unionNeighbor(p, i, j+1);  // right
        unionNeighbor(p, i+1, j);  // below
    }
    
    /**
     * Checks if a given site is open
     * 
     * @param i  x-coordinate of site beginning from northwest corner
     * @param j  y-coordinate of site beginning from northwest corner
     * @return   true if grid states this site is open
     */    
    public boolean isOpen(int i, int j) { return grid[i-1][j-1]; }
    
    /**
     * Checks if a given site is full (percolates to these coordinates)
     * 
     * @param i  x-coordinate of site beginning from northwest corner
     * @param j  y-coordinate of site beginning from northwest corner
     * @return   true if the site is open and connected to the top
     */
    public boolean isFull(int i, int j) {
        return isOpen(i, j) && uf.connected(vTop, xyTo1D(i, j));
    }
    
    /**
     * Checks if the system percolates from top to bottom
     * 
     * @return  true if the virtual top and bottom are connected as
     *          represented in the union-find data structure
     */
    public boolean percolates() { return uf.connected(vTop, vBottom); }
    
    /**
     * Test client for executing a model from an input file
     * 
     * The file must begin with grid size N on the first line.
     * Proceeding lines must include pairs of integers separated by
     * whitespaces for which cells in the grid to open
     */
    public static void main(String[] args) {
        In in = new In(args[0]);      // input file
        int N = in.readInt();         // N-by-N percolation system
        
        Percolation perc = new Percolation(N);
        
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
            StdOut.println(i + " " + j);
        }
    }
    
    /**
     * Validates the index is in range
     * 
     * @param i  index to be validated
     * @return true if index between 1 and N, the size of one side of the system
     */
    private boolean valid(int i) {
        int N = grid.length;
        return (i > 0 && i <= N); 
    }
    
    /**
     * Converts grid coordinates to union-find 1-dimensional representative
     * 
     * @param i the x-coordinate beginning from the northwest corner
     * @param j the y-coordinate beginning from the northwest corner
     * @throws  a IndexOutOfBoundsException if index not bound to grid system [1,N]
     * @return  an integer cooresponding to the union-find array position of
     *          this coordinate
     */
    private int xyTo1D(int i, int j) {
        int N = grid.length;
        if (!valid(i)) 
            throw new IndexOutOfBoundsException("row index i out of bounds");
        if (!valid(j)) 
            throw new IndexOutOfBoundsException("column index j out of bounds");
        return (i-1) * N + j;
    }
    
    /**
     * Unions a given point to its open neighbors at the given coordinates
     * 
     * @param p  the union-find position that was opened
     * @param i  the x-coordinate of a neighbor to be joined
     * @param j  the y-coordinate of a neighbor to be joined
     * @see      open
     * @see      xyTo1D
     */ 
    private void unionNeighbor(int p, int i, int j) {
        if (!valid(i) || !valid(j)) return;
        
        if (isOpen(i, j))
            uf.union(p, xyTo1D(i, j));
    }
}
