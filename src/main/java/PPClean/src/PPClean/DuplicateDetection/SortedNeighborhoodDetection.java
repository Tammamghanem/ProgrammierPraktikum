package PPClean.src.PPClean.DuplicateDetection;

import PPClean.src.PPClean.Data.Duplicate;
import PPClean.src.PPClean.Data.Record;
import PPClean.src.PPClean.Data.Table;
import PPClean.src.PPClean.Similarity.RecordSimilarity;

import java.util.*;

/**
 * Duplicate detection algorithm which first sorts the Table (according to a key)
 * and then only compares neighboring Records
 */
public class SortedNeighborhoodDetection implements DuplicateDetection {

    double threshold;
    int windowSize;
    int[] keyComponents;

    /**
     * @param threshold Threshold in range [0,1], Records at least this similar are considered Duplicates
     * @param windowSize Each record is compared with 2*(windowSize-1) neighboring Records
     * @param keyComponents Each component indicates how many characters of the content
     *                      value at the same list position are integrated in the key
     *                      Enter 0 to omit a Record value
     */
    public SortedNeighborhoodDetection(double threshold, int windowSize, int[] keyComponents) {
        this.threshold = threshold;
        this.windowSize = windowSize;
        this.keyComponents = keyComponents;
    }

    /**
     * @param table Table to check for duplicates
     * @param recSim Similarity measure to use for comparing two records
     * @return Set of detected duplicates
     */
    @Override
    public Set<Duplicate> detect(Table table, RecordSimilarity recSim) {
        Set<Duplicate> duplicates = new HashSet<>();
        int numComparisons = 0;
        // BEGIN SOLUTION

        List<Record> records = table.getData();
        Map<Record, String> recordKeyMap = new HashMap<>();
        for (Record r : records) {
            r.generateKey(keyComponents);
            recordKeyMap.put(r, r.getKey());
        }
        records.sort((r1, r2) -> recordKeyMap.get(r1).compareTo(recordKeyMap.get(r2)));

        for (int i = 0; i < records.size(); i++) {
            for (int j = 1; j < i+windowSize && j+i < records.size(); j++) {
                numComparisons++;
                if (recSim.compare(records.get(i), records.get(j+i)) > threshold) {
                    duplicates.add(new Duplicate(records.get(i), records.get(j+i)));
                }
            }
        }



        // END SOLUTION
        System.out.printf("Sorted Neighborhood Detection found %d duplicates after %d comparisons%n", duplicates.size(), numComparisons);
        return duplicates;
    }
}
