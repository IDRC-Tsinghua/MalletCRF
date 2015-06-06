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
  public Thread[] readData(String[] paths, int limit, int partition) {
    ArrayList<Thread> dataset = new ArrayList<>();
    for (String path : paths) {
      System.out.println(path);
      File dataPath = new File(path);
      File[] datafiles = dataPath.listFiles();
      ArrayList<Node> nodes = null;
      long preThreadID = 0;
      for (File file : datafiles) {
        System.out.println(file);
        try {
          BufferedReader br = new BufferedReader(new FileReader(file));
          String line;
          while ((line = br.readLine()) != null) {
            JSONObject obj = new JSONObject(line);
            long curThreadID = Long.parseLong(obj.getString("threadid"));
            if (curThreadID != preThreadID) {
              if (nodes != null) {
                dataset.add(new Thread(preThreadID, nodes));
              }
              nodes = new ArrayList<>();
            }
            if (nodes.size() < limit)
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
      dataset.add(new Thread(preThreadID, nodes)); // add the last thread
    }
    if (partition >= 5)
      return dataset.toArray(new Thread[dataset.size()]);
    ArrayList<Thread> partDataset = new ArrayList<>();
    for (int i = 0; i < dataset.size(); i++) {
      if (i % 5 < partition)
        partDataset.add(dataset.get(i));
    }
    return partDataset.toArray(new Thread[partDataset.size()]);
  }
}
