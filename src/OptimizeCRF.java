import Inference.GraphBuilder;
import Microblog.*;
import Microblog.Thread;
import cc.mallet.grmm.inference.Inferencer;
import cc.mallet.grmm.inference.TRP;
import cc.mallet.grmm.types.*;
import cc.mallet.optimize.LimitedMemoryBFGS;
import cc.mallet.optimize.Optimizable.ByGradientValue;
import cc.mallet.optimize.Optimizer;


public class OptimizeCRF implements ByGradientValue {

  double[] params;
  Thread[] threads;
  double[] logZ;
  GraphBuilder graphBuilder = new GraphBuilder();

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
          oneExp += thread.nodeFeatures[i].potentials[n][ptlIndex] * params[i];
        }
      }
      for (int i = 0; i < edgeFeatureNum; i++) {
        for (int n = 0; n < nodeCount-1; n++) {
          int x = thread.edgeFeatures[i].x[n];
          int ptlIndex = x * 9 + it.assignment().get(labelVar[thread.nodes.get(n+1).parent]) * 3 +
              it.assignment().get(labelVar[n+1]);
          oneExp += thread.edgeFeatures[i].potentials[n][ptlIndex] * params[i+nodeFeatureNum];
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
      System.out.print(i + " ");
      logZ[i] = getLogPartitionValue(threads[i]);
      System.out.print(logZ[i] + "\n");
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
    System.out.println("start getModelExpec");
    double[] modelExpec = new double[params.length];
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

    FactorGraph graph = graphBuilder.buildWithCRF(xNode, xEdge, y, thread, params);
    Inferencer inf = new TRP();
    inf.computeMarginals(graph);
    for (int i = 0; i < nodeFeatureNum; i++) {
      NodeFeature feature = thread.nodeFeatures[i];
      for (int n = 0; n < nodeCount; n++) {
        Factor single = inf.lookupMarginal(y.get(n));
        int x = feature.x[n];
        for (int a = 0; a < 3; a++) {
          if (feature.potentials[n][x * 3 + a] > 0)
            modelExpec[i] += single.value(new Assignment(y.get(n), a)) * feature.potentials[n][x * 3 + a];
        }
      }
    }
    int edgeCount = nodeCount - 1;
    for (int i = 0; i < edgeFeatureNum; i++) {
      EdgeFeature feature = thread.edgeFeatures[i];
      for (int e = 0; e < edgeCount; e++) {
        Variable[] varList = new Variable[3];
        varList[0] = xEdge[i].get(e);
        varList[1] = y.get(thread.nodes.get(e + 1).parent);
        varList[2] = y.get(e + 1);
        Factor tripple = inf.lookupMarginal(new HashVarSet(varList));
        int x = feature.x[e];
        for (int a = 0; a < 9; a++) {
          if (feature.potentials[e][x * 9 + a] > 0)
            modelExpec[i + nodeFeatureNum] +=
                tripple.value(new Assignment(varList, new int[]{x, a / 3, a % 3})) * feature.potentials[e][x * 9 + a];
        }
      }
    }
    System.out.println("finish getModelExpec");
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

  public static void main(String[] args) {
    System.out.println("==========Reading Data==========");
    DataReader dataReader = new DataReader();
    Thread[] threads = dataReader.readData("data/Interstellar");
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
		}

    System.out.println("==========Learning Finished==========");
		System.out.println("converged: " + converged);
    for (int p = 0; p < crf.getNumParameters(); p++)
		  System.out.println(crf.getParameter(p));
  }

}
