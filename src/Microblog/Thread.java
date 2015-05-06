package Microblog;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class Thread {
	
	private Node[] nodes;
	private NodeFeature[] nodeFeatures;
	private String[] nodeFeatureNames;
	private EdgeFeature[] edgeFeatures;
	private String[] edgeFeatureNames;
	private int nodeCount;
	

	public Thread(Node[] nodeList) {
		this.nodes = nodeList;
		this.nodeCount = nodeList.length;
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
