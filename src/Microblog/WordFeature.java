package Microblog;

import java.util.ArrayList;

/**
 * Created by wyc on 2015/5/7.
 */
public class WordFeature extends NodeFeature {
  public String name = "WordFeature";
  public int index;
  public int polarity;

  public WordFeature(int _index, int dualDictLength) {
    index = _index;
    if (index * 2 < dualDictLength)
      polarity = 0;
    else
      polarity = 1;
  }

  public void extract(ArrayList<Node> nodes) {

  }
}
