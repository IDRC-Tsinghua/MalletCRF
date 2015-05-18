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

    public Variable[][] xNode;
    public Variable[][] xEdge;
    public Variable[] y;


    public Factor[][] nodeFactors;
    public Factor[][] edgeFactors;

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
        nodeFactors = new Factor[nodeCnt][nodeFeatureNum];
        edgeFactors = new Factor[nodeCnt][edgeFeatureNum];
        xNode = new Variable[nodeCnt][nodeFeatureNum];
        xEdge = new Variable[nodeCnt][edgeFeatureNum];
        y = new Variable[nodeCnt];

        for(int j=0; j<nodeCnt; j++) {
            // generate each factor to each node
            FactorTable ftItem = factorTable;
            // set node and edge varset with local variables
            ftItem.setNodeFeatureVarSet(xNode[j], y[j]);
            // add node feature of factor
            for(int i=0; i<this.nodeFeatureNum; i++) {
                // set node feature factor
                ftItem.nodeFeatureFactor[i] = new TableFactor(
                        ftItem.nodeFeatureVarSet[i],
                        ftItem.nodeFeatureProb[i]
                );
                this.nodeFactors[j][i] = ftItem.nodeFeatureFactor[i];
                mdl.addFactor(this.nodeFactors[j][i]);
            }

            if(j == 0) continue;
            ftItem.setEdgeFeatureVarSet(xEdge[j], y[j], y[j-1]);
            // add edge feature of factor
            for(int i=0; i<this.edgeFeatureNum; i++) {
                // set the variables to the edgeFeatureVarSet
                ftItem.edgeFeatureFactor[i] = new TableFactor(
                        ftItem.edgeFeatureVarSet[i],
                        ftItem.edgeFeatureProb[i]
                );
                this.edgeFactors[j][i] = ftItem.edgeFeatureFactor[i];
                mdl.addFactor(this.edgeFactors[j][i]);

            }
        }

        /*
        for(int j=1; j<nodeCnt; j++) {
            this.xEdge[j] = factorTable.edgeVariables;
            this.y[j-1] = factorTable.yParentVariable;
        }
        */

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
