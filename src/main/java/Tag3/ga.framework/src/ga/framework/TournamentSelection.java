package Tag3.src.ga.framework;

import ga.framework.model.Solution;
import ga.framework.operators.SelectionOperator;

import java.util.List;
import java.util.Random;

public class TournamentSelection implements SelectionOperator {
    public Solution selectParent(List<Solution> candidates) {
        Random r = new Random();
        int n = candidates.size();
        int i = r.nextInt(n);
        int j = r.nextInt(n);
        if (i==j) {
            return selectParent(candidates);
        }
        if (candidates.get(i).getFitness() >= candidates.get(j).getFitness()) {
            return candidates.get(i);
        } else {
            return candidates.get(j);
        }


    }
}
