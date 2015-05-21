package Microblog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

abstract public class Feature {
	public String name = "Feature";
	public int choiceNum = 2;
	double simThreshold = 0.1;
	double diffThreshold = 0.0;
	public int[] x;
	public double[] values;
	public double[] potentials;

	abstract public void extract(ArrayList<Node> nodes);

	double cosineSim(HashMap<Integer, Integer> A, HashMap<Integer, Integer> B) {
		if (A.size() == 0 && B.size() == 0)
			return 1.0;
		double sum_AB = 0.0;
		double sum_A2 = 0.0;
		double sum_B2 = 0.0;
		HashSet<Integer> common = new HashSet<>();
		common.addAll(A.keySet());
		common.retainAll(B.keySet());
		for (int key : common)
			sum_AB += A.get(key) * B.get(key);
		for (int key : A.keySet())
			sum_A2 += A.get(key) * A.get(key);
		for (int key : B.keySet())
			sum_B2 += B.get(key) * B.get(key);
		sum_A2 = Math.max(1.0, sum_A2);
		sum_B2 = Math.max(1.0, sum_B2);
		return sum_AB / (Math.sqrt(sum_A2) * Math.sqrt(sum_B2));
	}
}
