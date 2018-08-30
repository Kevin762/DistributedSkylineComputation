package com.experiment.communication;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.experiment.regionSkylineComputation.Point;
import com.experiment.regionSkylineComputation.RegionSkyCom;
import com.experiment.regionSkylineComputation.ZHtreeNode;

/**
 * �ͻ��˽�ɫ����������
 * 1�����ӷ���������������
 * @author Kevin762
 *
 */
public class Client {
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		RegionSkyCom regionSkyCom = new RegionSkyCom(3,2);
		String url = "D:\\Eclipse\\java-oxygen\\eclipse\\workspace\\Encoder\\AntidataD7S1N10000.txt";
		List<Point> pointList = regionSkyCom.encoder(url);
		Map<String,LinkedList<ZHtreeNode>> nodeListMap = regionSkyCom.Prunning(pointList);
		String key = (String)nodeListMap.keySet().toArray()[0];
		LinkedList<ZHtreeNode> leafNodeList = nodeListMap.get(key);
		//1�������ͻ���  ����ָ��������+�˿ڡ�
		Socket client = new Socket("localhost",8888);
		new Thread(new Send(client,leafNodeList)).start();
		new Thread(new Receive(client)).start();
	}
}
