package Microblog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

abstract public class EdgeFeature extends Feature {
	String name = "EdgeFeature";
}

class SameAuthor extends EdgeFeature {
	String name = "SameAuthor";

	public void extract(ArrayList<Node> nodes) {
		// compute x and values
		this.x = new int[nodes.size()-1];
		this.values = new double[nodes.size()-1];
		for (int n = 1; n < nodes.size(); n++) {
			Node curNode = nodes.get(n);
			Node parNode = nodes.get(curNode.parent);
			if (curNode.name == parNode.name)
				this.x[n] = 1;
			else this.x[n] = 0;
			if (curNode.name == parNode.name && curNode.label == parNode.label)
				this.values[n] = 1.0;
			else this.values[n] = 0.0;
		}
		// compute potentials
		this.potentials = new double[nodes.size()-1][this.choiceNum*3*3];
		for (int n = 1; n < nodes.size(); n++) {
			this.potentials[n][9] = 1.0;
			this.potentials[n][13] = 1.0;
			this.potentials[n][17] = 1.0;
		}
	}
}

class Similarity extends EdgeFeature {
	String name = "Similarity";

	public void extract(ArrayList<Node> nodes) {
		// compute x and values
		this.x = new int[nodes.size()-1];
		this.values = new double[nodes.size()-1];
		for (int n = 1; n < nodes.size(); n++) {
			Node curNode = nodes.get(n);
			Node parNode = nodes.get(curNode.parent);
			if (cosineSim(curNode.vector, parNode.vector) >= simThreshold)
				this.x[n] = 1;
			else this.x[n] = 0;
			if (cosineSim(curNode.vector, parNode.vector) >= simThreshold
					&& curNode.label == parNode.label)
				this.values[n] = 1.0;
			else this.values[n] = 0.0;
		}
		// compute potentials
		this.potentials = new double[nodes.size()-1][this.choiceNum*3*3];
		for (int n = 1; n < nodes.size(); n++) {
			this.potentials[n][9] = 1.0;
			this.potentials[n][13] = 1.0;
			this.potentials[n][17] = 1.0;
		}
	}
}

class Difference extends EdgeFeature {
	String name = "Difference";

	public void extract(ArrayList<Node> nodes) {
		// compute x and values
		this.x = new int[nodes.size()-1];
		this.values = new double[nodes.size()-1];
		for (int n = 1; n < nodes.size(); n++) {
			Node curNode = nodes.get(n);
			Node parNode = nodes.get(curNode.parent);
			if (cosineSim(curNode.vector, parNode.vector) <= diffThreshold)
				this.x[n] = 1;
			else this.x[n] = 0;
			if (cosineSim(curNode.vector, parNode.vector) <= diffThreshold
					&& curNode.label != parNode.label)
				this.values[n] = 1.0;
			else this.values[n] = 0.0;
		}
		// compute potentials
		this.potentials = new double[nodes.size()-1][this.choiceNum*3*3];
		for (int n = 1; n < nodes.size(); n++) {
			this.potentials[n][10] = 1.0;
			this.potentials[n][11] = 1.0;
			this.potentials[n][12] = 1.0;
			this.potentials[n][14] = 1.0;
			this.potentials[n][15] = 1.0;
			this.potentials[n][16] = 1.0;
		}
	}
}

class SentimentProp extends EdgeFeature {
	String name = "SentimentProp";

	public void extract(ArrayList<Node> nodes) {
		// compute x and values
		this.x = new int[nodes.size()-1];
		this.values = new double[nodes.size()-1];
		for (int n = 1; n < nodes.size(); n++) {
			Node curNode = nodes.get(n);
			Node parNode = nodes.get(curNode.parent);
			this.x[n] = 1;
			if (curNode.label == parNode.label)
				this.values[n] = 1.0;
			else this.values[n] = 0.0;
		}
		// compute potentials
		this.potentials = new double[nodes.size()-1][this.choiceNum*3*3];
		for (int n = 1; n < nodes.size(); n++) {
			this.potentials[n][9] = 1.0;
			this.potentials[n][13] = 1.0;
			this.potentials[n][17] = 1.0;
		}
	}
}

class AuthorRef extends EdgeFeature {
	String name = "AuthorRef";

	public void extract(ArrayList<Node> nodes) {
		// compute x and values
		this.x = new int[nodes.size()-1];
		this.values = new double[nodes.size()-1];
		for (int n = 1; n < nodes.size(); n++) {
			Node curNode = nodes.get(n);
			Node parNode = nodes.get(curNode.parent);
			if (Arrays.asList(parNode.mention).contains(curNode.name))
				this.x[n] = 1;
			else this.x[n] = 0;
			if (Arrays.asList(parNode.mention).contains(curNode.name) && curNode.label == parNode.label)
				this.values[n] = 1.0;
			else this.values[n] = 0.0;
		}
		// compute potentials
		this.potentials = new double[nodes.size()-1][this.choiceNum*3*3];
		for (int n = 1; n < nodes.size(); n++) {
			this.potentials[n][9] = 1.0;
			this.potentials[n][13] = 1.0;
			this.potentials[n][17] = 1.0;
		}
	}
}

class HashTag extends EdgeFeature {
	String name = "HashTag";

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
				this.x[n] = 1;
			else this.x[n] = 0;
			if (common.size() > 0 && curNode.label == parNode.label)
				this.values[n] = 1.0;
			else this.values[n] = 0.0;
		}
		// compute potentials
		this.potentials = new double[nodes.size()-1][this.choiceNum*3*3];
		for (int n = 1; n < nodes.size(); n++) {
			this.potentials[n][9] = 1.0;
			this.potentials[n][13] = 1.0;
			this.potentials[n][17] = 1.0;
		}
	}
}

class SameEmoji extends EdgeFeature {
	String name = "SameEmoji";

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
				this.x[n] = 1;
			else this.x[n] = 0;
			if (common.size() > 0 && curNode.label == parNode.label)
				this.values[n] = 1.0;
			else this.values[n] = 0.0;
		}
		// compute potentials
		this.potentials = new double[nodes.size()-1][this.choiceNum*3*3];
		for (int n = 1; n < nodes.size(); n++) {
			this.potentials[n][9] = 1.0;
			this.potentials[n][13] = 1.0;
			this.potentials[n][17] = 1.0;
		}
	}
}

class FollowRoot extends EdgeFeature {
	String name = "HashTag";

	public void extract(ArrayList<Node> nodes) {
		// compute x and values
		this.x = new int[nodes.size()-1];
		this.values = new double[nodes.size()-1];
		Node rootNode = nodes.get(0);
		for (int n = 1; n < nodes.size(); n++) {
			Node curNode = nodes.get(n);
			this.x[n] = 1;
			if (curNode.label == rootNode.label)
				this.values[n] = 1.0;
			else this.values[n] = 0.0;
		}
		// compute potentials
		this.potentials = new double[nodes.size()-1][this.choiceNum*3*3];
		for (int n = 1; n < nodes.size(); n++) {
			this.potentials[n][9] = 1.0;
			this.potentials[n][13] = 1.0;
			this.potentials[n][17] = 1.0;
		}
	}
}