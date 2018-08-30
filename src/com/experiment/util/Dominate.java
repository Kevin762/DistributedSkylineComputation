package com.experiment.util;

/**
 * �ж�����Ԫ��֧���ϵ��
 * 
 */

public class Dominate {
	/**
	 * ֧�䣺1��t1Ԫ����������ֵ��С�ڵ���t2��Ӧ������ֵ
	 *     2��t1����һ������ֵС��t2��Ӧ������ֵ
	 *     ��t1֧��t2
	 * @param t1 t1֧��t2��t1��t2����ֵ֮�����0�ĸ�������С��0�ĸ���������Ԫ��ĳ���
	 * @param t2 t2֧��t1��t1��t2����ֵ֮�����0�ĸ������ϴ���0�ĸ���������Ԫ��ĳ���
	 * @return t1��t2����֧�䣬����null
	 */
	public double[] dominate(double[] t1, double[] t2) {
		double[] tuple = new double[t1.length];
		for(int i=0; i<tuple.length;i++) {
			tuple[i] = t1[i] - t2[i];
		}
		
		int numofNeg = 0;//��¼�����ĸ���
		int numofPos = 0;//��¼�����ĸ���
		int numofZero = 0;//��¼��ĸ���
		for(double temp : tuple) {
			if(temp < 0) numofNeg++;
			if(temp == 0) numofZero++;
			if(temp > 0) numofPos++;
		}
		
		if(numofZero < tuple.length && numofNeg+numofZero == tuple.length) return t1;
		if(numofZero < tuple.length && numofPos+numofZero == tuple.length) return t2;
		return null;
	}
	
}
