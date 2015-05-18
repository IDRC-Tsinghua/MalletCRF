import Inference.FactorTable;
import Microblog.*;
import Microblog.Thread;
import Utils.Constant;
import cc.mallet.grmm.inference.Inferencer;
import cc.mallet.grmm.inference.JunctionTreeInferencer;
import cc.mallet.grmm.types.*;

/**
 * Created by root on 5/15/15.
 */
public class BayesianNetwork {

    public FactorTable factorTable;
    public FactorGraph mdl;

    public Thread thread;
    public int nodeFeatureNum;
    public int edgeFeatureNum;
    public VarSet y;
    public Factor[][] nodeFactors;
    public Factor[][] edgeFactors;

    public VarSet[][] xNode;
    public VarSet[][] xEdge;

    /*
     Build the Graph

     */
    public BayesianNetwork(FactorTable factorTable, Thread thread) {

        // init
        this.factorTable = factorTable;
        this.thread = thread;
        mdl = new FactorGraph();
        this.nodeFeatureNum = thread.nodeFeatureNum;
        this.edgeFeatureNum = thread.edgeFeatureNum;
        int nodeCnt = thread.nodes.size();
        nodeFactors = new Factor[nodeFeatureNum][nodeCnt];
        edgeFactors = new Factor[edgeFeatureNum][nodeCnt];
        xNode = new VarSet[nodeFeatureNum][nodeCnt];
        xEdge = new VarSet[edgeFeatureNum][nodeCnt];


        for(int i=0; i<this.nodeFeatureNum; i++) {
            for(int j=0; j<nodeCnt; j++) {

                this.nodeFactors[j][i] = factorTable.nodeFeatureFactor[i];
                xNode[i] = factorTable.nodeFeatureVarSet; // fix the xNode varset
                mdl.addFactor(this.nodeFactors[j][i]);
            }
        }

        for(int i=0; i<this.edgeFeatureNum; i++) {
            for(int j=0; j<nodeCnt; j++) {

                this.edgeFactors[j][i] = factorTable.edgeFeatureFactor[i];
                xEdge[i] = factorTable.edgeFeatureVarSet;// fix the edgeNode va
                mdl.addFactor(this.edgeFactors[j][i]);
            }

        }

    }
    public void inference() {

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
