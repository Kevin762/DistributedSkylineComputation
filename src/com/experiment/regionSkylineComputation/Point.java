package com.experiment.regionSkylineComputation;

public class Point {

	private String label; //���Z_address
	private String[] coordinate; //��ŵ������
	
	public Point() {
		
	}
	
	
	
	public Point(String label, String[] coordinate) {
		super();
		this.label = label;
		this.coordinate = coordinate;
	}



	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String[] getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(String[] coordinate) {
		this.coordinate = coordinate;
	}	
	
}
