package Inference;

import Microblog.Node;
import Microblog.Thread;
import Utils.Constant;
import cc.mallet.grmm.types.*;

public class FactorTable {

    public Factor[] nodeFeatureFactor = new Factor[Constant.nodeFeatureNames.length];
    public Factor[] edgeFeatureFactor = new Factor[Constant.edgeFeatureNames.length];

    public double[][] nodeFeatureProb = new double[Constant.nodeFeatureNames.length][3*3];
    public double[][] edgeFeatureProb = new double[Constant.edgeFeatureNames.length][3*3];
    public double[] nodeFeatureCountAll = new double[Constant.nodeFeatureNames.length];
    public double[] edgeFeatureCountAll = new double[Constant.nodeFeatureNames.length];

    public VarSet[] nodeFeatureVarSet = new VarSet[Constant.nodeFeatureNames.length];
    public VarSet[] edgeFeatureVarSet = new VarSet[Constant.edgeFeatureNames.length];


    public FactorTable(){

        for(int i=0; i< Constant.nodeFeatureNames.length; i++) {

            nodeFeatureVarSet[i] = new HashVarSet(new Variable[]{
                    new Variable(3), // node label
                    new Variable(3)  // parent label
            });
        }

        for(int i=0; i< Constant.edgeFeatureNames.length; i++) {
            edgeFeatureVarSet[i] = new HashVarSet(new Variable[]{

                    new Variable(2), // choice num
                    new Variable(3), // cur label
                    new Variable(3)  // parent label
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

            // normalize
            for(int i=0; i<nodeFeatureNum; i++) {
                for (int j=0; j<this.nodeFeatureProb[i].length; j++) {
                    this.nodeFeatureProb[i][j] /= this.nodeFeatureCountAll[i];
                }

                this.nodeFeatureFactor[i] = new TableFactor(nodeFeatureVarSet[i], this.nodeFeatureProb[i]);
            }
            for(int i=0; i<edgeFeatureNum; i++) {
                for (int j = 0; j < this.edgeFeatureProb[i].length; j++) {
                    this.edgeFeatureProb[i][j] /= this.edgeFeatureCountAll[i];
                }

                this.edgeFeatureFactor[i] = new TableFactor(edgeFeatureVarSet[i], this.edgeFeatureProb[i]);
            }
        }

    }
}
