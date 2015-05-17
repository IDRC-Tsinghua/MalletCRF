package Microblog;

public class DataSet {
  public Thread[] threads;
  public int featureNum;
  int nodeFeatureNum;
  int edgeFeatureNum;
  public String[] nodeFeatureNames = new String[]{"NodeEmoji"};
  public String[] edgeFeatureNames = new String[]{"SameAuthor", "Similarity", "Difference",
      "SentimentProp", "AuthorRef", "HashTag", "SameEmoji"};
  public int posDictLength = 345;
  public int neuDictLength = 335;
  public int negDictLength = 328;

  public DataSet(Thread[] threads) {
    this.featureNum = nodeFeatureNames.length + edgeFeatureNames.length;// + posDictLength + neuDictLength + negDictLength;
    this.nodeFeatureNum = nodeFeatureNames.length;// + posDictLength + neuDictLength + negDictLength;
    this.edgeFeatureNum = edgeFeatureNames.length;
    this.threads = threads;
  }

  public int getThreadNum() {
    return threads.length;
  }

  public void extractFeatures() {
    for (Thread thread : this.threads) {
      thread.setNodeFeatures(this.nodeFeatureNames, this.nodeFeatureNum);
      thread.setEdgeFeatures(this.edgeFeatureNames);
      thread.extractFeatures();
      thread.showFeatureValues();
    }
  }
}
