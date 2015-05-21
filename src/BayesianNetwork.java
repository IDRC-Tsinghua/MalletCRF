import Inference.FactorTable;
import Microblog.*;
import Microblog.Thread;
import Utils.Constant;
import Utils.Tuple;
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


        // init
        // xNode init
        for(int n=0; n<nodeCnt; n++)
            for(int i=0; i<nodeFeatureNum; i++)
                xNode[n][i] = new Variable(3);

        for(int n=0; n<nodeCnt; n++)
            for(int i=0; i<edgeFeatureNum; i++)
                xEdge[n][i] = new Variable(2);

        for(int n=0; n<nodeCnt; n++)
            y[n] = new Variable(3);

        for(int n=0; n<nodeCnt; n++) {

            // generate each factor to each node
            FactorTable ftItem = factorTable.clone();

            /*
            // set node and edge varset with local variables
            for (int i=0; i < nodeFeatureNum; i++)
                xNode[n][i] = new Variable(3);
            for (int i=0; i < edgeFeatureNum; i++)
                xEdge[n][i] = new Variable(2);
            y[n] = new Variable(3);
            */

            ftItem.setNodeFeatureVarSet(xNode[n], y[n]);
            // add node feature of factor
            for(int i=0; i<this.nodeFeatureNum; i++) {
                int x = thread.nodeFeatures[i].x[n];


                // set node feature factor
                /*
                ftItem.nodeFeatureFactor[i] = new TableFactor(
                        ftItem.nodeFeatureVarSet[i],
                        ftItem.nodeFeatureProb[i]
                );
                this.nodeFactors[n][i] = ftItem.nodeFeatureFactor[i];
                */
                this.nodeFactors[n][i] = new TableFactor(
                        ftItem.nodeFeatureVarSet[i],
                        ftItem.nodeFeatureProb[i]
                );
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
                /*
                ftItem.edgeFeatureFactor[i] = new TableFactor(
                        ftItem.edgeFeatureVarSet[i],
                        ftItem.edgeFeatureProb[i]
                );
                this.edgeFactors[n][i] = ftItem.edgeFeatureFactor[i];
                */
                this.edgeFactors[n][i] = new TableFactor(
                        ftItem.edgeFeatureVarSet[i],
                        ftItem.edgeFeatureProb[i]
                );
                mdl.addFactor(this.edgeFactors[n][i]);

                double[] singlePtl = new double[xEdge[n][i].getNumOutcomes()];
                singlePtl[thread.edgeFeatures[i].x[n-1]] = 1.0;
                Factor single = new TableFactor(xEdge[n][i], singlePtl);
                mdl.addFactor(single);
            }
        }
    }
    public Tuple<Double, Double> inference() {

        Inferencer inf = new JunctionTreeInferencer();
        inf.computeMarginals(mdl);
        // int nodeFeatureNum = thread.nodeFeatureNum;
        // int edgeFeatureNum = thread.edgeFeatureNum;
        // int nodeSize = thread.nodes.size();

        Double correctCnt = 0.0;
        Double Cnt = 0.0;

        for (int i=0; i< y.length; i++) {
            Variable var = y[i];
            int label = thread.nodes.get(i).label;
            int outcomeMax = 0;
            double ptlMax = 0;
            Factor ptl = inf.lookupMarginal(var);
            for (AssignmentIterator it = ptl.assignmentIterator (); it.hasNext (); it.next()) {
                int outcome = it.indexOfCurrentAssn ();
                // System.out.println (var+"  "+outcome+"   "+ptl.value (it));
                if (ptl.value(it) > ptlMax) {
                    outcomeMax = outcome;
                    ptlMax = ptl.value(it);
                }
            } // get max outcome
            if (label == outcomeMax)
                correctCnt += 1;
            Cnt += 1;
        }
        return new Tuple<Double, Double>(correctCnt, Cnt);
    }

    public static void main(String[] args) {
        // factor table init
        System.out.println("========Reading data========");
        DataReader dataReader = new DataReader();
        Thread[] threads1 = dataReader.readData("data/traindata/fold_0"); // foobar
        Thread[] threads2 = dataReader.readData("data/traindata/fold_1"); // foobar
        Thread[] threads3 = dataReader.readData("data/traindata/fold_2"); // foobar
        Thread[] threads4 = dataReader.readData("data/traindata/fold_3"); // foobar
        // NAIVE!!!
        Thread[] threads = Constant.concatentate(threads1, threads2);
        threads = Constant.concatentate(threads, threads3);
        threads = Constant.concatentate(threads, threads4);

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

        Double correctCnt = 0.;
        Double Cnt = 0.;

        double taskCnt = 0;
        double threadCnt = threads.length;
        double intervel = threadCnt / 100.0;
        System.out.println(intervel);
        for (Thread thread: threads) {

            taskCnt += 1;
            // print y
            for(int i = 0; i < thread.nodes.size(); i++) {

                Node node = thread.nodes.get(i);
                // System.out.println(node.label);
            }

            BayesianNetwork bn = new BayesianNetwork(factorTable,  thread);
            Tuple<Double, Double> tuple = bn.inference();
            correctCnt += tuple.x;
            Cnt += tuple.y;

            // System.out.println(taskCnt);
            // System.out.println(taskCnt);
            if (taskCnt % 13 == 0) {
                System.out.println(taskCnt/threadCnt * 100 + "%");
            }
            // System.out.println("===============");

        }
        double res = correctCnt / Cnt;
        System.out.println(res);

    }


}
