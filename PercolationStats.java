/*****************************************************************************
 * Author:       Bryan Goodrich
 * Written:      2/4/2015
 * Last updated: 2/8/2015
 * 
 * Compilation:  javac PercolationStats.java
 * Execution:    java PercolationStats 200 100
 * Dependencies: Percolation.java StdOut.java StdRandom.java
 * 
 * Runs simulations to estimate proportion of open sites required for a 
 * percolation system to percolate from top to bottom
 * 
 ****************************************************************************/

/**
 * <i>Percolation Statistics</i>
 * 
 * This class provides methods for executing a Monte Carlo simulation of a 
 * percolating system. The API here is designed by Robert Sedgewick and 
 * Kevin Wayne as part of their online <i>Coursera</i> class.
 * 
 * @author Bryan Goodrich
 */
public class PercolationStats {
    
    // stores the mean proportion of open sites from this simulation
    private final double mu;
    
    // stores the standard deviation from this simulation
    private final double std;
    
    // stores the proportion of open sites for each run in the simulation
    private double[] run;
    
    /**
     * Creates a Monte Carlo Simulation
     * 
     * Using a percolation grid model of size N, the simulation runs
     * T trials recording the proportion of open sites before the system
     * percolated. The object then stores the mean and standard deviations
     * 
     * @param N  the size of the square grid of the percolation system
     * @param T  the number of trials to run
     */
    public PercolationStats(int N, int T) {
        if (N <= 0) 
            throw new IllegalArgumentException("Invalid argument: non-positive N");
        if (T <= 0) 
            throw new IllegalArgumentException("Invalid argument: non-positive T");
        
        this.run = new double[T];
        
        for (int i = 0; i < T; i++)
            this.run[i] = simulation(N);
        
        if (T == 1) {
            mu = this.run[0];
            std = Double.NaN;
        } else {
            mu = StdStats.mean(this.run);
            std = StdStats.stddev(this.run);
        }
    }
    
    /**
     * Return the mean proportion of open sites from this simulation
     */
    public double mean() { return mu; }
    
    /**
     * Return the standard deviation of p from this simulation
     */
    public double stddev() { return std; }
    
    /**
     * Return the 95% lower bound confidence interval
     */
    public double confidenceLo() { 
        return mu - ((1.96*std) / Math.sqrt(this.run.length)); 
    }
    
    /**
     * Return the 95% upper bound confidence interval
     */
    public double confidenceHi() { 
        return mu + ((1.96*std) / Math.sqrt(this.run.length)); 
    }
    
    /**
     * Test client for running commandline simulations
     * 
     * Client requires 2 command-line parameters
     *   - N for the size of the square grid
     *   - T for the number of trials to execute
     * 
     * Results of this simulation are printed to standard out
     */
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats s = new PercolationStats(N, T);
        StdOut.println("mean                    = " + s.mean());
        StdOut.println("stddev                  = " + s.stddev());
        StdOut.println("95% confidence interval = " + s.confidenceLo() 
                           + ", " + s.confidenceHi());
    }
    
    /**
     * Executes a percolation simulation
     *
     * The simulation opens random sites (uniform distribution)
     * until the system percolates. Then it records the proportion
     * of open cells that were required before the system percolated
     * 
     * @param N  the size of the square grid
     * @return   proportion of open sites of N*N grid
     */
    private double simulation(int N) {
        int i, j, count = 0;
        double p;
        
        Percolation perc = new Percolation(N);
        
        do {
            i = StdRandom.uniform(1, N+1);
            j = StdRandom.uniform(1, N+1);
            if (!perc.isOpen(i, j)) {
                perc.open(i, j);
                count++;
            }
        } while (!perc.percolates());
        
        p = count * 1.0 / (N*N);
        return p;
    }
}
