package Inference;

import Microblog.Node;
import Microblog.Thread;
import Utils.Constant;
import cc.mallet.grmm.types.*;
import com.sun.javafx.Utils;

public class FactorTable {

    public int nodeFeatureNum;
    public int edgeFeatureNum;

    // probability
    public double[][] nodeFeatureProb;
    public double[][] edgeFeatureProb;


    // variables in the factorTable
    public Variable[] nodeVariables;
    public Variable[] edgeVariables;
    public Variable yVariable;
    public Variable yParentVariable;

    //
    public VarSet[] nodeFeatureVarSet;
    public VarSet[] edgeFeatureVarSet;
  
    public FactorTable(int nodeFeatureNum, int edgeFeatureNum){

        // init
        this.nodeFeatureNum = nodeFeatureNum;
        this.edgeFeatureNum = edgeFeatureNum;
        this.nodeFeatureProb = new double[nodeFeatureNum][3*3];
        this.edgeFeatureProb = new double[edgeFeatureNum][2*3*3];

        this.nodeFeatureVarSet = new HashVarSet[nodeFeatureNum];
        this.edgeFeatureVarSet = new HashVarSet[edgeFeatureNum];

        // variable init
        this.nodeVariables = new Variable[nodeFeatureNum];
        this.edgeVariables = new Variable[edgeFeatureNum];
        for(int i=0; i<nodeVariables.length; i++)
            this.nodeVariables[i] = new Variable(3);
        for(int i=0; i<edgeVariables.length; i++)
            this.edgeVariables[i] = new Variable(2);
        this.yVariable = new Variable(3);
        this.yParentVariable = new Variable(3);

    }

    public FactorTable clone() {
        FactorTable obj = new FactorTable(this.nodeFeatureNum, this.edgeFeatureNum);
        // obj.nodeFeatureFactor = this.nodeFeatureFactor;
        // obj.edgeFeatureFactor = this.edgeFeatureFactor;
        obj.nodeFeatureNum = this.nodeFeatureNum;
        obj.edgeFeatureNum = this.edgeFeatureNum;
        obj.nodeVariables = this.nodeVariables;
        obj.edgeVariables = this.edgeVariables;
        obj.yParentVariable = this.yParentVariable;
        obj.yVariable = this.yVariable;
        obj.nodeFeatureVarSet = this.nodeFeatureVarSet;
        obj.edgeFeatureVarSet = this.edgeFeatureVarSet;

        obj.nodeFeatureProb = this.nodeFeatureProb;
        obj.edgeFeatureProb = this.edgeFeatureProb;

        return obj;
    }

    public void setNodeFeatureVarSet(Variable[] xNode, Variable y) {

        for(int i=0; i<this.nodeFeatureNum; i++) {

            this.nodeFeatureVarSet[i] = new HashVarSet(new Variable[]{
                    xNode[i],
                    y
            });
        }
    }

    public void setEdgeFeatureVarSet(Variable[] xEdge, Variable yParent, Variable y) {

        for(int i=0; i<this.edgeFeatureNum; i++) {
            this.edgeFeatureVarSet[i] = new HashVarSet(new Variable[]{
                    xEdge[i],
                    yParent,
                    y

            });
        }
    }

    public void Stats(Microblog.Thread[] threads,
                      int nodeFeatureNum, int edgeFeatureNum) {

        // compute var
        double nodeFeatureCnt[][] = new double[nodeFeatureNum][3*3];
        double edgeFeatureCnt[][] = new double[edgeFeatureNum][2*3*3];
        double nodeFeatureCountAll[] = new double[nodeFeatureNum];
        double edgeFeatureCountAll[] = new double[edgeFeatureNum];

        for (Thread thread: threads) {

            // init the category of feature
            thread.setNodeFeatures(Constant.nodeFeatureNames, nodeFeatureNum);
            thread.setEdgeFeatures(Constant.edgeFeatureNames);


            // System.out.println(thread.nodes.size());
            // extract all the featrue
            thread.extractNodeFeatures();
            thread.extractEdgeFeatures();

            // after extract, get all the x-value in these features
            for(int i=0; i<nodeFeatureNum; i++) {

                // foreach node on each nodefeature
                for(int j=0; j<thread.nodes.size(); j++) {

                    // x is the similarity measure of node
                    // label is just the y

                    // System.out.println(i);
                    // System.out.println(j);
                    int x = thread.nodeFeatures[i].x[j];
                    // System.out.println("node x:" + x); // test
                    int label = thread.nodes.get(j).label;

                    nodeFeatureCnt[i][x*3 + label] += 1;
                    nodeFeatureCountAll[i] += 1;
                    // this.nodeFeatureProb[i][x*3 + label] += 1;
                    // this.nodeFeatureCountAll[i] += 1;
                }
            }

            // foreach node on each edgefeature
            for(int i=0; i<edgeFeatureNum; i++) {
                for(int j=0; j<thread.nodes.size(); j++) {
                    if(j == 0) continue;
                    int x = thread.edgeFeatures[i].x[j-1]; // the length of x is node.size()-1
                    // System.out.println("edge x:" + x); // test

                    Node curNode = thread.nodes.get(j);
                    Node parentNode = thread.nodes.get(curNode.parent);
                    int curLabel = curNode.label;
                    int parentLabel = parentNode.label;
                    edgeFeatureCnt[i][x * (3*3) + parentLabel * 3 + curLabel] += 1;
                    edgeFeatureCountAll[i] += 1;
                    // this.edgeFeatureProb[i][x*2 + (parentLabel*3) + curLabel] += 1;
                    // this.edgeFeatureCountAll[i] += 1;
                }
            }

            this.setNodeFeatureVarSet(nodeVariables, yVariable);
            this.setEdgeFeatureVarSet(edgeVariables, yParentVariable, yVariable);

            // normalize and given result
            for(int i=0; i<nodeFeatureNum; i++) {
                for (int j=0; j<this.nodeFeatureProb[i].length; j++) {
                    this.nodeFeatureProb[i][j] = nodeFeatureCnt[i][j] / nodeFeatureCountAll[i];
                }



                //this.nodeFeatureFactor[i] = new TableFactor(nodeFeatureVarSet[i], this.nodeFeatureProb[i]);
            }
            for(int i=0; i<edgeFeatureNum; i++) {
                for (int j = 0; j < this.edgeFeatureProb[i].length; j++) {
                    this.edgeFeatureProb[i][j] = edgeFeatureCnt[i][j] / edgeFeatureCountAll[i];
                }

                // this.edgeFeatureFactor[i] = new TableFactor(edgeFeatureVarSet[i], this.edgeFeatureProb[i]);
            }

            // check the probability
            for (int i=0; i<nodeFeatureNum; i++)
                for (int j=0; j<this.nodeFeatureProb.length; j++)
                    if (this.nodeFeatureProb[i][j] == 0)
                        this.nodeFeatureProb[i][j] = Constant.minPtl;

            for (int i=0; i<edgeFeatureNum; i++)
                for (int j=0; j<this.edgeFeatureProb.length; j++)
                    if (this.edgeFeatureProb[i][j] == 0)
                        this.edgeFeatureProb[i][j] = Constant.minPtl;
        }

    }
}
