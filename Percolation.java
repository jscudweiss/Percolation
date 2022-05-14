/* *****************************************************************************
 *  Name:    Jonah Scudere-Weiss
 *  NetID:   C70275911
 *  Precept: P01
 *
 *  Description:  percolation
 *
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // the array of values representing open and closed sites
    private boolean[] siteGrid;
    // The UF object of unionized sites
    private WeightedQuickUnionUF openSites;
    // The size of the square, number of rows, and number of columns
    private int length;
    // The count of open sites
    private int openCount;
    // the integer value which represents the position of the virtual top
    private int top;
    // the list of root values of unions which are connected to the bottom
    // avoids backwash by tracking the bottom as a row and setting root values to true
    // instead of merging everything via a false bottom
    private boolean[] bottomCheck;


    /**
     * creates n-by-n grid, with all sites initially blocked
     *
     * @param n, the size of the square in rows and columns we test for percolation
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException(
                    "Size input N : " + n + " must be greater than 0");
        }
        openCount = 0;
        length = n;
        openSites = new WeightedQuickUnionUF((n * n) + 1);
        siteGrid = new boolean[(n * n) + 1];
        bottomCheck = new boolean[(n * n) + 1];
        top = n * n;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int curPos = convertList(row, col);
                siteGrid[curPos] = false;
                bottomCheck[curPos] = false;
            }
        }
        bottomCheck[top] = false;
    }


    /**
     * converts (row,col) to an int value usable in a 1d array
     *
     * @param row, the row of the current site
     * @param col, the column of the current site
     * @return an int representing the location of (row, col) as one integer
     */
    private int convertList(int row, int col) {
        int calcConvert = Math.abs((row * length)) + col;
        if (calcConvert == length * length) {
            exceptionChecker(row, col);
            throw new IllegalArgumentException(
                    "Row input " + row + " and Col input"
                            + col + " is outside of length "
                            + length);
        }
        return calcConvert;
    }

    /**
     * opens the site (row, col) if it is not open already
     * <p>
     * this function also performs the union find merges,
     * institutes the usage of a "false top" to determine which pieces are
     * touching the top
     * institutes the usage of "virtual false bottom" which consists of an array
     * which stores At set root position,
     * a boolean representing whether or not the set is touching the bottom,
     *
     * @param row, the row of the current site
     * @param col, the column of the current site
     */
    public void open(int row, int col) {
        exceptionChecker(row, col);
        int curPos = convertList(row, col);
        if (!siteGrid[curPos]) {
            siteGrid[curPos] = true;
            boolean bottomVal;
            int posRoot = openSites.find(curPos);
            bottomVal = bottomCheck[posRoot];
            if (row != 0) {
                int upRoot = openSites.find(convertList(row - 1, col));
                if (siteGrid[upRoot]) {
                    bottomVal = bottomVal ||
                            bottomCheck[upRoot];
                    openSites.union(posRoot, upRoot);
                }
            }
            else {
                int topRoot = openSites.find(top);
                bottomVal = bottomVal || bottomCheck[topRoot];
                openSites.union(posRoot, topRoot);
            }
            if (row < length - 1) {
                int downRoot = openSites.find(convertList(row + 1, col));
                if (siteGrid[downRoot]) {
                    bottomVal = bottomVal ||
                            bottomCheck[downRoot];
                    openSites.union(posRoot, downRoot);
                }
            }
            else {
                // if in bottom row set bottomval to true
                bottomVal = true;
            }
            if (col > 0) {
                int leftRoot = openSites.find(convertList(row, col - 1));
                if (siteGrid[leftRoot]) {
                    bottomVal = bottomVal ||
                            bottomCheck[leftRoot];
                    openSites.union(posRoot, leftRoot);
                }
            }
            if (col < length - 1) {
                int rightRoot = openSites.find(convertList(row, col + 1));
                if (siteGrid[rightRoot]) {
                    bottomVal = bottomVal ||
                            bottomCheck[rightRoot];
                    openSites.union(posRoot, rightRoot);
                }
            }
            openCount++;
            // set the root of the current position
            // to whether or not this is touching a bottom val
            bottomCheck[openSites.find(posRoot)] = bottomVal;
        }

    }

    /**
     * checks to see if input values are valid inputs
     * if inputs are invalid, throws IllegalArgumentException
     *
     * @param row, the row of the input
     * @param col, the column of the input
     */
    private void exceptionChecker(int row, int col) {
        if (row >= length) {
            throw new IllegalArgumentException(
                    "Row input " + row + " is outside of length " + length);
        }
        if (col >= length) {
            throw new IllegalArgumentException(
                    "Col input " + col + " is outside of length " + length);
        }
    }


    /**
     * is the site (row, col) open?
     *
     * @param row, the row of the current site
     * @param col, the column of the current site
     * @return a boolean representing if the site (row, col) is open
     */
    public boolean isOpen(int row, int col) {
        exceptionChecker(row, col);
        int curPos = convertList(row, col);
        return siteGrid[curPos];
    }


    /**
     * is the site (row, col) full?
     * it is full if the set is the set containing the false top
     *
     * @param row, the row of the current site
     * @param col, the column of the current site
     * @return a boolean representing if the site (row, col) is full
     */
    public boolean isFull(int row, int col) {
        exceptionChecker(row, col);
        int curRoot = openSites.find(convertList(row, col));
        boolean topCheck = openSites.find(top) == curRoot;
        return (topCheck);
    }

    /**
     * returns the number of open sites
     *
     * @return an int representing the number of open sites
     */
    public int numberOfOpenSites() {
        return openCount;
    }

    //

    /**
     * does the system percolate?
     *
     * @return a boolean representing whether the system percolates
     */
    public boolean percolates() {
        boolean percol = (bottomCheck[openSites.find(top)]);
        return percol;
    }

    // unit testing (required)

    /**
     * @param args the external commandline arguments
     */
    public static void main(String[] args) {
        int val = Integer.parseInt(args[0]);
        Percolation percolator = new Percolation(val);
        Out out = new Out("D:/CoSci/School/Algorithms/Percolation/myText.txt");
        out.println(val);
        int randRow;
        int randCol;
        while (!percolator.percolates()) {
            randRow = StdRandom.uniform(val);
            randCol = StdRandom.uniform(val);
            percolator.open(randRow, randCol);
            out.println(randRow + " " + randCol);
        }
        out.print(" ");
        randRow = StdRandom.uniform(val);
        randCol = StdRandom.uniform(val);
        StdOut.println("Open Count : " + percolator.numberOfOpenSites());
        StdOut.println("( " + randRow + ", " + randCol + ")");
        StdOut.println("is it Open? : " + percolator.isOpen(randRow, randCol));
        StdOut.println("is it Full? : " + percolator.isFull(3, 10));

    }

}

