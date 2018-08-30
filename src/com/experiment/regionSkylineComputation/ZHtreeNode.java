package com.experiment.regionSkylineComputation;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Serializable是一个空接口，只是一个标识，标识类可以被序列化
 * transient关键字标识某一属性不被序列化
 * @author Kevin762
 *
 */
public class ZHtreeNode implements Serializable {
	private String label;
	private transient ZHtreeNode father;
	private transient LinkedList<Object> childrenList;
	private double[] minpt;
	private double[] maxpt;
	
	
	
	public ZHtreeNode() {
		
	}
	
	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}

	public ZHtreeNode getFather() {
		return father;
	}


	public void setFather(ZHtreeNode father) {
		this.father = father;
	}


	public LinkedList<Object> getChildrenList() {
		return childrenList;
	}


	public void setChildrenList(LinkedList<Object> childrenList) {
		this.childrenList = childrenList;
	}

	public double[] getMinpt() {
		return minpt;
	}
	public void setMinpt(double[] minpt) {
		this.minpt = minpt;
	}
	public double[] getMaxpt() {
		return maxpt;
	}
	public void setMaxpt(double[] maxpt) {
		this.maxpt = maxpt;
	}
	
}
