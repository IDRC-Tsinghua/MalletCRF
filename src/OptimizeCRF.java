import Microblog.DataReader;
import Microblog.DataSet;
import Microblog.Feature;
import Microblog.Thread;
import cc.mallet.optimize.Optimizable.ByGradientValue;


public class OptimizeCRF implements ByGradientValue {

	double[] params;
	Thread[] threads;
	double[] logZ;
	
	public OptimizeCRF(double[] init_params, Thread[] dataset) {
		params = new double[init_params.length];
		for (int i = 0; i < init_params.length; i++)
			params[i] = init_params[i];
		threads = dataset;
		logZ = new double[dataset.length];
		for (int i = 0; i < dataset.length; i++)
			logZ[i] = getLogPartitionValue(dataset[i]);
	}
	
	@Override
	public int getNumParameters() {
		return params.length;
	}

	@Override
	public double getParameter(int i) {
		return params[i];
	}

	@Override
	public void getParameters(double[] container) {
		for (int i = 0; i < params.length; i++)
			container[i] = params[i];
	}

	@Override
	public void setParameter(int i, double new_param) {
		params[i] = new_param;
	}

	@Override
	public void setParameters(double[] new_params) {
		for (int i = 0; i < params.length; i++)
			params[i] = new_params[i];
	}

	double getLogPartitionValue(Thread thread) {
		// TODO: implement partition function
		return 0.0;
	}
	
	@Override
	public double getValue() {
		double logLH = 0.0;
		int pt = 0;
		for (Thread thread : threads) {
			double threadSum = 0.0;
			for (Feature nodeFeature : thread.nodeFeatures) {
				double featureSum = 0.0;
				for (double value : nodeFeature.values)
					featureSum += value;
				threadSum += params[pf] * featureSum;
				pf ++;
			}
			for (Feature edgeFeature : thread.edgeFeatures) {
				double featureSum = 0.0;
				for (double value: edgeFeature.values)
					featureSum += value;
				threadSum += params[pf] * featureSum;
				pf ++;
			}
			threadSum -= logZ[pt];
			pt ++;
			logLH += threadSum;
		}
		return logLH;
	}

	double[] getModelExpec(Thread thread, double[] params){
		double[] modelExpec = new double[params.length];
		// TODO: implement model expectation computation with factor graph and TRP.
		return modelExpec;
	}

	@Override
	public void getValueGradient(double[] gradient) {
		for (int i = 0; i < gradient.length; i++) 
			gradient[i] = 0.0;
		for (Thread thread : threads) {
			double[] modelExpec = getModelExpec(thread, params);
			for (int i = 0; i < gradient.length; i++) {
				int n = thread.nodeFeatures.length;
				if (i < n) {
					for (double value : thread.nodeFeatures[i].values)
						gradient[i] += value;
				} else {
					for (double value : thread.edgeFeatures[i-n].values)
						gradient[i] += value;
				}
				gradient[i] -= modelExpec[i];
			}
		}
	}
	
	public static void main(String[] args) {
		DataReader dataReader = new DataReader();
		Thread[] threads = dataReader.readData("data/Interstellar");
		DataSet dataset = new DataSet(threads);
		System.out.println(dataset.getThreadNum());
		dataset.extractFeatures();
		double[] init_params = new double[dataset.featureNum];

		/*OptimizeCRF crf = new OptimizeCRF(init_params, dataset);
		Optimizer opt = new LimitedMemoryBFGS(crf);
		
		boolean converged = false;
		
		try {
			converged = opt.optimize();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		System.out.println(crf.getParameter(0) + ", " + crf.getParameter(1));*/
	}

}
