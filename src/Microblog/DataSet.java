package Microblog;

/**
 * Created by wyc on 2015/5/7.
 */
public class DataSet {
  public Thread[] threads;
  public int featureNum;
  public String[] nodeFeatureNames;
  public String[] edgeFeatureNames;
  public int dualDictLength;

  public DataSet(Thread[] _threads) {
    dualDictLength = 1000;
    nodeFeatureNames = new String[]{"NodeEmoji"};
    edgeFeatureNames = new String[]{"SameAuthor", "Sibling", "Similarity", "Difference",
        "SentimentProp", "AuthorRef", "HashTag", "SameEmoji", "FollowRoot"};
    featureNum = nodeFeatureNames.length + edgeFeatureNames.length + dualDictLength;
    threads = _threads;
  }

  public int getThreadNum() {
    return threads.length;
  }
}
