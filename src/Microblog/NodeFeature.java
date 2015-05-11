package Microblog;

import java.util.ArrayList;

abstract public class NodeFeature extends Feature {
	String name = "NodeFeature";
}

class NodeEmoji extends NodeFeature {

  public NodeEmoji() {
    this.name = "NodeEmoji";
    this.choiceNum = 3; // different from other features
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
      if (emojiLabel == node.label)
        this.values[n] = 1.0;
      else
        this.values[n] = 0.0;
    }
    // compute potentials
    this.potentials = new double[nodes.size()][this.choiceNum*3];
    for (int n = 0; n < nodes.size(); n++) {
      this.potentials[n][0] = 1.0;
      this.potentials[n][4] = 1.0;
      this.potentials[n][8] = 1.0;
    }
  }
}
