package Microblog;

/**
 * Created by wyc on 2015/5/7.
 */
public class DataSet {
  public Thread[] threads;
  public int featureNum;
  public String[] nodeFeatureNames;
  public String[] edgeFeatureNames;
  public int posDictLength;
  public int neuDictLength;
  public int negDictLength;

  public DataSet(Thread[] threads) {
    this.posDictLength = 200;
    this.neuDictLength = 300;
    this.negDictLength = 200;
    this.nodeFeatureNames = new String[]{"NodeEmoji"};
    this.edgeFeatureNames = new String[]{"SameAuthor", "Sibling", "Similarity", "Difference",
        "SentimentProp", "AuthorRef", "HashTag", "SameEmoji", "FollowRoot"};
    this.featureNum = nodeFeatureNames.length + edgeFeatureNames.length + posDictLength + neuDictLength + negDictLength;
    this.threads = threads;
  }

  public int getThreadNum() {
    return threads.length;
  }
}
