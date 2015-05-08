package Microblog;

import java.util.ArrayList;

public class Thread {
	public long id;
	public ArrayList<Node> nodes = null;
	public NodeFeature[] nodeFeatures = null;
	String[] nodeFeatureNames = null;
	public EdgeFeature[] edgeFeatures = null;
	String[] edgeFeatureNames = null;
	private int nodeCount;
	
	public Thread(long id, ArrayList<Node> nodes) {
		this.id = id;
		this.nodes = nodes;
    this.nodeCount = this.nodes.size();
	}

	public void setNodeFeatures(String[] featureNames) throws ClassNotFoundException {
		this.nodeFeatureNames = featureNames;
		Class featureClass = Feature.class;
		for(int i = 0; i < featureNames.length; i++) {
			Class c = Class.forName(featureNames[i]);
			Object featureObj = c.getInterfaces();
			this.nodeFeatures[i] = (NodeFeature)featureObj;
		}
    // TODO: add WordFeature separately
	}

	public void setEdgeFeatures(String[] featureNames) throws ClassNotFoundException {
		this.edgeFeatureNames = featureNames;
		Class featureClass = Feature.class;
		for(int i=0; i<featureNames.length; i++) {

			Class c = Class.forName(featureNames[i]);
			Object featureObj = c.getInterfaces();
			this.edgeFeatures[i] = (EdgeFeature)featureObj;
		}
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
				System.out.println(nodefeature.name + ": " + nodefeature.values.toString());
			for (EdgeFeature edgefeature : this.edgeFeatures)
				System.out.println(edgefeature.name + ": " + edgefeature.values.toString());
		} else {
			System.out.println("Features not defined or extracted");
		}
	}
}
