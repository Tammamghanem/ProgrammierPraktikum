package ga.problems.knapsack;

import ga.framework.model.Solution;
import java.util.List;

public class KnapsackFitnessEvaluator implements ga.framework.operators.FitnessEvaluator {
    @Override
    public void evaluate(List<Solution> population) {
        population.stream().forEach(solution -> { if (solution instanceof KnapsackSolution)
        {solution.setFitness((double)((KnapsackSolution) solution).value);}
        else {throw new IllegalArgumentException("Solution is not a KnapsackSolution");}});
    }
}
