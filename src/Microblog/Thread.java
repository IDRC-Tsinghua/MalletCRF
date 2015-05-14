package Microblog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Thread {
	public long id;
	public ArrayList<Node> nodes = null;
	public NodeFeature[] nodeFeatures = null;
	String[] nodeFeatureNames = null;
	public int nodeFeatureNum;
	public EdgeFeature[] edgeFeatures = null;
	String[] edgeFeatureNames = null;
	public int edgeFeatureNum;
	private int nodeCount;

	public Thread(long id, ArrayList<Node> nodes) {
		this.id = id;
		this.nodes = nodes;
		this.nodeCount = this.nodes.size();
	}

	public void setNodeFeatures(String[] featureNames, int nodeFeatureNum) {
		this.nodeFeatureNames = featureNames;
		this.nodeFeatures = new NodeFeature[nodeFeatureNum];
		try {
			for (int i = 0; i < featureNames.length; i++) {
				NodeFeature featureObj = (NodeFeature) Class.forName(
						"Microblog." + featureNames[i]).newInstance();
				this.nodeFeatures[i] = featureObj;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int pf = featureNames.length;
		try {
			BufferedReader br = new BufferedReader(new FileReader("data/positive.txt"));
			String line;
			while ((line = br.readLine()) != null) {
				String[] pair = line.split("\t");
				this.nodeFeatures[pf] = new WordFeature(Integer.parseInt(pair[0]), 2);
				pf++;
			}
			br.close();
			br = new BufferedReader(new FileReader("data/neutral.txt"));
			while ((line = br.readLine()) != null) {
				String[] pair = line.split("\t");
				this.nodeFeatures[pf] = new WordFeature(Integer.parseInt(pair[0]), 1);
				pf++;
			}
			br.close();
			br = new BufferedReader(new FileReader("data/negative.txt"));
			while ((line = br.readLine()) != null) {
				String[] pair = line.split("\t");
				this.nodeFeatures[pf] = new WordFeature(Integer.parseInt(pair[0]), 0);
				pf++;
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.nodeFeatureNum = this.nodeFeatures.length;
	}

	public void setEdgeFeatures(String[] featureNames) {
		this.edgeFeatureNames = featureNames;
		this.edgeFeatures = new EdgeFeature[featureNames.length];
		try {
			for (int i = 0; i < featureNames.length; i++) {
				EdgeFeature featureObj = (EdgeFeature) Class.forName(
						"Microblog." + featureNames[i]).newInstance();
				this.edgeFeatures[i] = featureObj;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.edgeFeatureNum = this.edgeFeatures.length;
	}

	public void extractNodeFeatures() {
		for (NodeFeature nodeFeature : this.nodeFeatures)
			nodeFeature.extract(this.nodes);
	}

	public void extractEdgeFeatures() {
		for (EdgeFeature edgeFeature : this.edgeFeatures)
			edgeFeature.extract(this.nodes);
	}

	public void extractFeatures() {
		this.extractNodeFeatures();
		this.extractEdgeFeatures();
	}

	public void getInstance() {
	}

	public int[] getLabels() {
		int[] labels = new int[this.nodeCount];
		for (int i = 0; i < this.nodeCount; i++)
			labels[i] = this.nodes.get(i).label;
		return labels;
	}

	public void showFeatureValues() {
		if (this.nodeFeatures != null && this.edgeFeatures != null) {
			for (NodeFeature nodefeature : this.nodeFeatures)
				System.out.println(nodefeature.name + ": " + Arrays.toString(nodefeature.x));
			for (EdgeFeature edgefeature : this.edgeFeatures)
				System.out.println(edgefeature.name + ": " + Arrays.toString(edgefeature.x));
		} else {
			System.out.println("Features not defined or extracted");
		}
	}
}
