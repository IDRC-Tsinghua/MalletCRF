package Inference;

import Microblog.Node;
import Microblog.Thread;
import Utils.Constant;
import cc.mallet.grmm.types.*;

public class FactorTable {

    public Factor[] nodeFeatureFactor;
    public Factor[] edgeFeatureFactor;

    public int nodeFeatureNum;
    public int edgeFeatureNum;

    // probability
    public double[][] nodeFeatureProb;
    public double[][] edgeFeatureProb;
    public double[] nodeFeatureCountAll;
    public double[] edgeFeatureCountAll;

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
        this.nodeFeatureFactor = new Factor[nodeFeatureNum];
        this.edgeFeatureFactor = new Factor[edgeFeatureNum];
        this.nodeFeatureNum = nodeFeatureNum;
        this.edgeFeatureNum = edgeFeatureNum;
        this.nodeFeatureProb = new double[nodeFeatureNum][3*3];
        this.edgeFeatureProb = new double[edgeFeatureNum][3*3];
        this.nodeFeatureCountAll = new double[nodeFeatureNum];
        this.edgeFeatureCountAll = new double[edgeFeatureNum];
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
        /**
        for(int i=0; i< Constant.nodeFeatureNames.length; i++) {

            nodeFeatureVarSet[i] = new HashVarSet(new Variable[]{
                   nodeVariables
            });
        }

        for(int i=0; i< Constant.edgeFeatureNames.length; i++) {
            edgeFeatureVarSet[i] = new HashVarSet(new Variable[]{

                    new Variable(2), // abstract value
            });
        }
         **/
    }

    public void setNodeFeatureVarSet(Variable[] xNode, Variable y) {

        for(int i=0; i<this.nodeFeatureNum; i++) {

            this.nodeFeatureVarSet[i] = new HashVarSet(new Variable[]{
                    xNode[i],
                    y
            });
        }
    }

    public void setEdgeFeatureVarSet(Variable[] xEdge, Variable y, Variable yParent) {

        for(int i=0; i<this.edgeFeatureNum; i++) {
            this.edgeFeatureVarSet[i] = new HashVarSet(new Variable[]{
                    xEdge[i],
                    y,
                    yParent
            });
        }
    }

    public void Stats(Microblog.Thread[] threads,
                      int nodeFeatureNum, int edgeFeatureNum) {
        for (Thread thread: threads) {

            // init the category of feature
            thread.setNodeFeatures(Constant.nodeFeatureNames, Constant.sentimentDictLength);
            thread.setEdgeFeatures(Constant.edgeFeatureNames);

            // extract all the featrue
            thread.extractNodeFeatures();
            thread.extractNodeFeatures();

            // after extract, get all the x-value in these features
            for(int i=0; i<nodeFeatureNum; i++) {

                // foreach node on each nodefeature
                for(int j=0; i<thread.nodes.size(); j++) {

                    // x is the similarity measure of node
                    // label is just the y
                    int x = thread.nodeFeatures[i].x[j];
                    int label = thread.nodes.get(j).label;
                    this.nodeFeatureProb[i][x*3 + label] += 1;
                    this.nodeFeatureCountAll[i] += 1;
                }
            }

            // foreach node on each edgefeature
            for(int i=0; i<edgeFeatureNum; i++) {
                for(int j=1; j<thread.nodes.size(); j++) {
                    int x = thread.edgeFeatures[i].x[j];
                    Node curNode = thread.nodes.get(j);
                    Node parentNode = thread.nodes.get(curNode.parent);
                    int curLabel = curNode.label;
                    int parentLabel = parentNode.label;
                    this.edgeFeatureProb[i][x*curLabel + parentLabel] += 1;
                    this.edgeFeatureCountAll[i] += 1;
                }
            }

            // normalize and given result
            for(int i=0; i<nodeFeatureNum; i++) {
                for (int j=0; j<this.nodeFeatureProb[i].length; j++) {
                    this.nodeFeatureProb[i][j] /= this.nodeFeatureCountAll[i];
                }

                this.setNodeFeatureVarSet(nodeVariables, yVariable);
                this.nodeFeatureFactor[i] = new TableFactor(nodeFeatureVarSet[i],
                        this.nodeFeatureProb[i]);
            }
            for(int i=0; i<edgeFeatureNum; i++) {
                for (int j = 0; j < this.edgeFeatureProb[i].length; j++) {
                    this.edgeFeatureProb[i][j] /= this.edgeFeatureCountAll[i];
                }

                this.setEdgeFeatureVarSet(edgeVariables, yVariable, yParentVariable);
                this.edgeFeatureFactor[i] = new TableFactor(edgeFeatureVarSet[i],
                        this.edgeFeatureProb[i]);
            }
        }

    }
}
