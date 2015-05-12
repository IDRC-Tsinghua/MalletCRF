package Inference;

import Microblog.Node;
import Microblog.Thread;
import cc.mallet.grmm.inference.Inferencer;
import cc.mallet.grmm.inference.JunctionTreeInferencer;
import cc.mallet.grmm.types.Factor;
import cc.mallet.grmm.types.FactorGraph;

public class GraphBuilder {

	public FactorGraph build(FactorTable factorTable, Thread thread) {

		FactorGraph mdl = new FactorGraph();
		for (Node node: thread.nodes) {

            for (Factor nodeFactor : factorTable.nodeFeatureFactor) {
                mdl.addFactor(nodeFactor);
            }
            for (Factor edgeFactor : factorTable.edgeFeatureFactor) {
                mdl.addFactor(edgeFactor);
            }
		}
        return mdl;
	}

    public void inference(FactorGraph mdl) {

        Inferencer inf = new JunctionTreeInferencer();
        inf.computeMarginals(mdl);
    }
}
