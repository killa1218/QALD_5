package org.apex.util;

public class vec {
	public double[] wordvec=new double[200];
	public double getlen() {
		double ans=0.0;
		for (int i=0;i<200;i++) {
			ans+=wordvec[i]*wordvec[i];
		}
		return Math.sqrt(ans);
	}
	public static double getcos(vec a,vec b) {
		double ans=0.0;
		for (int i=0;i<200;i++) {
			ans+=a.wordvec[i]*b.wordvec[i];
		}
		return ans/(a.getlen()*b.getlen());
	}
}
