package ga.problems.knapsack;

import ga.framework.model.Solution;
import ga.framework.operators.EvolutionException;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class KnapsackMutation implements ga.framework.operators.EvolutionaryOperator{
    Random r = new Random();

    /**
     * This method is used to mutate a solution.
     * @param solution
     * @return
     * @throws EvolutionException
     */
    @Override
    public Solution evolve(Solution solution) throws EvolutionException {
        KnapsackSolution parent = (KnapsackSolution) solution;
        KnapsackSolution child = parent.copyOf();

        if (r.nextDouble()<=0.5){
            if (!afs.remove(child)){
                this.add(child);
            }
        } else {
            if (!this.add(child)){
                afs.remove(child);
            }
        }
        return child;
    }
    /**
     * This class is used to remove an item from the knapsack.
     */
    class afs{
        /**
         * This method removes an item from the knapsack.
         * @param solution
         * @return
         */
        private static boolean remove(KnapsackSolution solution) {
            PermutationVisit pv = new PermutationVisit(solution.isInside.length);
            for (int i = 0; i < solution.isInside.length; i++) {
                int index = pv.next();
                if (solution.isInside[index]) {
                    solution.isInside[index] = false;
                    solution.weight -= solution.items[index];
                    solution.value -= solution.values[index];
                    return true;
                }
            }
            return false;
        }}

    /**
     * This method adds an item to the knapsack.
     * @param solution
     * @return
     */
    private boolean add(KnapsackSolution solution){
        PermutationVisit pv = new PermutationVisit(solution.isInside.length);
        for (int i = 0; i <solution.isInside.length ; i++) {
            int index = pv.next();
            if (!solution.isInside[index] && solution.weight + solution.items[index] <= solution.capacity) {
                solution.isInside[index] = true;
                solution.weight += solution.items[index];
                solution.value += solution.values[index];
                return true;
            }
        }
        return false;
    }

}

class PermutationVisit{
    List<Integer> indices;
    Random r = new Random();

    /**
     * This method is used to visit all indices of a permutation in a random order.
     * @param rangeLimit
     */

    public PermutationVisit(int rangeLimit){
        this.indices = new ArrayList<>();
        for (int i = 0; i < rangeLimit; i++) {
            indices.add(i);
        }
    }
    /**
     * This method is used to get the next index.
     * @return
     */

    public int next(){
        if (indices.size()==0){
            return -1;
        }
        int index = r.nextInt(indices.size());
        int value = indices.get(index);
        indices.remove(index);
        return value;
    }
}
