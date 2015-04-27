import cc.mallet.optimize.LimitedMemoryBFGS;
import cc.mallet.optimize.Optimizable.ByGradientValue;
import cc.mallet.optimize.Optimizer;


public class OptimizeCRF implements ByGradientValue {

	double[] params;
	
	public OptimizeCRF(double[] init_params, int length) {
		params = new double[length];
		for (int i = 0; i < length; i++)
			params[i] = init_params[i];
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

	@Override
	public double getValue() {
		double x = params[0];
		double y = params[1];
		return -3*x*x - 4*y*y + 2*x - 4*y + 18;
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
