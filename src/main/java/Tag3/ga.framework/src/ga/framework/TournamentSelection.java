package ga.framework;

import ga.framework.model.Solution;
import ga.framework.operators.SelectionOperator;

import java.util.List;

public class TournamentSelection implements SelectionOperator {
    @Override
    public Solution selectParent(List<Solution> candidates) {
        Solution solutionOne = candidates.get((int) (Math.random() * (candidates.size())));
        Solution solutionTwo = candidates.get((int) (Math.random() * (candidates.size())));

        return solutionTwo.getFitness() >= solutionOne.getFitness() ? solutionTwo : solutionOne;
    }
}