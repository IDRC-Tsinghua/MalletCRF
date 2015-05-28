package Microblog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class DataSet {
  public Thread[] threads;
  public int featureNum;
  public int nodeFeatureNum;
  public int edgeFeatureNum;
  public String[] nodeFeatureNames = new String[]{"NodeEmoji", "PositiveWord", "NeutralWord",
      "NegativeWord"};
  public String[] edgeFeatureNames = new String[]{"SameAuthor", "Similarity", "SentimentProp",
      "AuthorRef", "HashTag", "SameEmoji"};
  public int posDictLength = 1300;
  public int neuDictLength = 1006;
  public int negDictLength = 631;
  ArrayList<Integer> positiveWords = new ArrayList<>();
  ArrayList<Integer> neutralWords = new ArrayList<>();
  ArrayList<Integer> negativeWords = new ArrayList<>();

  public DataSet(Thread[] threads) {
    this.featureNum = nodeFeatureNames.length + edgeFeatureNames.length;
    this.nodeFeatureNum = nodeFeatureNames.length;
    //this.featureNum = nodeFeatureNames.length + edgeFeatureNames.length + posDictLength + neuDictLength + negDictLength;
    //this.nodeFeatureNum = nodeFeatureNames.length + posDictLength + neuDictLength + negDictLength;
    this.edgeFeatureNum = edgeFeatureNames.length;
    this.threads = threads;
    try {
      BufferedReader br = new BufferedReader(new FileReader("data/positive.txt"));
      String line;
      while ((line = br.readLine()) != null) {
        String[] pair = line.split("\t");
        this.positiveWords.add(Integer.parseInt(pair[0]));
      }
      br.close();
      br = new BufferedReader(new FileReader("data/neutral.txt"));
      while ((line = br.readLine()) != null) {
        String[] pair = line.split("\t");
        this.neutralWords.add(Integer.parseInt(pair[0]));
        ;
      }
      br.close();
      br = new BufferedReader(new FileReader("data/negative.txt"));
      while ((line = br.readLine()) != null) {
        String[] pair = line.split("\t");
        this.negativeWords.add(Integer.parseInt(pair[0]));
      }
      br.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public int getThreadNum() {
    return threads.length;
  }

  public void extractFeatures() {
    for (Thread thread : this.threads) {
      thread.positiveWords = this.positiveWords;
      thread.neutralWords = this.neutralWords;
      thread.negativeWords = this.negativeWords;
      thread.setNodeFeatures(this.nodeFeatureNames);
      thread.setEdgeFeatures(this.edgeFeatureNames);
      thread.extractFeatures();
      //thread.showFeatureValues();
    }
  }
}
