package Microblog;

import Utils.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

abstract public class EdgeFeature extends Feature {
	public String name = "EdgeFeature";
}

class SameAuthor extends EdgeFeature {

  public SameAuthor() {
    this.name =  "SameAuthor";
    this.potentials = Constant.SamePtl;
  }

	public void extract(ArrayList<Node> nodes) {
		// compute x and values
		this.x = new int[nodes.size()-1];
		this.values = new double[nodes.size()-1];
		for (int n = 1; n < nodes.size(); n++) {
			Node curNode = nodes.get(n);
			Node parNode = nodes.get(curNode.parent);
			if (curNode.name == parNode.name)
				this.x[n-1] = 1;
			else this.x[n-1] = 0;
			if (curNode.name == parNode.name && curNode.label == parNode.label)
				this.values[n-1] = 1.0;
			else this.values[n-1] = 0.0;
		}
	}
}

class Similarity extends EdgeFeature {

  public Similarity() {
    this.name = "Similarity";
    this.potentials = Constant.SamePtl;
  }

	public void extract(ArrayList<Node> nodes) {
		// compute x and values
		this.x = new int[nodes.size()-1];
		this.values = new double[nodes.size()-1];
		for (int n = 1; n < nodes.size(); n++) {
			Node curNode = nodes.get(n);
			Node parNode = nodes.get(curNode.parent);
			if (cosineSim(curNode.vector, parNode.vector) >= simThreshold)
				this.x[n-1] = 1;
			else this.x[n-1] = 0;
			if (cosineSim(curNode.vector, parNode.vector) >= simThreshold
					&& curNode.label == parNode.label)
				this.values[n-1] = 1.0;
			else this.values[n-1] = 0.0;
		}
	}
}

class Difference extends EdgeFeature {

  public Difference() {
    this.name = "Difference";
    this.potentials = Constant.DiffPtl;
  }

	public void extract(ArrayList<Node> nodes) {
		// compute x and values
		this.x = new int[nodes.size()-1];
		this.values = new double[nodes.size()-1];
		for (int n = 1; n < nodes.size(); n++) {
			Node curNode = nodes.get(n);
			Node parNode = nodes.get(curNode.parent);
			if (cosineSim(curNode.vector, parNode.vector) <= diffThreshold)
				this.x[n-1] = 1;
			else this.x[n-1] = 0;
			if (cosineSim(curNode.vector, parNode.vector) <= diffThreshold
					&& curNode.label != parNode.label)
				this.values[n-1] = 1.0;
			else this.values[n-1] = 0.0;
		}
	}
}

class SentimentProp extends EdgeFeature {

  public SentimentProp() {
    this.name = "SentimentProp";
    this.potentials = Constant.SamePtl;
  }

	public void extract(ArrayList<Node> nodes) {
		// compute x and values
		this.x = new int[nodes.size()-1];
		this.values = new double[nodes.size()-1];
		for (int n = 1; n < nodes.size(); n++) {
			Node curNode = nodes.get(n);
			Node parNode = nodes.get(curNode.parent);
			this.x[n-1] = 1;
			if (curNode.label == parNode.label)
				this.values[n-1] = 1.0;
			else this.values[n-1] = 0.0;
		}
	}
}

class AuthorRef extends EdgeFeature {

  public AuthorRef() {
    this.name = "AuthorRef";
    this.potentials = Constant.SamePtl;
  }

	public void extract(ArrayList<Node> nodes) {
		// compute x and values
		this.x = new int[nodes.size()-1];
		this.values = new double[nodes.size()-1];
		for (int n = 1; n < nodes.size(); n++) {
			Node curNode = nodes.get(n);
			Node parNode = nodes.get(curNode.parent);
			if (Arrays.asList(parNode.mention).contains(curNode.name))
				this.x[n-1] = 1;
			else this.x[n-1] = 0;
			if (Arrays.asList(parNode.mention).contains(curNode.name) && curNode.label == parNode.label)
				this.values[n-1] = 1.0;
			else this.values[n-1] = 0.0;
		}
	}
}

class HashTag extends EdgeFeature {

  public HashTag() {
    this.name = "HashTag";
    this.potentials = Constant.SamePtl;
  }

	public void extract(ArrayList<Node> nodes) {
		// compute x and values
		this.x = new int[nodes.size()-1];
		this.values = new double[nodes.size()-1];
		for (int n = 1; n < nodes.size(); n++) {
			Node curNode = nodes.get(n);
			Node parNode = nodes.get(curNode.parent);
			HashSet<String> common = new HashSet<>();
			common.addAll(Arrays.asList(curNode.hashtag));
			common.retainAll(Arrays.asList(parNode.hashtag));
			if (common.size() > 0)
				this.x[n-1] = 1;
			else this.x[n-1] = 0;
			if (common.size() > 0 && curNode.label == parNode.label)
				this.values[n-1] = 1.0;
			else this.values[n-1] = 0.0;
		}
	}
}

class SameEmoji extends EdgeFeature {

  public SameEmoji() {
    this.name = "SameEmoji";
    this.potentials = Constant.SamePtl;
  }

	public void extract(ArrayList<Node> nodes) {
		// compute x and values
		this.x = new int[nodes.size()-1];
		this.values = new double[nodes.size()-1];
		for (int n = 1; n < nodes.size(); n++) {
			Node curNode = nodes.get(n);
			Node parNode = nodes.get(curNode.parent);
			HashSet<String> common = new HashSet<>();
			common.addAll(Arrays.asList(curNode.emoji));
			common.retainAll(Arrays.asList(parNode.emoji));
			if (common.size() > 0)
				this.x[n-1] = 1;
			else this.x[n-1] = 0;
			if (common.size() > 0 && curNode.label == parNode.label)
				this.values[n-1] = 1.0;
			else this.values[n-1] = 0.0;
		}
	}
}

class FollowRoot extends EdgeFeature {

  public FollowRoot() {
    this.name = "FollowRoot";
    this.potentials = Constant.SamePtl;
  }

	public void extract(ArrayList<Node> nodes) {
		// compute x and values
		this.x = new int[nodes.size()-1];
		this.values = new double[nodes.size()-1];
		Node rootNode = nodes.get(0);
		for (int n = 1; n < nodes.size(); n++) {
			Node curNode = nodes.get(n);
			this.x[n-1] = 1;
			if (curNode.label == rootNode.label)
				this.values[n-1] = 1.0;
			else this.values[n-1] = 0.0;
		}
	}
}