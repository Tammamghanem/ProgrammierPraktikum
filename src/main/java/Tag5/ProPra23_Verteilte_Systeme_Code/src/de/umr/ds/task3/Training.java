package Tag5.ProPra23_Verteilte_Systeme_Code.src.de.umr.ds.task3;

import java.util.ArrayList;
import java.util.Collections;
import Tag5.ProPra23_Verteilte_Systeme_Code.src.de.umr.ds.task3.Visualization;

public class Training {

	private static final double alpha = 0.05; // learning rate
	private static final int epochs = 100; // number of epochs

	/**
	 * A perceptron is trained on a dataset. After each epoch the perceptron's
	 * parameters are updated, the dataset is shuffled and the accuracy is computed.
	 * 
	 * @param perceptron the perceptron to train
	 * @param dataset the training dataset
	 */
	public static void train(Perceptron perceptron, Dataset dataset) {
		ArrayList<DataPoint> dataPoints = dataset.getDataPoints();
		int dataSize = dataPoints.size();

		for (int epoch = 0; epoch < epochs; epoch++) {
			Collections.shuffle(dataPoints);

			for (DataPoint dataPoint : dataPoints) {
				Vector input = dataPoint.getInput();
				int target = dataPoint.getLabel();
				int prediction = perceptron.predict(input);

				if (prediction != target) {
					int delta = target - prediction;
					Vector weightDelta = input.mult(alpha * delta);
					double biasDelta = alpha * delta;

					perceptron.updateWeights(weightDelta);
					perceptron.updateBias(biasDelta);
				}
			}
			double accuracy = Evaluation.accuracy(perceptron, dataset);
			System.out.println("Epoch " + epoch + ": accuracy = " + accuracy);

			Visualization.update(perceptron.getWeights(), perceptron.getBias(), epoch);
		}
	}


	public static void main(String[] args) {

		Dataset dataset = new Dataset(1000);
		Perceptron perceptron = new Perceptron(2);
		train(perceptron, dataset);

	}

}
