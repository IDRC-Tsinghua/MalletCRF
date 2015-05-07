package Microblog;

import java.util.ArrayList;

public class Thread {
	public long id;
	public ArrayList<Node> nodes;
	public NodeFeature[] nodeFeatures;
	public EdgeFeature[] edgeFeatures;
	private int nodeCount;
	
	public Thread(long _id, ArrayList<Node> _nodes) {
		id = _id;
		nodes = _nodes;
	}

	public void setNodeNames(String[] featureNames) throws ClassNotFoundException {

		this.nodeFeatureNames = featureNames;
		Class featureClass = Feature.class;
		for(int i=0; i<featureNames.length; i++) {
			Class c = Class.forName(featureNames[i]);
			Object featureObj = c.getInterfaces();
			this.nodeFeatures[i] = (NodeFeature)featureObj;
		}
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
		for(int i=0; i<this.nodeFeatures.length; i++) {

			this.nodeFeatures[i].extract(this.nodes);
		}
	}

	public void extractEdgeFeatures() {
		for(int i=0; i<this.edgeFeatures.length; i++) {

			this.edgeFeautures[i].extract(this.nodes);
		}
	}

	public void extractFeatures() {

	}
	public void getInstance() {


	}
	public void getLabel() {

	}
}
