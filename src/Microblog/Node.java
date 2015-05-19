package Microblog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class Node {

  public String name;
  public int number;
  public int depth;
  public int parent;
  public int[] children;
  public int label;
  public String[] mention;
  public String[] hashtag;
  public String[] emoji;
  public int[] word;
  public HashMap<Integer, Integer> vector;

  public Node(JSONObject obj) {
    try {
      name = obj.getString("username");
      parent = obj.getInt("parent");
      number = obj.getInt("docid");
      label = obj.getInt("label") + 1;
      //depth = obj.getInt("depth");
      JSONArray mentionArray = obj.getJSONArray("mention");
      mention = new String[mentionArray.length()];
      for (int i = 0; i < mentionArray.length(); i++)
        mention[i] = mentionArray.getString(i);
      JSONArray hashArray = obj.getJSONArray("hashtag");
      hashtag = new String[hashArray.length()];
      for (int i = 0; i < hashArray.length(); i++)
        hashtag[i] = hashArray.getString(i);
      JSONArray emojiArray = obj.getJSONArray("emoji");
      emoji = new String[emojiArray.length()];
      for (int i = 0; i < emojiArray.length(); i++)
        emoji[i] = emojiArray.getString(i);
      /*JSONArray childrenArray = obj.getJSONArray("children");
      children = new int[childrenArray.length()];
      for (int i = 0; i < childrenArray.length(); i++)
        children[i] = childrenArray.getInt(i);*/
      JSONArray wordArray = obj.getJSONArray("vector");
      word = new int[wordArray.length()];
      vector = new HashMap<>();
      for (int i = 0; i < wordArray.length(); i++) {
        word[i] = wordArray.getJSONArray(i).getInt(0);
        vector.put(wordArray.getJSONArray(i).getInt(0), wordArray.getJSONArray(i).getInt(1));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String toString() {
    return number + "\t" + name + "\t" + parent;
  }
}
