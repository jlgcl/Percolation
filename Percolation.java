/* *****************************************************************************
    UNION-FIND ASSIGNMENT: PERCOLATION

    find p*, a probability at which the system percolates:
        - doesn't percolate when p < P*
        - percolates when p > p*
    row and column indices are integers between 1 and n, where (1, 1) is the upper-left site.

    create an object (not Java object) for each site and name them 0 to N^2 - 1.
    Sites are in same component if connected by open sites.
    percolate iff any site on bottom row is connected to site on top row.

    check for connection: same roots

    create an open site: call union(), connect it with the adjacent open sites.
    use Weight Union QU - WeightedQuickUnionUF

    spec: throw IllegalArgumentException error in constructor if n <= 0;
 **************************************************************************** */

/*
    JAVA HINTS

    multidimensional arrays: int[][] foo = new int[][];
        int[] = column;
        int[0] = row;
 */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid;
    private WeightedQuickUnionUF uf; // use w/ uf = new WeightedQuickUnionUF(...)
    private int gridSize;
    private int oneDGridSize;
    private int ufInd;
    private int[] parent;
    private int count;


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        uf = new WeightedQuickUnionUF(n * n);
        grid = new boolean[n - 1][n - 1];
        gridSize = n;
        oneDGridSize = n * n;
        parent = new int[n];

        // per spec:
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = false; // block all grids
            }
            parent[i] = i;
        }
    }

    /// PRIVATE METHODS (CREATED) ///
    // MAP FROM 2D PAIR TO 1D UNION FIND OBJECT INDEX - int xyTo1D(int, int); this is done to use WeightedQuickUnionUF methods.
    // we are converting the array coordinate (row, col) to a corresponding object number [1..N]
    private int xyTo1D(int row, int col) {
        return row * gridSize + col;
    }

    // VALIDATE INDEX (out of range or not) if yes, throw error; this method is used inside open(), and other relevant methods.
    // private void validation(int row, int col) {
    //     if (row < 0 || row >= n || col >= n || col < 0) {
    //         throw new IllegalArgumentException();
    //     }
    // }
    /// END OF PRIVATE METHODS ///

    // opens the site (row, col) if it is not open already
    // if !isOpen, union() to surrounding site
    public void open(int row, int col) {
        if (row < 0 || row >= gridSize || col >= gridSize || col < 0) {
            throw new IllegalArgumentException();
        }
        ufInd = xyTo1D(row, col);

        if (!isOpen(row, col)) {
            grid[row][col] = true;
            count++;
            if (grid[row + 1][col]) {
                uf.union(ufInd, xyTo1D(row + 1, col));
            } else if (grid[row - 1][col]) {
                uf.union(ufInd, xyTo1D(row - 1, col));
            } else if (grid[row][col + 1]) {
                uf.union(ufInd, xyTo1D(row, col + 1));
            } else if (grid[row][col - 1]) {
                uf.union(ufInd, xyTo1D(row, col - 1));
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (grid[row][col]) {
            return true;
        } else {
            return false;
        }
    }

    // is the site (row, col) full?
    // is the root of an object/site's connected component one of the top rows?
    public boolean isFull(int row, int col) {
        boolean result = false;

        for (int i = 0; i < gridSize; i++) {
            if (uf.find(xyTo1D(row, col)) == parent[i]) {
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return count;
    }

    // does the system percolate?
    // does the bottom row connect with the top row? Root of the bottom = top row site?
    public boolean percolates() {
        int bottom = (gridSize * gridSize) - gridSize;
        boolean result = false;

        for (int i = 0; i < gridSize; i++) {
            for (int j = bottom; j < oneDGridSize; j++) {
                if (uf.find(j) == parent[i]) {
                    result = true;
                } else {
                    result = false;
                }
            }
        }
        return result;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(4);
        p.open(1, 1);
        p.open(2, 1);
        p.open(2, 2);
        p.open(3, 2);
        p.open(3, 3);
        p.open(4, 3);
    }
}
