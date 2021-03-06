/* Copyright (C) 2003 Univ. of Massachusetts Amherst, Computer Science Dept.
   This file is part of "MALLET" (MAchine Learning for LanguagE Toolkit).
   http://www.cs.umass.edu/~mccallum/mallet
   This software is provided under the terms of the Common Public License,
   version 1.0, as published by http://www.opensource.org.  For further
   information, see the file `LICENSE' included with this distribution. */


import cc.mallet.grmm.inference.Inferencer;
import cc.mallet.grmm.inference.TRP;
import cc.mallet.grmm.types.*;

/**
 * Created: Aug 13, 2004
 *
 * @author <A HREF="mailto:casutton@cs.umass.edu>casutton@cs.umass.edu</A>
 * @version $Id: SimpleGraphExample.java,v 1.1 2007/10/22 21:38:02 mccallum Exp $
 */
public class SimpleGraphExample {

  public static void main (String[] args)
  {

    // STEP 1: Create the graph

    Variable[] allVars = {
        new Variable(3),
        new Variable(3),
      new Variable (2),
        new Variable(3),
        new Variable(3)
    };

    FactorGraph mdl = new FactorGraph();

    // Create a diamond graph, with random potentials
    /*Random r = new Random (42);
    for (int i = 0; i < allVars.length; i++) {
      double[] ptlarr = new double [4];
      for (int j = 0; j < ptlarr.length; j++)
        ptlarr[j] = Math.abs (r.nextDouble ());

      Variable v1 = allVars[i];
      Variable v2 = allVars[(i + 1) % allVars.length];
      mdl.addFactor (v1, v2, ptlarr);
    }*/
    Factor single = new TableFactor(allVars[0], new double[]{0.0, 1.0, 0.0});
    mdl.addFactor(single);
    single = new TableFactor(allVars[1], new double[]{0.0, 1.0, 0.0});
    mdl.addFactor(single);
    single = new TableFactor(allVars[2], new double[]{1.0, 0.0});
    mdl.addFactor(single);
    Factor pair = new TableFactor(new Variable[]{allVars[0], allVars[3]},
        new double[]{1.0, 0.001, 0.001, 0.001, 1.0, 0.001, 0.001, 0.001, 1.0});
    mdl.addFactor(pair);
    pair = new TableFactor(new Variable[]{allVars[1], allVars[4]},
        new double[]{1.0, 0.001, 0.001, 0.001, 1.0, 0.001, 0.001, 0.001, 1.0});
    mdl.addFactor(pair);
    Factor triple = new TableFactor(new Variable[]{allVars[2], allVars[3], allVars[4]},
        new double[]{0.001, 0.001, 0.001, 0.001, 0.001, 0.001, 0.001, 0.001, 0.001, 1.0, 0.001, 0.001, 0.001, 1.0, 0.001, 0.001, 0.001, 1.0});
    mdl.addFactor(triple);

    // STEP 2: Compute marginals

    //Inferencer inf = new JunctionTreeInferencer ();
    Inferencer inf = new TRP ();
    inf.computeMarginals (mdl);

    // STEP 3: Collect the results
    //   We'll just print them out

    for (int varnum = 0; varnum < allVars.length; varnum++) {
      Variable var = allVars[varnum];
      Factor ptl = inf.lookupMarginal (var);
      System.out.println(ptl.value(new Assignment(var, 0)));
      System.out.println(var.getNumOutcomes());
      for (AssignmentIterator it = ptl.assignmentIterator (); it.hasNext (); it.next()) {
        int outcome = it.indexOfCurrentAssn ();
        System.out.println (var+"  "+outcome+"   "+ptl.value (it));
      }
      System.out.println ();
    }
    pair = inf.lookupMarginal(new HashVarSet(new Variable[]{allVars[2], allVars[3], allVars[4]}));
    for (AssignmentIterator it = pair.assignmentIterator(); it.hasNext(); it.next()) {
      System.out.println(it.assignment().get(allVars[2]) + " " + it.assignment().get(allVars[3])
          + " " + it.assignment().get(allVars[4]) + " " + pair.value(it));
    }

  }

}
