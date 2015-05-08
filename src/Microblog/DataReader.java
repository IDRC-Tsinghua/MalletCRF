package Microblog;

import org.json.JSONArray;
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
    int thread_num = datafiles.length;
    Thread[] dataset = new Thread[thread_num];
    int pfile = 0;
    for (File file : datafiles) {
      System.out.println(file);
      ArrayList<Node> nodes = new ArrayList<>();
      long threadID = 0;
      try {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
          JSONObject obj = new JSONObject(line);
          nodes.add(new Node(obj));
          if (threadID == 0)
            threadID = Long.parseLong(obj.getString("id"));
        }
        br.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
      for (Node node : nodes)
        System.out.println(node.toString());
      dataset[pfile] = new Thread(threadID, nodes);
      pfile++;
    }
    return dataset;
  }
}
