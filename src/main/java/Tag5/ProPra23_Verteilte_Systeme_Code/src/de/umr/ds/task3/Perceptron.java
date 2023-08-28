package Tag5.ProPra23_Verteilte_Systeme_Code.src.de.umr.ds.task3;

import java.util.Random;

/**
 * A Perceptron holds weights and bias and can be applied to a data vector to
 * predict its class. Weights and bias are initialized randomly.
 */
public class Perceptron {

	    private Vector weights;
        private double bias;

        public Perceptron(int inputDimension) {

            double[] w = new double[inputDimension];
            Random r = new Random();
            for (int i = 0; i < inputDimension; i++) {
                w[i] = r.nextDouble();
            }
            weights = new Vector(w);
            bias = r.nextDouble();
        }


    /**
     * Apply the perceptron to classify a data vector.
     *
     * @param x An input vector
     * @return 0 or 1
     */
    public int predict(Vector x) {

        double res = weights.dot(x) + bias;
        if (res > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public Vector getWeights() {
        return weights;
    }

public double getBias() {
        return bias;
    }

    /**
     * Update the weights of the perceptron.
     *
     * @param delta A vector of the same dimension as the weights
     */
    public void updateWeights(Vector delta) {
        weights = weights.add(delta);



}

        /**
        * Update the bias of the perceptron.
        *
        * @param delta A scalar
        */
        public void updateBias(double delta) {
            bias += delta;
        }
    }



