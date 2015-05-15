package Inference;

import Microblog.Node;
import Microblog.NodeFeature;
import Microblog.Thread;
import Utils.Constant;
import cc.mallet.grmm.inference.Inferencer;
import cc.mallet.grmm.inference.JunctionTreeInferencer;
import cc.mallet.grmm.types.*;

import javax.swing.text.TabExpander;
import java.lang.ThreadGroup;

public class GraphBuilder {

    /*

     */
    public FactorGraph buildWithCRF(VarSet[] xNode, VarSet[] xEdge, VarSet y,
                                    Thread thread, double[][] probs) {


        FactorGraph mdl = new FactorGraph();

        // for each node feature
        for(int i=0; i<thread.nodeFeatures.length; i++) {
            // for each node
            int nodeVariableCur = 0;
            for(int j=0; j<thread.nodes.size(); j++) {

                VarSet varSet = new HashVarSet(new Variable[]{
                        xNode[i].get(nodeVariableCur),
                        y.get(nodeVariableCur)
                });
                Factor factor = new TableFactor(varSet,
                        thread.nodeFeatures[i].potentials[j]);
                mdl.addFactor(factor);
                nodeVariableCur += 1;
            }
        }

        // for each edge feature
        for(int i=0; i<thread.edgeFeatures.length; i++) {
            // for each node, from the second node

            int edgeVariableCur = 0;
            for(int j=1; j<thread.nodes.size(); j++) {
                VarSet varSet = new HashVarSet(new Variable[]{

                        xEdge[i].get(edgeVariableCur),
                        y.get(edgeVariableCur),
                        y.get(edgeVariableCur-1)
                });
                Factor factor = new TableFactor(varSet,
                        thread.edgeFeatures[i].potentials[j]);
                mdl.addFactor(factor);
                edgeVariableCur += 1;
            }
        }
    }

	public FactorGraph buildWithBN(FactorTable factorTable, Thread thread) {

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
