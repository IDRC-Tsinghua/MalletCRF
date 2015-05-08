package Microblog;

import java.util.ArrayList;

abstract class Feature {
	String name = "Feature";
	public int[] x;
	public double[] values;
	public double[][] potentials;

	abstract public void extract(ArrayList<Node> nodes);
}
