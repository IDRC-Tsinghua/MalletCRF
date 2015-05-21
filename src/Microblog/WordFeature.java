package Microblog;

import Utils.Constant;

import java.util.ArrayList;

/**
 * Created by wyc on 2015/5/7.
 */
public class WordFeature extends NodeFeature {
  public int index;
  public int polarity; //0, 1, 2

  public WordFeature(int index, int polarity) {
    this.index = index;
    this.polarity = polarity;
    this.name = "Word-" + polarity + "-" + index;
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
    this.potentials = new double[nodes.size()][this.choiceNum*3];
    for (int n = 0; n < nodes.size(); n++) {
      for (int p = 0; p < this.choiceNum * 3; p++)
        this.potentials[n][p] = Constant.minPtl;
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

  public void extract(ArrayList<Node> nodes, ArrayList<Integer> words) {
    this.extract(nodes);
  }
}

abstract class SentimentWord extends NodeFeature {
  int polarity;

  public SentimentWord() {
    this.name = "SentimentWord";
    this.choiceNum = 10;
  }

  public void extract(ArrayList<Node> nodes) {
    System.out.println("Cannot extract SentimentWord without dict.");
  }

  public void extract(ArrayList<Node> nodes, ArrayList<Integer> words) {
    // compute x
    this.x = new int[nodes.size()];
    for (int n = 0; n < nodes.size(); n++) {
      this.x[n] = 0;
      Node node = nodes.get(n);
      for (int i : node.word) {
        if (words.contains(i))
          this.x[n] += 1;
      }
      this.x[n] = Math.min(10, this.x[n]);
    }
    // compute values
    this.values = new double[nodes.size()];
    for (int n = 0; n < nodes.size(); n++) {
      this.values[n] = 0;
      Node node = nodes.get(n);
      if (this.x[n] > 0 && node.label == this.polarity)
        this.values[n] = (double) this.x[n];
    }
    // compute potentials
    this.potentials = new double[nodes.size()][this.choiceNum * 3];
    for (int n = 0; n < nodes.size(); n++) {
      for (int p = 0; p < this.choiceNum * 3; p++)
        this.potentials[n][p] = Constant.minPtl;
      for (int c = 0; c < this.choiceNum; c++)
        this.potentials[n][c * 3 + this.polarity] = Math.max((double) c, Constant.minPtl);
    }
  }
}

class PositiveWord extends SentimentWord {

  public PositiveWord() {
    this.name = "PositiveWord";
    this.choiceNum = 10;
    this.polarity = 2;
  }

}

class NeutralWord extends SentimentWord {

  public NeutralWord() {
    this.name = "NeutralWord";
    this.choiceNum = 10;
    this.polarity = 1;
  }

}

class NegativeWord extends SentimentWord {

  public NegativeWord() {
    this.name = "NegativeWord";
    this.choiceNum = 10;
    this.polarity = 0;
  }

}