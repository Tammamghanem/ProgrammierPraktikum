package ga.framework;
import ga.framework.model.Problem;
import ga.framework.model.Solution;
import ga.framework.model.NoSolutionException;
import ga.framework.operators.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {
    Problem problem;
    int populationSize;
    List<EvolutionaryOperator> evolutionaryOperator;
    FitnessEvaluator fitnessEvaluator;
    SurvivalOperator survivalOperator;
    SelectionOperator selectionOperator;
    int maxIterations;

    public GeneticAlgorithm() {
        evolutionaryOperator = new ArrayList<>();
    }

    public GeneticAlgorithm(GeneticAlgorithm geneticAlgorithm) {
        this.problem = geneticAlgorithm.problem;
        this.populationSize = geneticAlgorithm.populationSize;
        this.evolutionaryOperator = geneticAlgorithm.evolutionaryOperator;
        this.fitnessEvaluator = geneticAlgorithm.fitnessEvaluator;
        this.survivalOperator = geneticAlgorithm.survivalOperator;
        this.selectionOperator = geneticAlgorithm.selectionOperator;
        this.maxIterations = geneticAlgorithm.maxIterations;
    }

    public GeneticAlgorithm(Problem problem, int populationSize, List<EvolutionaryOperator> evolutionaryOperator,
                            FitnessEvaluator fitnessEvaluator, SurvivalOperator survivalOperator, SelectionOperator selectionOperator,
                            int maxIterations) {
        this.problem = problem;
        this.populationSize = populationSize;
        this.evolutionaryOperator = evolutionaryOperator;
        this.fitnessEvaluator = fitnessEvaluator;
        this.survivalOperator = survivalOperator;
        this.selectionOperator = selectionOperator;
        this.maxIterations = maxIterations;
    }
    public SetProblem WithProblem(Problem problem) {
        this.problem = problem;
        return new SetProblem();
    }
    public class SetProblem{
        public SetPopulationSize withPopulationSize(int populationSizeX) {
            populationSize = populationSizeX;
            return new SetPopulationSize();
        }
    }
    public class SetPopulationSize {
        public SetPopulationSize withEvolutionaryOperator(EvolutionaryOperator evolutionaryOperatorX) {
            evolutionaryOperator.add(evolutionaryOperatorX);
            return new SetPopulationSize();
        }
        public SetEvolutionaryOperator withEvolutionaryOperator(List<EvolutionaryOperator> evolutionaryOperatorX) {
            evolutionaryOperator = evolutionaryOperatorX;
            return new SetEvolutionaryOperator();
        }
        public SetFitnessEvaluator withFitnessEvaluator(FitnessEvaluator fitnessEvaluatorX) {
            fitnessEvaluator = fitnessEvaluatorX;
            return new SetFitnessEvaluator();
        }
    }
    public class SetEvolutionaryOperator {
        public SetFitnessEvaluator withFitnessEvaluator(FitnessEvaluator fitnessEvaluatorX) {
            fitnessEvaluator = fitnessEvaluatorX;
            return new SetFitnessEvaluator();
        }
    }
    public class SetFitnessEvaluator {

        public SetSurvivalOperator withSurvivalOperator(SurvivalOperator survivalOperatorX) {
            survivalOperator = survivalOperatorX;
            return new SetSurvivalOperator();
        }
    }
    public class SetSurvivalOperator {

        public SetSelectionOperator withSelectionOperator(SelectionOperator selectionOperatorX) {
            selectionOperator = selectionOperatorX;
            return new SetSelectionOperator();
        }
    }
    public class SetSelectionOperator {

        public SetMaxIterations withMaxIterations(int maxIterationsX) {
            maxIterations = maxIterationsX;
            return new SetMaxIterations(GeneticAlgorithm.this);
        }

    }

    public class SetMaxIterations {
        GeneticAlgorithm geneticAlgorithm;
        public SetMaxIterations(GeneticAlgorithm geneticAlgorithmX) {
            geneticAlgorithm = geneticAlgorithmX;
        }
        public GeneticAlgorithm build() {
            return geneticAlgorithm;
        }
    }





    public List<Solution> RunOptimization() {
        List<Solution> population = new ArrayList<>();
        //create initial population
        for (int i = 0; i < populationSize; i++) {
            try {
                population.add(problem.createNewSolution());
            } catch (NoSolutionException e) {
                e.printStackTrace();
                return null;
            }
        }
        //evaluate fitness of population
        fitnessEvaluator.evaluate(population);
        for (int abc = 0; abc < maxIterations; abc++) {
            //evolve population
            Random r = new Random();
            EvolutionaryOperator evolutionaryOperator = this.evolutionaryOperator.get(r.nextInt(this.evolutionaryOperator.size()));
            List<Solution> children = new ArrayList<>();
            for (int i = 0; i < populationSize; i++) {
                try {
                    children.add(evolutionaryOperator.evolve(selectionOperator.selectParent(population)));
                } catch (EvolutionException e) {
                    e.printStackTrace();
                }
            }
            population.addAll(children);
            //evaluate fitness of population
            fitnessEvaluator.evaluate(population);
            //select population
            try {
                population = survivalOperator.selectPopulation(population, populationSize);
            } catch (SurvivalException e) {
                e.printStackTrace();
            }
        }
        //return die Population with the best fitness
        return population;
    }

    public static void main(String[] args) {
        GeneticAlgorithm ga = new GeneticAlgorithm();
        List<Solution> solutions = (List<Solution>) ga.WithProblem(null)
                .withPopulationSize(10)
                .withEvolutionaryOperator(new LinkedList<>())
                .withFitnessEvaluator(null)
                .withSurvivalOperator(null)
                .withSelectionOperator(null)
                .withMaxIterations(20)
                .build();
        solutions = ga.RunOptimization();
    }
}




















