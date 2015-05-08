package Inference;

import cc.mallet.grmm.types.Factor;
import cc.mallet.grmm.types.FactorGraph;
import cc.mallet.grmm.types.HashVarSet;
import cc.mallet.grmm.types.TableFactor;
import cc.mallet.grmm.types.VarSet;
import cc.mallet.grmm.types.Variable;

public class FactorTable {


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
