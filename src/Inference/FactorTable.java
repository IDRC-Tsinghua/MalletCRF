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


    /**
     *
     * All the factor table we use
     */
    /*
    public Factor nodeFeatureFactor;
    public Factor nodeEmojiFeatureFactor;
    public Factor authorRefFeatureFactor;
    public Factor differenceFeatureFactor;
    public Factor followRootFeatureFactor;
    public Factor hashTagFeatureFactor;
    public Factor sameAuthorFeatureFactor;
    public Factor sameEmojiFeatureFactor;
    public Factor sentimentPropFeatureFactor;
    public Factor similarityFeatureFactor;
    */


    /**
     *
     *  All the frequency we use for statistics
     */
    /*
    public double[] nodeFeatureCount;
    public double[] nodeEmojiFeatureCount;
    public double[] authorRefFeatureCount;
    public double[] differenceFeatureCount;
    public double[] followRootFeatureCount;
    public double[] hashTagFeatureCount;
    public double[] sameAuthorFeatureCount;
    public double[] sameEmojiFeatureCount;
    public double[] sentimentPropFeatureCount;
    public double[] similarityFeatureCount;
    */


    public Factor[] nodeFeatureFactor;
    public Factor[] edgeFeatureFactor;

    public double[][] nodeFeatureCount = new double[Constant.nodeFeatureNames.length][3*3];
    public double[][] edgeFeatureCount = new double[Constant.edgeFeatureNames.length][3*3];

    public void Stats(Microblog.Thread[] threads, double[] params,
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
                }

            }

        }

    }


    public void BasicFactor(FactorGraph mdl, double[] prob) {

        VarSet varSet = new HashVarSet(new Variable[]{
                new Variable(3), // node label
                new Variable(3)     // parent label
        });
        Factor factor = new TableFactor(varSet, prob);
        mdl.addFactor(factor);
    }

    public void NodeEmojiFactor(FactorGraph mdl) {

        double[] prob = new double[9];
        BasicFactor(mdl, prob);
    }

    public void SameAuthorFactor(FactorGraph mdl) {

    }

    public void SiblingFactor(FactorGraph mdl) {

    }

    public void SimilarityFactor(FactorGraph mdl) {


    }

    public void DifferenceFactor(FactorGraph mdl) {

    }

    public void SentimentPropFactor(FactorGraph mdl) {

    }

    public void AuthorRefFactor(FactorGraph mdl) {

    }

    public void HashtagFactor(FactorGraph mdl) {

    }

    public void SameEmojiFactor(FactorGraph mdl) {

    }

    public void FollowRootFactor(FactorGraph mdl) {

    }

    // ====================================================


    public void WordPolarityFactor(FactorGraph mdl) {

        /*
                  word          y                 p
                   0            0        0       1/3
		 *         0            1                 0
		 *         0            2                 0
		 *         1            0                 0
		 *         1            1        1       1/3
		 *         1            2                 0
		 *         2            0                 0
		 *         2            1                 0
		 *         2            2        2       1/3
         */
        VarSet varSet = new HashVarSet(new Variable[] {
                new Variable(3),
                new Variable(3)
        });
        double[] res = new double[] {1/3, 0, 0, 0, 1/3, 0, 0, 0, 1/3};
        Factor factor = new TableFactor(varSet, res);
        mdl.addFactor(factor);
    }



	public void RootFeatureFactor(FactorGraph mdl) {
		
		/*    node_label   root_label    y
		 *         0            0        1
		 *         0            1        0
		 *         0            2        0 
		 *         1            0        0
		 *         1            1        1
		 *         1            2        0
		 *         2            0        0
		 *         2            1        0
		 *         2            2        1
		 * 
		 */
		
		VarSet varSet = new HashVarSet(new Variable[] {
				new Variable(3), // node label
				new Variable(3)	 // root label	
		});

        double averageProb = 1 / 9;
        double[] res = new double[9];
        for (int i = 0; i < res.length; i++)
            res[i] = averageProb;
		Factor factor = new TableFactor(varSet, res);
		mdl.addFactor(factor);
	}
	
	public void ParentFeatureFactor(FactorGraph mdl) {
		
		/*    node_label   parent_label  y     p
		 *         0            0        1     1/9
		 *         0            1        0
		 *         0            2        0 
		 *         1            0        0
		 *         1            1        1
		 *         1            2        0
		 *         2            0        0
		 *         2            1        0
		 *         2            2        1
		 * 
		 */

        VarSet varSet = new HashVarSet(new Variable[]{
                new Variable(3), // node label
                new Variable(3)     // parent label
        });

        double averageProb = 1 / 9;

        double[] res = new double[9];
        for (int i = 0; i < res.length; i++)
            res[i] = averageProb;

        Factor factor = new TableFactor(varSet, res);
        mdl.addFactor(factor);
    }


}
