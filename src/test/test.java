package test;

import java.util.List;


import com.experiment.regionSkylineComputation.Point;
import com.experiment.regionSkylineComputation.RegionSkyCom;

public class test {

	public static void main(String[] args) {
		RegionSkyCom regionSkyCom = new RegionSkyCom(3,2);
		String url = "D:\\Eclipse\\java-oxygen\\eclipse\\workspace\\Encoder\\AntidataD6S2N20.txt";
		List<Point> list = regionSkyCom.encoder(url);
//		regionSkyCom.ZHtree(list);
//		regionSkyCom.Prunning(list);
	}

}
