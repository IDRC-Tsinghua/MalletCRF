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

import java.util.Objects;


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
            FactorTable ftItem = factorTable.clone();
            // set node and edge varset with local variables
            for (int i=0; i < nodeFeatureNum; i++)
                xNode[n][i] = new Variable(3);
            for (int i=0; i < edgeFeatureNum; i++)
                xEdge[n][i] = new Variable(2);
            y[n] = new Variable(3);


            ftItem.setNodeFeatureVarSet(xNode[n], y[n]);
            // add node feature of factor
            for(int i=0; i<this.nodeFeatureNum; i++) {
                int x = thread.nodeFeatures[i].x[n];


                // set node feature factor
                ftItem.nodeFeatureFactor[i] = new TableFactor(
                        ftItem.nodeFeatureVarSet[i],
                        ftItem.nodeFeatureProb[i]
                );
                this.nodeFactors[n][i] = ftItem.nodeFeatureFactor[i];
                mdl.addFactor(this.nodeFactors[n][i]);

                double[] singlePtl = new double[xNode[n][i].getNumOutcomes()];
                singlePtl[thread.nodeFeatures[i].x[n]] = 1.0;
                Factor single = new TableFactor(xNode[n][i], singlePtl);
                mdl.addFactor(single);
            }

            if(n == 0) continue;
            ftItem.setEdgeFeatureVarSet(xEdge[n], y[n-1], y[n]);
            // add edge feature of factor
            for(int i=0; i<this.edgeFeatureNum; i++) {
                // set the variables to the edgeFeatureVarSet
                ftItem.edgeFeatureFactor[i] = new TableFactor(
                        ftItem.edgeFeatureVarSet[i],
                        ftItem.edgeFeatureProb[i]
                );
                this.edgeFactors[n][i] = ftItem.edgeFeatureFactor[i];
                mdl.addFactor(this.edgeFactors[n][i]);

                double[] singlePtl = new double[xEdge[n][i].getNumOutcomes()];
                singlePtl[thread.edgeFeatures[i].x[n-1]] = 1.0;
                Factor single = new TableFactor(xEdge[n][i], singlePtl);
                mdl.addFactor(single);
            }
        }
    }
    public void inference() {

        Inferencer inf = new JunctionTreeInferencer();
        inf.computeMarginals(mdl);
        // int nodeFeatureNum = thread.nodeFeatureNum;
        // int edgeFeatureNum = thread.edgeFeatureNum;
        // int nodeSize = thread.nodes.size();
        for (Variable var: y) {
            Factor ptl = inf.lookupMarginal(var);
            for (AssignmentIterator it = ptl.assignmentIterator (); it.hasNext (); it.next()) {
                int outcome = it.indexOfCurrentAssn ();
                System.out.println (var+"  "+outcome+"   "+ptl.value (it));
            }
        }
    }

    public static void main(String[] args) {
        // factor table init
        System.out.println("========Reading data========");
        DataReader dataReader = new DataReader();
        Thread[] threads = dataReader.readData("data/Interstellar"); // foobar

        DataSet dataset = new DataSet(threads);
        // System.out.println(dataset.getThreadNum());
        //
        int nodeFeatureNum = Constant.nodeFeatureNames.length;
        int edgeFeatureNum = Constant.edgeFeatureNames.length;
        // factor table init
        FactorTable factorTable = new FactorTable(nodeFeatureNum, edgeFeatureNum);
        factorTable.Stats(threads, nodeFeatureNum, edgeFeatureNum);

        // factor graph
        // TODO: Test data: regard thread[0] as testdata
        BayesianNetwork bn = new BayesianNetwork(factorTable,  threads[0]);
        bn.inference();
    }


}
