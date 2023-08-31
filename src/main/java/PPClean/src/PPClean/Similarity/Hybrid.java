package PPClean.src.PPClean.Similarity;

import PPClean.src.PPClean.Data.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Record similarity for comparing two Records attribute by attribute either with {@link Levenshtein} or {@link Jaccard}
 */
public class Hybrid implements RecordSimilarity {

    private List<String> policies;
    private final int JACCARD_N = 3;
    private StringSimilarity jaccard;
    private StringSimilarity levenshtein;

    /**
     * @param policies List of comparison policies, write "L" for {@link Levenshtein},
     *                 "J" {@link Jaccard}, and null to skip an attribute.
     *                 The policies are applied in order of attributes (e.g., first policy to first attribute)
     */
    public Hybrid(List<String> policies) {
        this.policies = policies;
        this.jaccard = new Jaccard(JACCARD_N);
        this.levenshtein = new Levenshtein();
    }

    /**
     * Compares two Records attribute by attribute according to {@link #policies}.
     * For Jaccard similarity, a default window size of {@link #JACCARD_N} is used
     * @param r1
     * @param r2
     * @return Similarity score in range [0,1] (1=same, 0=very different)
     */
    @Override
    public double compare(Record r1, Record r2) {
        double res = 0;
        // BEGIN SOLUTION

        List<Double> resL = new ArrayList<>();
        for (int i=0;i<r1.getContent().size();i++) {
            if ("L".equals(policies.get(i))) {
                resL.add(levenshtein.compare(r1.getContent().get(i),r2.getContent().get(i)));
            } else if ("J".equals(policies.get(i))) {
                resL.add(jaccard.compare(r1.getContent().get(i),r2.getContent().get(i)));
            }
        }
        double sum=0;
        for (double num:resL) {
            sum+=num;
        }
        res=sum/resL.size();




        // END SOLUTION
        return res;
    }
}

