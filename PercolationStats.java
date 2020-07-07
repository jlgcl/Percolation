/* *****************************************************************************
    PERCOLATION COMPUTATIONAL EXPERIMENTS

    Each trial will result in a percolation.

    Repeat the experiment for T times, and use the spec formula to derive an accurate
    threshold, p*.

    use StdRandom to generate random numbers
    use StdStats to compute sample mean and sample std dev.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final int[] pCount;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        // No need to import class if it's in the same package.
        Percolation p = new Percolation(n);
        int count = 0;
        pCount = new int[trials];

        for (int i = 0; i < trials; i++) {

            while (!p.percolates()) {
                int row = StdRandom.uniform(n - 1);
                int col = StdRandom.uniform(n - 1);
                count++;
                p.open(row, col);
                if (p.percolates()) {
                    pCount[i] = p.numberOfOpenSites();
                }
            }
        }
        // OR initialize & define mean/stddev here & return them in respective methods.
    }

    // sample mean of percolation threshold
    public double mean() {
        //StdStats.mean() returns mean of the given array.
        return StdStats.mean(pCount);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(pCount);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96 * stddev() / Math.sqrt(pCount.length));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96 * stddev() / Math.sqrt(pCount.length));
    }

    // test client (see below)
    public static void main(String[] args) {
        // "args" receives inputs

        // Instantiates a class through passing arguments into constructor:
        PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));

        // Use methods from the instantiated class:
        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }

}
