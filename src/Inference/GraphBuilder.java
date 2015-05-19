package Inference;

import Microblog.EdgeFeature;
import Microblog.NodeFeature;
import Microblog.Thread;
import cc.mallet.grmm.types.*;

public class GraphBuilder {

    /*

     */
    public FactorGraph buildWithCRF(VarSet[] xNode, VarSet[] xEdge, VarSet y,
                                    Thread thread, double[] params) {


        FactorGraph mdl = new FactorGraph();

        // for each node feature
        // double[] potentialValue = new double[params.length];
        for(int i=0; i<thread.nodeFeatures.length; i++) {
            // for each node
            NodeFeature nodeFeature = thread.nodeFeatures[i];
            for(int j=0; j<thread.nodes.size(); j++) {
                // exclude factors of wordFeature when x=0, for the efficiency concerns
                if (i > 0 && nodeFeature.x[j] == 0)
                    continue;
                VarSet varSet = new HashVarSet(new Variable[]{
                    xNode[i].get(j),
                    y.get(j)
                });

                // potentialValue
                int potentialLength = nodeFeature.potentials[j].length;
                double[] potentialValue = new double[potentialLength];
                for (int t = 0; t < potentialLength; t++) {
                    // param times potential
                    potentialValue[t] = params[i] * nodeFeature.potentials[j][t];
                }
                Factor factor = new TableFactor(varSet, potentialValue);
                mdl.addFactor(factor);
                double[] singlePtl = new double[xNode[i].get(j).getNumOutcomes()];
                singlePtl[nodeFeature.x[j]] = 1.0;
                Factor single = new TableFactor(xNode[i].get(j), singlePtl);
                mdl.addFactor(single);
            }
        }

        // for each edge feature
        for(int i=0; i<thread.edgeFeatures.length; i++) {
            // for each node, from the second node
            EdgeFeature edgeFeature = thread.edgeFeatures[i];
            for(int j=1; j<thread.nodes.size(); j++) {
                VarSet varSet;
                if (edgeFeature.name == "FollowRoot")
                    varSet = new HashVarSet(new Variable[]{
                        xEdge[i].get(j - 1),
                        y.get(0),
                        y.get(j)
                    });
                else varSet = new HashVarSet(new Variable[]{
                    xEdge[i].get(j - 1),
                    y.get(thread.nodes.get(j).parent),
                    y.get(j)
                });
                int potentialLength = edgeFeature.potentials[j - 1].length;
                double[] potentialValue = new double[potentialLength];
                for (int t = 0; t < potentialLength; t++) {
                    // param times potential
                    potentialValue[t] = params[i] * edgeFeature.potentials[j - 1][t];
                }
                Factor factor = new TableFactor(varSet, potentialValue);
                mdl.addFactor(factor);
                double[] singlePtl = new double[xEdge[i].get(j - 1).getNumOutcomes()];
                singlePtl[edgeFeature.x[j - 1]] = 1.0;
                Factor single = new TableFactor(xEdge[i].get(j - 1), singlePtl);
                mdl.addFactor(single);
            }
        }
      return mdl;
    }
    public static void main(String[] args) {


    }
}
