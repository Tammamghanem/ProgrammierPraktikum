

import ga.framework.GeneticAlgorithm;
import ga.framework.TopKSurvival;
import ga.framework.TournamentSelection;
import ga.framework.model.Solution;
import ga.problems.knapsack.KnapsackFitnessEvaluator;
import ga.problems.knapsack.KnapsackMutation;
import ga.problems.knapsack.KnapsackProblem;




import java.util.Comparator;
import java.util.List;

public class ConcreteProblem {

    public static void main(String[] args) {
        int[] weights = new int[]{5,4,4,4,3,3,2,2,1,1};
        int[] values = new int[]{10,8,6,4,7,4,6,3,3,1};
        int capacity = 11;

        KnapsackProblem problem = new KnapsackProblem(capacity, weights,values);
        GeneticAlgorithm ga = new GeneticAlgorithm(new TopKSurvival(4));
        List<Solution> result = ga.WithProblem(problem)
                .withPopulationSize(10)
                .withEvolutionaryOperator(new KnapsackMutation())
                .withFitnessEvaluator(new KnapsackFitnessEvaluator())
                .withSurvivalOperator(new TopKSurvival(4))
                .withSelectionOperator(new TournamentSelection())
                .withMaxIterations(222)
                .build()
                .runOptimization();
        result.sort(Comparator.comparing(Solution::getFitness));
        for (Solution solution : result) {
            System.out.println(solution);
        }
    }
}