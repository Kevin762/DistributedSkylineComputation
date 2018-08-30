package com.experiment.util;

/**
 * 判断两个元组支配关系：
 * 
 */

public class Dominate {
	/**
	 * 支配：1、t1元组所有属性值都小于等于t2对应的属性值
	 *     2、t1存在一个属性值小于t2对应的属性值
	 *     则t1支配t2
	 * @param t1 t1支配t2：t1与t2属性值之差，等于0的个数加上小于0的个数，等于元组的长度
	 * @param t2 t2支配t1：t1与t2属性值之差，等于0的个数加上大于0的个数，等于元组的长度
	 * @return t1与t2互不支配，返回null
	 */
	public double[] dominate(double[] t1, double[] t2) {
		double[] tuple = new double[t1.length];
		for(int i=0; i<tuple.length;i++) {
			tuple[i] = t1[i] - t2[i];
		}
		
		int numofNeg = 0;//记录负数的个数
		int numofPos = 0;//记录整数的个数
		int numofZero = 0;//记录零的个数
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
