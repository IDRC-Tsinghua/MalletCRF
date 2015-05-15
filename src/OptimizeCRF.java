import Inference.FactorTable;
import Inference.GraphBuilder;
import Microblog.*;
import Microblog.Thread;
import cc.mallet.grmm.inference.Inferencer;
import cc.mallet.grmm.inference.TRP;
import cc.mallet.grmm.types.*;
import cc.mallet.optimize.Optimizable.ByGradientValue;


public class OptimizeCRF implements ByGradientValue {

    double[] params;
    Thread[] threads;
    double[] logZ;
    GraphBuilder graphBuilder = new GraphBuilder();

    public OptimizeCRF(double[] init_params, Thread[] dataset) {
        params = new double[init_params.length];
        for (int i = 0; i < init_params.length; i++)
            params[i] = init_params[i];
        threads = dataset;
        logZ = new double[dataset.length];
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
        // TODO: implement partition function
        return 0.0;
    }

    @Override
    public double getValue() {
        double logLH = 0.0;
        int pt = 0;
        for (int i = 0; i < threads.length; i++)
            logZ[i] = getLogPartitionValue(threads[i]);
        for (Thread thread : threads) {
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
        return logLH;
    }

    double[] getModelExpec(Thread thread, double[] params) {
        double[] modelExpec = new double[params.length];
        int nodeFeatureNum = thread.nodeFeatureNum;
        int edgeFeatureNum = thread.nodeFeatureNum;
        int nodeCount = thread.nodes.size();
        VarSet[] xNode = new VarSet[nodeFeatureNum];
        VarSet[] xEdge = new VarSet[edgeFeatureNum];
        VarSet y = new HashVarSet();
        // TODO: change the definition of build function
        FactorGraph graph = graphBuilder.build(new FactorTable(), thread);
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
        return modelExpec;
    }

    @Override
    public void getValueGradient(double[] gradient) {
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
    }

    public static void main(String[] args) {
        DataReader dataReader = new DataReader();
        Thread[] threads = dataReader.readData("data/Interstellar");
        DataSet dataset = new DataSet(threads);
        System.out.println(dataset.getThreadNum());
        dataset.extractFeatures();
        double[] init_params = new double[dataset.featureNum];

		/*OptimizeCRF crf = new OptimizeCRF(init_params, dataset);
        Optimizer opt = new LimitedMemoryBFGS(crf);
		
		boolean converged = false;
		
		try {
			converged = opt.optimize();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		System.out.println(crf.getParameter(0) + ", " + crf.getParameter(1));*/
    }

}
