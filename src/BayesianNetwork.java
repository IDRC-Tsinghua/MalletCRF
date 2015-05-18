import Inference.FactorTable;
import Microblog.*;
import Microblog.Thread;
import Utils.Constant;
import cc.mallet.grmm.inference.Inferencer;
import cc.mallet.grmm.inference.JunctionTreeInferencer;
import cc.mallet.grmm.types.*;
import cc.mallet.grmm.types.Factor;
import cc.mallet.grmm.types.FactorGraph;
import cc.mallet.grmm.types.VarSet;


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

        for(int n=0; n<nodeCnt; n++) {
            // generate each factor to each node
            FactorTable ftItem = factorTable;
            // set node and edge varset with local variables
            ftItem.setNodeFeatureVarSet(xNode[n], y[n]);
            // add node feature of factor
            for(int i=0; i<this.nodeFeatureNum; i++) {
                // set node feature factor
                ftItem.nodeFeatureFactor[i] = new TableFactor(
                        ftItem.nodeFeatureVarSet[i],
                        ftItem.nodeFeatureProb[i]
                );
                this.nodeFactors[n][i] = ftItem.nodeFeatureFactor[i];
                mdl.addFactor(this.nodeFactors[n][i]);
            }

            if(n == 0) continue;
            ftItem.setEdgeFeatureVarSet(xEdge[n], y[n], y[n-1]);
            // add edge feature of factor
            for(int i=0; i<this.edgeFeatureNum; i++) {
                // set the variables to the edgeFeatureVarSet
                ftItem.edgeFeatureFactor[i] = new TableFactor(
                        ftItem.edgeFeatureVarSet[i],
                        ftItem.edgeFeatureProb[i]
                );
                this.edgeFactors[n][i] = ftItem.edgeFeatureFactor[i];
                mdl.addFactor(this.edgeFactors[n][i]);

            }
        }
    }
    public void inference() {

        Inferencer inf = new JunctionTreeInferencer();
        inf.computeMarginals(mdl);
        // int nodeFeatureNum = thread.nodeFeatureNum;
        // int edgeFeatureNum = thread.edgeFeatureNum;
        // int nodeSize = thread.nodes.size();
        VarSet ySet = new HashVarSet(y);
        Factor ptl = inf.lookupMarginal(ySet);
        int yCur = 0;
        for (AssignmentIterator it = ptl.assignmentIterator (); it.hasNext (); it.next()) {
            int outcome = it.indexOfCurrentAssn ();
            System.out.println (y[yCur]+"  "+outcome+"   "+ptl.value (it));
            yCur ++;
        }


    }

    public static void main(String[] args) {
        // factor table init
        System.out.println("========Reading data========");
        DataReader dataReader = new DataReader();
        Thread[] threads = dataReader.readData("../data/weibo.tsv"); // foobar
        DataSet dataset = new DataSet(threads);
        System.out.println(dataset.getThreadNum());
        //
        int nodeFeatureNum = Constant.nodeFeatureNames.length;
        int edgeFeatureNum = Constant.edgeFeatureNames.length;
        // factor table init
        FactorTable factorTable = new FactorTable(nodeFeatureNum, edgeFeatureNum);
        factorTable.Stats(threads, nodeFeatureNum, edgeFeatureNum);

        // factor graph
        // TODO: Test data
        BayesianNetwork bn = new BayesianNetwork(factorTable,  threads[0]);
        bn.inference();
    }


}
