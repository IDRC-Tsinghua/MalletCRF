package Inference;

import cc.mallet.grmm.types.Factor;
import cc.mallet.grmm.types.FactorGraph;
import cc.mallet.grmm.types.HashVarSet;
import cc.mallet.grmm.types.TableFactor;
import cc.mallet.grmm.types.VarSet;
import cc.mallet.grmm.types.Variable;

public class FactorTable {

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

		double[] res = new double[] {1, 0, 0, 0, 1, 0, 0, 0, 1};
		Factor factor = new TableFactor(varSet, res);
		mdl.addFactor(factor);
	}
	
	public void ParentFeatureFactor(FactorGraph mdl) {
		
		/*    node_label   parent_label  y     p
		 *         0            0        1     1/3
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
				new Variable(3)	 // parent label	
		});

        double averageProb = 1 / 9;

		double[] res = new double[] {1, 0, 0, 0, 1, 0, 0, 0, 1};
		Factor factor = new TableFactor(varSet, res);
		mdl.addFactor(factor);
	}


}
