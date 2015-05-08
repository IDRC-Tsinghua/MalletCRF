package Microblog;

import java.util.ArrayList;

/**
 * Created by wyc on 2015/5/7.
 */
public class WordFeature extends NodeFeature {
  public String name = "WordFeature";
  public int index;
  public int polarity; //0, 1, 2

  public WordFeature(int index, int polarity) {
    this.index = index;
    this.polarity = polarity;
  }

  public void extract(ArrayList<Node> nodes) {
    // compute x
    this.x = new int[nodes.size()];
    for (int n = 0; n < nodes.size(); n++) {
      this.x[n] = 0;
      Node node = nodes.get(n);
      for (int i : node.word) {
        if (i == this.index) {
          this.x[n] = 1;
          break;
        }
      }
    }
    // compute values
    this.values = new double[nodes.size()];
    for (int n = 0; n < nodes.size(); n++) {
      this.values[n] = 0;
      Node node = nodes.get(n);
      for (int i : node.word) {
        if (i == this.index && node.label == this.polarity) {
          this.values[n] = 1;
          break;
        }
      }
    }
    // compute potentials
    this.potentials = new double[nodes.size()][6];
    for (int n = 0; n < nodes.size(); n++) {
      // potentials should be all 0.0 as default
      switch (this.polarity) {
        case 0:
          this.potentials[n][3] = 1.0;
          break;
        case 1:
          this.potentials[n][4] = 1.0;
          break;
        case 2:
          this.potentials[n][5] = 1.0;
          break;
        default: // should never be reached
          break;
      }
    }
  }
}
