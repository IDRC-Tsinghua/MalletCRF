package Inference;

import cc.mallet.grmm.types.FactorGraph;
import cc.mallet.grmm.types.Variable;
import Microblog.Thread;


public class GraphBuilder {
	public FactorGraph build(Thread[] threads, double[] params, int nodeFeatureNum, int EdgeFeatureNum) {
		
		Variable[] allVars = new Variable[threads.length * (1 + nodeFeatureNum)];
		
		
		for (int i = 0; i < allVars.length; i++)
			allVars[i] = new Variable(3);
		FactorGraph graph = new FactorGraph (allVars);
		
		return graph;
	}
}
