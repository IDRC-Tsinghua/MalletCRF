package Microblog;

import java.util.ArrayList;

public class Thread {
	public long id;
	public ArrayList<Node> nodes;
	public NodeFeature[] nodeFeatures;
	public EdgeFeature[] edgeFeatures;
	
	public Thread(long _id, ArrayList<Node> _nodes) {
		id = _id;
		nodes = _nodes;
	}
}
