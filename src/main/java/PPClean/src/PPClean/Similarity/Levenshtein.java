package PPClean.src.PPClean.Similarity;

/**
 * Levenshtein String similarity
 */
public class Levenshtein implements StringSimilarity {
    public Levenshtein() {
    }

    /**
     * Calculates Levenshtein String similarity for x and y
     * @param x
     * @param y
     * @return Similarity score in range [0,1]
     */
    @Override
    public double compare(String x, String y) {
        double res = 0;
        int m = x.length();
        int n = y.length();
        // BEGIN SOLUTION

        if (x.equals(y)) {
            return 1;
        }
        if (m == 0) {
            return 1-(double)n/Math.max(m,n);
        }
        if (n == 0) {
            return 1-(double)m/Math.max(m,n);
        }

        int[][] matrix = new int[m+1][n+1];
        for (int i = 0; i < m+1; i++) {
            matrix[i][0] = i;
        }
        for (int j = 0; j < n+1; j++) {
            matrix[0][j] = j;
        }
        for (int k = 1;k <= m; k++) {
            for (int l = 1; l <= n; l++) {
                if (x.charAt(k-1) == y.charAt(l-1)) {
                    matrix[k][l] = matrix[k-1][l-1];
                } else {
                    matrix[k][l] = 1+Math.min(matrix[k][l-1],Math.min(matrix[k-1][l],matrix[k-1][l-1]));
                }
            }
            res = 1-(double)matrix[m][n]/Math.max(m,n);
        }





        // END SOLUTION
        return res;
    }
}
