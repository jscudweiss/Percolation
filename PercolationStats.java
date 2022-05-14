/* *****************************************************************************
 *  Name:    Jonah Scudere-Weiss
 *  NetID:   C70275911
 *  Precept: P00
 *
 *  Description:  hekjwhekjwh
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    // the stored trial threshold values
    private double[] trialThresh;

    /**
     * perform independent trials on an n-by-n grid
     *
     * @param n,      the size n of the grid
     * @param trials, the number of trials
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0) {
            throw new IllegalArgumentException(
                    "Size input N : " + n + " must be greater than 0");
        }
        if (trials <= 0) {
            throw new IllegalArgumentException(
                    "Trial Count input : " + trials + " must be greater than 0");
        }
        trialThresh = new double[trials];
        int fullSize = n * n;
        int randRow;
        int randCol;
        for (int count = 0; count < trials; count++) {
            Percolation percolator = new Percolation(n);
            while (!percolator.percolates()) {
                randRow = StdRandom.uniform(n);
                randCol = StdRandom.uniform(n);
                percolator.open(randRow, randCol);
            }
            trialThresh[count] = (double) (percolator.numberOfOpenSites()) / fullSize;
        }
    }

    /**
     * sample mean of percolation threshold
     *
     * @return the mean of the thresholds
     */
    public double mean() {
        double meanVal = StdStats.mean(trialThresh);
        return meanVal;
    }

    //

    /**
     * sample standard deviation of percolation threshold
     *
     * @return the standard deviation of perc threshhold
     */
    public double stddev() {
        double devStd = StdStats.stddev(trialThresh);
        return devStd;
    }

    //

    /**
     * low endpoint of 95% confidence interval
     *
     * @return the low endpoint of 95% confidence interval
     */
    public double confidenceLow() {
        return this.mean() - this.stddev();
    }

    //

    /**
     * high endpoint of 95% confidence interval
     *
     * @return the high endpoint of 95% confidence interval
     */
    public double confidenceHigh() {
        return this.mean() + this.stddev();
    }

    // test client (see below)
    public static void main(String[] args) {
        int startVal = Integer.parseInt(args[0]);
        int trialRuns = Integer.parseInt(args[1]);
        double timer;
        Stopwatch stopwatch = new Stopwatch();
        PercolationStats percStat = new PercolationStats(startVal, trialRuns);
        timer = stopwatch.elapsedTime();
        StdOut.printf(
                "mean()\t\t\t\t= %.3f\n"
                        + "stddev()\t\t\t= %.3f\n"
                        + "confidenceLow\t\t= %.3f\n"
                        + "confidenceHigh\t\t= %.3f\n"
                        + "elapsed time\t\t= %.3f",
                percStat.mean(), percStat.stddev(), percStat.confidenceLow(),
                percStat.confidenceHigh(), timer);

    }
}

