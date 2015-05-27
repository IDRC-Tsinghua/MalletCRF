package Microblog;

import Utils.Constant;

import java.util.ArrayList;

abstract public class NodeFeature extends Feature {
	String name = "NodeFeature";

  abstract void extract(ArrayList<Node> nodes, ArrayList<Integer> words);
}

class NodeEmoji extends NodeFeature {

  public NodeEmoji() {
    this.name = "NodeEmoji";
    this.choiceNum = 3; // different from other features
    this.potentials = Constant.NodeEmojiPtl;
  }

  public void extract(ArrayList<Node> nodes) {
    // compute x and values
    this.x = new int[nodes.size()];
    this.values = new double[nodes.size()];
    for (int n = 0; n < nodes.size(); n++) {
      Node node = nodes.get(n);
      int emojiSum = 0;
      for (String e : node.emoji)
        emojiSum += Emoji.getEmojiLabel(e);
      int emojiLabel;
      if (emojiSum > 0)
        emojiLabel = 2;
      else if (emojiSum < 0)
        emojiLabel = 0;
      else
        emojiLabel = 1;
      this.x[n] = emojiLabel;
      if (emojiLabel == node.label | emojiLabel == 1)
        this.values[n] = 1.0;
      else
        this.values[n] = 0.0;
    }
  }

  public void extract(ArrayList<Node> nodes, ArrayList<Integer> words) {
    this.extract(nodes);
  }
}

class SVMLabel extends NodeFeature {

  public SVMLabel() {
    this.name = "SVMLabel";
    this.choiceNum = 3;
    this.potentials = Constant.SVMLabelPtl; // could be adjusted
  }

  public void extract(ArrayList<Node> nodes) {
    this.x = new int[nodes.size()];
    this.values = new double[nodes.size()];
    for (int n = 0; n < nodes.size(); n++) {
      Node node = nodes.get(n);
      this.x[n] = node.svm;
      if (node.svm == node.label)
        this.values[n] = 1.0;
      else
        this.values[n] = 0.0;
    }
  }

  public void extract(ArrayList<Node> nodes, ArrayList<Integer> words) {
    this.extract(nodes);
  }
}