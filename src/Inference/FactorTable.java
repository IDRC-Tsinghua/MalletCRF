package Inference;

import Microblog.*;
import Microblog.Thread;
import cc.mallet.grmm.types.Factor;
import cc.mallet.grmm.types.FactorGraph;
import cc.mallet.grmm.types.HashVarSet;
import cc.mallet.grmm.types.TableFactor;
import cc.mallet.grmm.types.VarSet;
import cc.mallet.grmm.types.Variable;
import Utils.Constant;

public class FactorTable {

    public Factor[] nodeFeatureFactor = new Factor[Constant.nodeFeatureNames.length];
    public Factor[] edgeFeatureFactor = new Factor[Constant.edgeFeatureNames.length];

    public double[][] nodeFeatureCount = new double[Constant.nodeFeatureNames.length][3*3];
    public double[][] edgeFeatureCount = new double[Constant.edgeFeatureNames.length][3*3];
    public double[] nodeFeatureCountAll = new double[Constant.nodeFeatureNames.length];
    public double[] edgeFeatureCountAll = new double[Constant.nodeFeatureNames.length];

    public void Stats(Microblog.Thread[] threads,
                      int nodeFeatureNum, int edgeFeatureNum) {
        for (Thread thread: threads) {

            // init the category of feature
            thread.setNodeFeatures(Constant.nodeFeatureNames);
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
                    this.nodeFeatureCount[i][x*3 + label] += 1;
                    this.nodeFeatureCountAll[i] += 1;
                }
            }

            // foreach node on each edgefeature
            for(int i=0; i<edgeFeatureNum; i++) {
                for(int j=0; j<thread.nodes.size(); j++) {
                    int x = thread.edgeFeatures[i].x[j];
                    Node curNode = thread.nodes.get(j);
                    Node parentNode = thread.nodes.get(curNode.parent);
                    int curLabel = curNode.label;
                    int parentLabel = parentNode.label;
                    this.edgeFeatureCount[i][x*curLabel + parentLabel] += 1;
                    this.edgeFeatureCountAll[i] += 1;
                }
            }

            // normalize
            for(int i=0; i<nodeFeatureNum; i++) {
                for (int j=0; j<this.nodeFeatureCount[i].length; j++) {
                    this.nodeFeatureCount[i][j] /= this.nodeFeatureCountAll[i];
                }
                VarSet varSet = new HashVarSet(new Variable[]{
                        new Variable(3), // node label
                        new Variable(3)  // parent label
                });
                this.nodeFeatureFactor[i] = new TableFactor(varSet, this.nodeFeatureCount[i]);
            }
            for(int i=0; i<edgeFeatureNum; i++) {
                for (int j = 0; j < this.edgeFeatureCount[i].length; j++) {
                    this.edgeFeatureCount[i][j] /= this.edgeFeatureCountAll[i];
                }
                VarSet varSet = new HashVarSet(new Variable[]{
                        new Variable(2), // choice num
                        new Variable(3), // cur label
                        new Variable(3)  // parent label
                });
                this.edgeFeatureFactor[i] = new TableFactor(varSet, this.edgeFeatureCount[i]);
            }
        }

    }
}
