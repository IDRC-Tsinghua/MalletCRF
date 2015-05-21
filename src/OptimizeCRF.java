import Inference.GraphBuilder;
import Microblog.*;
import Microblog.Thread;
import cc.mallet.grmm.inference.Inferencer;
import cc.mallet.grmm.inference.TRP;
import cc.mallet.grmm.types.*;
import cc.mallet.optimize.LimitedMemoryBFGS;
import cc.mallet.optimize.Optimizable.ByGradientValue;
import cc.mallet.optimize.OptimizationException;
import cc.mallet.optimize.Optimizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;


public class OptimizeCRF implements ByGradientValue {

  double[] params;
  Thread[] threads;
  double[] logZ;
  double[] modelExpec;
  GraphBuilder graphBuilder = new GraphBuilder();
  FactorGraph graph;
  Inferencer inf = new TRP();

  public OptimizeCRF(double[] init_params, DataSet dataset) {
    params = new double[init_params.length];
    for (int i = 0; i < init_params.length; i++)
      params[i] = init_params[i];
    threads = dataset.threads;
    logZ = new double[threads.length];
  }

  @Override
  public int getNumParameters() {
    return params.length;
  }

  @Override
  public double getParameter(int i) {
    return params[i];
  }

  @Override
  public void getParameters(double[] container) {
    for (int i = 0; i < params.length; i++)
      container[i] = params[i];
  }

  @Override
  public void setParameter(int i, double new_param) {
    params[i] = new_param;
  }

  @Override
  public void setParameters(double[] new_params) {
    for (int i = 0; i < params.length; i++)
      params[i] = new_params[i];
  }

  double getLogPartitionValue(Thread thread) {
    int nodeFeatureNum = thread.nodeFeatureNum;
    int edgeFeatureNum = thread.edgeFeatureNum;
    int nodeCount = thread.nodes.size();
    double result = 0.0;
    Variable[] labelVar = new Variable[nodeCount];
    for (int n = 0; n < nodeCount; n++)
      labelVar[n] = new Variable(3);
    VarSet labelVarSet = new HashVarSet(labelVar);
    for (AssignmentIterator it=labelVarSet.assignmentIterator(); it.hasNext(); it.next()) {
      double oneExp = 0.0;
      for (int i = 0; i < nodeFeatureNum; i++) {
        for (int n = 0; n < nodeCount; n++) {
          int x = thread.nodeFeatures[i].x[n];
          int ptlIndex = x * 3 + it.assignment().get(labelVar[n]);
          oneExp += thread.nodeFeatures[i].potentials[ptlIndex] * params[i];
        }
      }
      for (int i = 0; i < edgeFeatureNum; i++) {
        for (int n = 0; n < nodeCount-1; n++) {
          int x = thread.edgeFeatures[i].x[n];
          int ptlIndex = x * 9 + it.assignment().get(labelVar[thread.nodes.get(n+1).parent]) * 3 +
              it.assignment().get(labelVar[n+1]);
          oneExp += thread.edgeFeatures[i].potentials[ptlIndex] * params[i + nodeFeatureNum];
        }
      }
      result += Math.exp(oneExp);
    }
    return Math.log(result);
  }

  @Override
  public double getValue() {
    System.out.println("start getValue");
    double logLH = 0.0;
    int pt = 0;
    for (int i = 0; i < threads.length; i++) {
      logZ[i] = getLogPartitionValue(threads[i]);
    }
    for (Thread thread : threads) {
      int pf = 0;
      double threadSum = 0.0;
      for (Feature nodeFeature : thread.nodeFeatures) {
        double featureSum = 0.0;
        for (double value : nodeFeature.values)
          featureSum += value;
        threadSum += params[pf] * featureSum;
        pf++;
      }
      for (Feature edgeFeature : thread.edgeFeatures) {
        double featureSum = 0.0;
        for (double value : edgeFeature.values)
          featureSum += value;
        threadSum += params[pf] * featureSum;
        pf++;
      }
      threadSum -= logZ[pt];
      pt++;
      logLH += threadSum;
    }
    System.out.println("getValue: " + logLH);
    return logLH;
  }

