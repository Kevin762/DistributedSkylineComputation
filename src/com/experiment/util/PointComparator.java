package com.experiment.util;

import java.util.Comparator;

import com.experiment.regionSkylineComputation.Point;


public class PointComparator implements Comparator<Point> {
	//将字符串类型的label值转化为二进制的整数类型
	@Override
	public int compare(Point p1, Point p2) {
		return Integer.parseInt(p1.getLabel(), 2)-Integer.parseInt(p2.getLabel(), 2);
	}

}
