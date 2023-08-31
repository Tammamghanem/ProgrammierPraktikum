package PPClean.src.PPClean.DuplicateDetection;

import PPClean.src.PPClean.Helper;

import PPClean.src.PPClean.Data.Duplicate;
import PPClean.src.PPClean.Data.Record;
import PPClean.src.PPClean.Data.Table;
import PPClean.src.PPClean.Similarity.RecordSimilarity;

import java.util.*;

public class LSHDetection implements DuplicateDetection {

    // Hashing
    int HASH_BASE = 17;
    int HASH_PRIME = 19;
    // Tokenization
    int tokenSize;
    List<String> tokenUniverse;
    boolean[][] tokenMatrix;
    // MinHashing
    int numMinHashs;
    int[][] signatureMatrix;
    // Locality Sensitive Hashing
    int numBands;
    ArrayList<Hashtable<Integer, List<Integer>>> LSH;
    // Duplicate Detection
    double threshold;

    /**
     * @param tokenSize Number of characters per token
     * @param numMinHashs Number of min hashes
     * @param numBands Number of bands
     * @param threshold Similarity threshold between 0 and 1 to use for filtering duplicates
     */
    public LSHDetection(int tokenSize, int numMinHashs, int numBands, double threshold) {
        if (numMinHashs % numBands != 0) {
            throw new IllegalArgumentException("numMinHashs needs to be divisible by numBands");
        }
        this.tokenSize = tokenSize;
        this.numMinHashs = numMinHashs;
        this.numBands = numBands;
        this.threshold = threshold;
    }

    /**
     * Calculates {@link LSHDetection#tokenUniverse}: a list of all tokens in the entire table
     * and {@link LSHDetection#tokenMatrix}: a boolean matrix with as many rows as there are tokens in
     * the tokenUniverse and as many columns as there are records in the table. A true boolean value in
     * cell (i, j) means that the i-th token appears in the j-th record.
     * The size of tokens is determined by {@link LSHDetection#tokenSize}.
     * @param table Table to use to calculate tokens
     */
    private void calculateTokens(Table table) {
        // BEGIN SOLUTION

        // Tokenize
        tokenUniverse = new ArrayList<>();
        for (Record r : table.getData()) {
            String record = r.toString();
            for (int i = 0; i < record.length() - tokenSize + 1; i++) {
                String token = record.substring(i, i + tokenSize);
                if (!tokenUniverse.contains(token)) {
                    tokenUniverse.add(token);
                }
            }
        }
        // Token Matrix
        tokenMatrix = new boolean[tokenUniverse.size()][table.getData().size()];
        for (int i = 0; i < tokenUniverse.size(); i++) {
            for (int j = 0; j < table.getData().size(); j++) {
                Record r = table.getData().get(j);
                String record = table.getData().get(j).toString();
                tokenMatrix[i][j] = record.contains(tokenUniverse.get(i));
            }
        }


        // END SOLUTION
            }


    /**
     * Calculates {@link LSHDetection#signatureMatrix}: a matrix with {@link LSHDetection#numMinHashs} many rows
     * and as many columns as there are records in the table.
     * An integer value k at cell (i,j) says that for the i-th permutation of the {@link LSHDetection#tokenMatrix}
     * and for the j-th record in the table, a token of record j is at row k and rows 0 to k-1 have no tokens of record j.
     * @param table Table used to calculate min hashes
     */
    private void calculateMinHashes(Table table) {
        // BEGIN SOLUTION

        signatureMatrix = new int[numMinHashs][table.getData().size()];
        boolean[][] shuffledMatrix = Helper.shuffleMatrixRows(tokenMatrix);

        for (int i = 0; i < numMinHashs; i++) {
            for (int j = 0; j < table.getData().size(); j++) {
                int k = 0;
                while (!shuffledMatrix[k][j]) {
                    k++;
                }
                signatureMatrix[i][j] = k;
            }
            shuffledMatrix = Helper.shuffleMatrixRows(shuffledMatrix);
        }




        // END SOLUTION
    }

    private int hash(int[] band) {
        int hash = this.HASH_BASE;
        for (int i : band) {
            hash = hash * this.HASH_PRIME + i;
        }
        return hash;
    }

    /**
     * Calculates a hashtable for every band and adds it to {@link LSHDetection#LSH}. Uses {@link LSHDetection#hash(int[])}
     * to hash a band to an integer. For every hash value we store a list of record ids, these lists represent
     * buckets of duplicate candidates.
     */
    private void calculateHashBuckets() {
        // BEGIN SOLUTION
        LSH = new ArrayList<>(numBands);

        int rowsPerBand = numMinHashs / numBands;

        for (int i = 0; i < numBands; i++) {
            Hashtable<Integer, List<Integer>> hashTable = new Hashtable<>();

            for (int j = 0; j < signatureMatrix[0].length; j++) {
                int[] bandHashes = new int[rowsPerBand];
                for (int k = 0; k < rowsPerBand; k++) {
                    bandHashes[k] = signatureMatrix[i * rowsPerBand + k][j];
                }

                int hashValue = hash(bandHashes);

                if (!hashTable.containsKey(hashValue)) {
                    hashTable.put(hashValue, new ArrayList<>());
                }
                hashTable.get(hashValue).add(j);
            }

            LSH.add(hashTable);
        }




        // END SOLUTION
    }

    /**
     * First calculates tokens, minHashes and hash buckets.
     * Then iterates over all hashtables in {@link LSHDetection#LSH} and over all hash keys to compare all records
     * who share at least one hash bucket.
     * @param table Table to check for duplicates
     * @param recSim Similarity measure to use for comparing two records
     * @return Set of detected duplicates
     */
    @Override
    public Set<Duplicate> detect(Table table, RecordSimilarity recSim) {
        Set<Duplicate> duplicates = new HashSet<>();
        int numComparisons = 0;
        calculateTokens(table);
        calculateMinHashes(table);
        calculateHashBuckets();
        // BEGIN SOLUTION

        for (Hashtable<Integer,List<Integer>> hashTable : LSH) {
            for (List<Integer> bucket: hashTable.values()){
                for (int i = 0; i < bucket.size(); i++) {
                    for (int j = i + 1; j < bucket.size(); j++) {
                        Record r1 = table.getData().get(bucket.get(i));
                        Record r2 = table.getData().get(bucket.get(j));
                        if (recSim.compare(r1, r2) >= threshold) {
                            duplicates.add(new Duplicate(r1, r2));
                        }
                        numComparisons++;
                    }
                }
            }
        }




        // END SOLUTION
        System.out.printf("LSH Detection found %d duplicates after %d comparisons%n", duplicates.size(), numComparisons);
        return duplicates;
    }
}