  double[] getModelExpec(Thread thread, double[] params) {
    modelExpec = new double[params.length];
    int nodeFeatureNum = thread.nodeFeatureNum;
    int edgeFeatureNum = thread.edgeFeatureNum;
    int nodeCount = thread.nodes.size();
    VarSet[] xNode = new HashVarSet[nodeFeatureNum];
    for (int f = 0; f < nodeFeatureNum; f++) {
      Variable[] tmp = new Variable[nodeCount];
      for (int v = 0; v < nodeCount; v++)
        tmp[v] = new Variable(thread.nodeFeatures[f].choiceNum);
      xNode[f] = new HashVarSet(tmp);
    }
    VarSet[] xEdge = new HashVarSet[edgeFeatureNum];
    for (int f = 0; f < edgeFeatureNum; f++) {
      Variable[] tmp = new Variable[nodeCount - 1];
      for (int v = 0; v < nodeCount - 1; v++)
        tmp[v] = new Variable(thread.edgeFeatures[f].choiceNum);
      xEdge[f] = new HashVarSet(tmp);
    }
    Variable[] tmp = new Variable[nodeCount];
    for (int v = 0; v < nodeCount; v++)
      tmp[v] = new Variable(3);
    VarSet y = new HashVarSet(tmp);

    graph = graphBuilder.buildWithCRF(xNode, xEdge, y, thread, params);
    inf.computeMarginals(graph);
    Factor single;
    for (int i = 0; i < nodeFeatureNum; i++) {
      NodeFeature feature = thread.nodeFeatures[i];
      for (int n = 0; n < nodeCount; n++) {
        single = inf.lookupMarginal(y.get(n));
        int x = feature.x[n];
        for (int a = 0; a < 3; a++) {
          if (feature.potentials[x * 3 + a] > 0)
            modelExpec[i] += single.value(new Assignment(y.get(n), a)) * feature.potentials[x * 3 + a];
        }
      }
    }
    int edgeCount = nodeCount - 1;
    for (int i = 0; i < edgeFeatureNum; i++) {
      EdgeFeature feature = thread.edgeFeatures[i];
      for (int e = 0; e < edgeCount; e++) {
        Variable[] varList = new Variable[3];
        varList[0] = xEdge[i].get(e);
        if (feature.name == "FollowRoot")
          varList[1] = y.get(0);
        else varList[1] = y.get(thread.nodes.get(e + 1).parent);
        varList[2] = y.get(e + 1);
        Factor tripple = inf.lookupMarginal(new HashVarSet(varList));
        int x = feature.x[e];
        for (int a = 0; a < 9; a++) {
          if (feature.potentials[x * 9 + a] > 0)
            modelExpec[i + nodeFeatureNum] +=
                tripple.value(new Assignment(varList, new int[]{x, a / 3, a % 3})) * feature.potentials[x * 9 + a];
        }
      }
    }
    return modelExpec;
  }

  @Override
  public void getValueGradient(double[] gradient) {
    System.out.println("start getValueGradient");
    for (int i = 0; i < gradient.length; i++)
      gradient[i] = 0.0;
    for (Thread thread : threads) {
      double[] modelExpec = getModelExpec(thread, params);
      for (int i = 0; i < gradient.length; i++) {
        int n = thread.nodeFeatures.length;
        if (i < n) {
          for (double value : thread.nodeFeatures[i].values)
            gradient[i] += value;
        } else {
          for (double value : thread.edgeFeatures[i - n].values)
            gradient[i] += value;
        }
        gradient[i] -= modelExpec[i];
      }
    }
    System.out.println("finish getValueGradient");
  }

