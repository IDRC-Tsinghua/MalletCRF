package Microblog;

/**
 * Created by wyc on 2015/5/7.
 */
public class DataSet {
  public Thread[] threads;
  public int featureNum;
  public String[] nodeFeatureNames = new String[]{"NodeEmoji"};
  public String[] edgeFeatureNames = new String[]{"SameAuthor", "Similarity", "Difference",
      "SentimentProp", "AuthorRef", "HashTag", "SameEmoji", "FollowRoot"};
  public int posDictLength = 200;
  public int neuDictLength = 300;
  public int negDictLength = 200;

  public DataSet(Thread[] threads) {
    this.featureNum = nodeFeatureNames.length + edgeFeatureNames.length + posDictLength + neuDictLength + negDictLength;
    this.threads = threads;
  }

  public int getThreadNum() {
    return threads.length;
  }
}
