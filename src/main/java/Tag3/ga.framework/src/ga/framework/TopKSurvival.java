package ga.framework;


import ga.framework.model.Solution;
import ga.framework.operators.SurvivalException;
import ga.framework.operators.SurvivalOperator;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TopKSurvival implements SurvivalOperator {
        private int k;

        public TopKSurvival(int k) {
                this.k = k;
        }

        public List<Solution> selectPopulation(List<Solution> candidates, int populationSize)
                throws SurvivalException {
                Random r = new Random();
                if (candidates.size() < k) {
                        throw new SurvivalException("Not enough candidates to select population");
                }
                if (populationSize > candidates.size()) {
                        throw new SurvivalException("Population size is greater than candidates size");
                }
                List<Solution> pop =  candidates.stream().sorted(Comparator.comparingDouble(Solution::getFitness)
                        .reversed()).limit(k).collect(Collectors.toList());
                while (pop.size() < populationSize) {
                        Solution candidate = candidates.get(r.nextInt(candidates.size()));
                        if (!pop.contains(candidate)) {
                                pop.add(candidate);
                        }
                }
                return pop;
        }
}
