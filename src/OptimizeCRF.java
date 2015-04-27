import cc.mallet.optimize.LimitedMemoryBFGS;
import cc.mallet.optimize.Optimizable.ByGradientValue;
import cc.mallet.optimize.Optimizer;
import Microblog.Feature;
import Microblog.Thread;


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
			int pf = 0;
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

	@Override
	public void getValueGradient(double[] gradient) {
		gradient[0] = -6*params[0] + 2;
		gradient[1] = -8*params[1] - 4;
	}
	
	public static void main(String[] args) {
		OptimizeCRF crf = new OptimizeCRF(0, 0);
		Optimizer opt = new LimitedMemoryBFGS(crf);
		
		boolean converged = false;
		
		try {
			converged = opt.optimize();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		System.out.println(crf.getParameter(0) + ", " + crf.getParameter(1));
	}

}