  static void trainCRF(int fold) {
    System.out.println("==========Reading Data==========");
    DataReader dataReader = new DataReader();
    int[] folds = new int[4];
    int pf = 0;
    for (int f = 0; f < 5; f++) {
      if (f != fold) {
        folds[pf] = f;
        pf++;
      }
    }
    String[] trainFolds = {"data/weibo/fold_" + folds[0], "data/weibo/fold_" + folds[1],
        "data/weibo/fold_" + folds[2], "data/weibo/fold_" + folds[3]};
    Thread[] threads = dataReader.readData(trainFolds, 6);
    DataSet dataset = new DataSet(threads);
    System.out.println(dataset.getThreadNum());

    System.out.println("==========Extracting Features==========");
    dataset.extractFeatures();

    System.out.println("==========Start Learning==========");
    double[] init_params = new double[dataset.featureNum];
    for (int p = 0; p < dataset.featureNum; p++)
      init_params[p] = 1.0;
    OptimizeCRF crf = new OptimizeCRF(init_params, dataset);
    Optimizer opt = new LimitedMemoryBFGS(crf);
    boolean converged = false;
    try {
      converged = opt.optimize();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (OptimizationException e) {
      e.printStackTrace();
    }

    System.out.println("==========Learning Finished==========");
    System.out.println("converged: " + converged);
    for (int p = 0; p < crf.getNumParameters(); p++)
      System.out.println(crf.getParameter(p));
  }

  static void testCRF(int fold) {
    DataReader dataReader = new DataReader();
    Thread[] threads = dataReader.readData(new String[]{"data/weibo/fold_" + fold}, 30);
    DataSet dataset = new DataSet(threads);
    System.out.println(dataset.getThreadNum());
    dataset.extractFeatures();
    double[] params = new double[dataset.featureNum];
    try {
      BufferedReader br = new BufferedReader(new FileReader("data/params.txt"));
      String line;
      int p = 0;
      while ((line = br.readLine()) != null) {
        params[p] = Double.parseDouble(line.trim());
        p++;
      }
      br.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    int correctNum = 0;
    int totalNum = 0;
    FactorGraph graph;
    GraphBuilder builder = new GraphBuilder();
    Inferencer inf = new TRP();
    Factor single;
    VarSet[] xNode = new HashVarSet[dataset.nodeFeatureNum];
    VarSet[] xEdge = new HashVarSet[dataset.edgeFeatureNum];
    VarSet y = null;
    HashMap<Integer, Integer[]> precision = new HashMap<>();
    for (int l = 0; l < 3; l++)
      precision.put(l, new Integer[]{0, 0});
    HashMap<Integer, Integer[]> recall = new HashMap<>();
    for (int l = 0; l < 3; l++)
      recall.put(l, new Integer[]{0, 0});
    for (Thread thread : dataset.threads) {
      int nodeFeatureNum = thread.nodeFeatureNum;
      int edgeFeatureNum = thread.edgeFeatureNum;
      int nodeCount = thread.nodes.size();
      for (int f = 0; f < nodeFeatureNum; f++) {
        Variable[] tmp = new Variable[nodeCount];
        for (int v = 0; v < nodeCount; v++)
          tmp[v] = new Variable(thread.nodeFeatures[f].choiceNum);
        xNode[f] = new HashVarSet(tmp);
      }
      for (int f = 0; f < edgeFeatureNum; f++) {
        Variable[] tmp = new Variable[nodeCount - 1];
        for (int v = 0; v < nodeCount - 1; v++)
          tmp[v] = new Variable(thread.edgeFeatures[f].choiceNum);
        xEdge[f] = new HashVarSet(tmp);
      }
      Variable[] tmp = new Variable[nodeCount];
      for (int v = 0; v < nodeCount; v++)
        tmp[v] = new Variable(3);
      y = new HashVarSet(tmp);
      graph = builder.buildWithCRF(xNode, xEdge, y, thread, params);
      inf.computeMarginals(graph);
      int[] realY = thread.getLabels();
      for (int v = 0; v < y.size(); v++) {
        single = inf.lookupMarginal(y.get(v));
        int infY = -1;
        double maxPtl = -1;
        for (AssignmentIterator it = single.assignmentIterator(); it.hasNext(); it.next()) {
          if (single.value(it) > maxPtl) {
            maxPtl = single.value(it);
            infY = it.indexOfCurrentAssn();
          }
        }
        if (infY == realY[v]) {
          correctNum += 1;
          precision.get(infY)[0] += 1;
          recall.get(realY[v])[0] += 1;
        }
        totalNum += 1;
        precision.get(infY)[1] += 1;
        recall.get(realY[v])[1] += 1;
      }
      System.out.println(correctNum);
    }
    System.out.println("correct: " + correctNum);
    System.out.println("total: " + totalNum);
    System.out.println("accuracy: " + (double) correctNum / (double) totalNum);
    System.out.println("precision:" +
        " 0 " + precision.get(0)[0] + "/" + precision.get(0)[1] +
        " 1 " + precision.get(1)[0] + "/" + precision.get(1)[1] +
        " 2 " + precision.get(2)[0] + "/" + precision.get(2)[1]);
    System.out.println("recall: " +
        " 0 " + recall.get(0)[0] + "/" + recall.get(0)[1] +
        " 1 " + recall.get(1)[0] + "/" + recall.get(1)[1] +
        " 2 " + recall.get(2)[0] + "/" + recall.get(2)[1]);
  }

  public static void main(String[] args) {
    trainCRF(3);
    //testCRF(2);
  }

}
