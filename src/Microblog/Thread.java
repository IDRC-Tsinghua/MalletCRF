package Microblog;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Thread {
	
	public long id;
	public ArrayList<Node> nodes;
	public NodeFeature[] nodeFeatures;
	public String[] nodeFeatureNames;
	public EdgeFeature[] edgeFeatures;
	public String[] edgeFeatureNames;
	public int nodeCount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String[] getNodeFeatureNames() {
		return nodeFeatureNames;
	}

	public void setNodeFeatureNames(String[] nodeFeatureNames) {
		this.nodeFeatureNames = nodeFeatureNames;
	}


	public String[] getEdgeFeatureNames() {
		return edgeFeatureNames;
	}

	public void setEdgeFeatureNames(String[] edgeFeatureNames) {
		this.edgeFeatureNames = edgeFeatureNames;
	}


	public Thread(long _id, ArrayList<Node> _nodes) {
		this.id = _id;
		this.nodes = _nodes;
		this.nodeCount = _nodes.size();
			
	}
	
	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;

	}

	public void setNodeNames(String[] featureNames) throws ClassNotFoundException {
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
		for(int i=0; i<this.nodeFeatures.length; i++) {

			this.nodeFeatures[i].extract(this.nodes);
		}
	}

	public void extractEdgeFeatures() {
		for(int i=0; i<this.edgeFeatures.length; i++) {
			this.edgeFeatures[i].extract(this.nodes);
		}
	}

	public void extractFeatures() {
		if (this.nodeFeatureNames.length > 0) {
			this.setNodeNames(this.nodeFeatureNames);
		}

	}
	public void getInstance() {


	}
	public void getLabel() {

	
	

	}
	
	
}
