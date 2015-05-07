package Microblog;

import java.util.ArrayList;

abstract class NodeFeature extends Feature {
	String name = "NodeFeature";

  abstract public void extract(ArrayList<Node> nodes);
}

class NodeEmoji extends NodeFeature {
  String name = "NodeEmoji";

  public void extract(ArrayList<Node> nodes) {

  }
}
