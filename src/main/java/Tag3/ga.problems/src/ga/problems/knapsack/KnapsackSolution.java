package ga.problems.knapsack;

import ga.framework.model.NoSolutionException;
import ga.framework.model.Problem;
import ga.framework.model.Solution;


public class KnapsackSolution extends ga.framework.model.Solution {
    int[] items;
    int[] values;
    int capacity;
    boolean[] isInside;
    int weight;
    int value;

    public KnapsackSolution(Problem problem, int[] items,int[] values, int capacity) {
        super(problem);
        this.items = items.clone();
        this.values = values.clone();
        this.capacity = capacity;
        this.isInside = new boolean[items.length];
        for (boolean b : isInside) {
            b = false;
        }
        this.weight = 0;
        this.value = 0;
    }

    public KnapsackSolution copyOf(){
        KnapsackSolution copy = new KnapsackSolution(this.getProblem(), this.items,this.values, this.capacity);
        copy.isInside = this.isInside.clone();
        copy.weight = this.weight;
        copy.value = this.value;
        return copy;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < isInside.length; i++) {
            if (isInside[i]) {
                sb.append(items[i]);
                sb.append(",");
            }
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("]");
        sb.append("\n  weight: "+weight+"\t value: "+value);
        return sb.toString();
    }
}