package Microblog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by wyc on 2015/5/6.
 */
public class DataReader {
  public Thread[] readData(String path) {
    File dataPath = new File(path);
    File[] datafiles = dataPath.listFiles();
    ArrayList<Thread> dataset = new ArrayList<>();
    int pfile = 0;
    ArrayList<Node> nodes = null;
    long preThreadID = 0;
    for (File file : datafiles) {
      //System.out.println(file);
      try {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
          JSONObject obj = new JSONObject(line);
          long curThreadID = Long.parseLong(obj.getString("threadid"));
          if (curThreadID != preThreadID) {
            if (nodes != null) {
              dataset.add(new Thread(preThreadID, nodes));
              pfile++;
            }
            nodes = new ArrayList<>();
          }
          //if (nodes.size() < 5)
          nodes.add(new Node(obj));
          preThreadID = curThreadID;
        }
        br.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
      /*for (Node node : nodes)
        System.out.println(node.toString());*/
    }
    return dataset.toArray(new Thread[dataset.size()]);
  }
}
