package Inference;

import cc.mallet.grmm.types.FactorGraph;
import cc.mallet.grmm.types.Variable;
import Microblog.Thread;
import Utils.Constant;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class GraphBuilder {
	public FactorGraph build(Thread[] threads, double[] params, 
			int nodeFeatureNum, int EdgeFeatureNum) {
		
		Variable[] allVars = new Variable[threads.length * (1 + nodeFeatureNum)];
		
		
		for (int i = 0; i < allVars.length; i++)
			allVars[i] = new Variable(3);
		FactorGraph graph = new FactorGraph (allVars);
		
		return graph;
	}
	
	public FactorGraph buildOneGraph(Thread thread, double[] params, 
			int nodeFeatureNum, int EdgeFeatureNum) throws NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		
		Variable[] allVars = new Variable[thread.getNodes().size() * 
		                                (1+ nodeFeatureNum)];
		FactorGraph graph = new FactorGraph(allVars);

		for(int i=0; i<thread.nodeFeatureNames.length; i++) {

			String nodeFeatureName = thread.getNodeFeatureNames()[i];
			String featureFactorName = Constant.featureFactorMap.get(nodeFeatureName);

			Class cls = FactorTable.class;
			Object obj = cls.newInstance();
			// Method method = FactorTable.class.getMethod(featureFactorName, FactorGraph.class);
			Method method = cls.getDeclaredMethod(featureFactorName, FactorTable.class);
			method.invoke(obj, graph);
		}

		for(int j=0; j<thread.edgeFeatureNames.length; j++) {

			String edgeFeatureName = thread.getEdgeFeatureNames()[j];
			String edgeFactorName = Constant.featureFactorMap.get(edgeFeatureName);
			Class cls = FactorTable.class;
			Object obj = cls.newInstance();
			// Method method = FactorTable.class.getMethod(featureFactorName, FactorGraph.class);
			Method method = cls.getDeclaredMethod(edgeFactorName, FactorTable.class);
			method.invoke(obj, graph);
		}
		return graph;
	}
}