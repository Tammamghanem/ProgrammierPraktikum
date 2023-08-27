package Tag5.ProPra23_Verteilte_Systeme_Code.src.de.umr.ds.task3;

import java.util.ArrayList;

public class Evaluation {

	/**
	 * Applies the model to each data vector in the dataset and computes the
	 * accuracy.
	 * 
	 * @return accuracy
	 */
	public static double accuracy(Perceptron model, Dataset dataset) {

		ArrayList<DataPoint> dataPoints = dataset.getDataPoints();
		int correct = 0;

		for (DataPoint dataPoint : dataPoints) {
			Vector input = dataPoint.getInput();
			int target = dataPoint.getLabel();
			int prediction = model.predict(input);
			if (prediction == target) {
				correct++;
			}
		}
		
		return (double) correct / dataPoints.size();
	}

}
