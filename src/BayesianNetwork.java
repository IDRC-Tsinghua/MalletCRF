import Inference.FactorTable;
import Microblog.Node;
import Microblog.NodeFeature;
import Microblog.Thread;
import cc.mallet.grmm.inference.Inferencer;
import cc.mallet.grmm.inference.JunctionTreeInferencer;
import cc.mallet.grmm.types.Factor;
import cc.mallet.grmm.types.FactorGraph;
import cc.mallet.grmm.types.VarSet;

/**
 * Created by root on 5/15/15.
 */
public class BayesianNetwork {

    public FactorTable factorTable;
    public int nodeFeatureNum;
    public int edgeFeatureNum;
    public VarSet y;

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


    public void inference(FactorGraph mdl,
                          Microblog.Thread thread) {

        Inferencer inf = new JunctionTreeInferencer();
        inf.computeMarginals(mdl);
        int nodeFeatureNum = thread.nodeFeatureNum;
        int edgeFeatureNum = thread.edgeFeatureNum;
        int nodeSize = thread.nodes.size();
        for(int n = 0; n < nodeFeatureNum; n++) {

            NodeFeature nodeFeature = thread.nodeFeatures[n];
            for(int j = 0; j < nodeSize; j++) {
                // Factor single = inf.lookupMarginal();
            }

        }

        for(int e = 1; e < edgeFeatureNum; e++) {


        }

    }

    public static void main(String[] args) {

        System.out.println("hello world");
    }


}
