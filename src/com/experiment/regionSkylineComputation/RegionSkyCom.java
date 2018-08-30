package com.experiment.regionSkylineComputation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.experiment.util.CloseUtil;
import com.experiment.util.PointComparator;


public class RegionSkyCom {
	int h;  //切分网格参数
	int dimension; //处理的数据的维度、
	
	public RegionSkyCom() {
		
	}
	
	public RegionSkyCom(int dimension, int h) {
		this.dimension = dimension;
		this.h = h;
	}
	
	
	public List<Point> encoder(String url) {
		BufferedReader reader=null;
		String temp=null; //接收读取的数据
		int location;//标记点坐标值所属切分坐标轴的位置
		
		List<Point> pointList = new ArrayList<Point>();
		
		File file=new File(url);
		try{
				reader=new BufferedReader(new FileReader(file));
				while((temp=reader.readLine())!=null){
					String[] coordinate = new String[dimension];
					System.arraycopy(temp.split("\t"), 1, coordinate, 0, dimension);//将获取到的坐标存在数组中
					String[] coordinate_code = new String[dimension];
					for(int i=0; i < coordinate.length; i++) { //数组第一位存放行号，因此从第二位开始读取坐标值
						location = (int)Math.floor(Double.parseDouble(coordinate[i])*Math.pow(2, h));
						String biStr = Integer.toBinaryString(location);//进行二进制编码
						//当编码位数小于h时，要补全。
						int bit = h - biStr.length();
						if(biStr.length() < h) {
							for(int j=0; j < bit; j++) {
								biStr = "0" + biStr;
							}
						}
						coordinate_code[i] = biStr; //将坐标值编码存放到数组中
					}
					String Z_address = "";
					for(int k=0; k < coordinate_code[0].length(); k++) {
						for(int d=0; d < dimension; d++) {
							Z_address += coordinate_code[d].split("")[k];
						}
					}
					
					Point point = new Point();
					point.setLabel(Z_address);
					point.setCoordinate(coordinate);
					
					pointList.add(point);

				}
		}
		catch(Exception e){
			e.printStackTrace();
			CloseUtil.closeAll(reader);   //关闭io流
		}
		finally{
			CloseUtil.closeAll(reader);
		}
		
		Collections.sort(pointList,new PointComparator());
		
		return pointList;
	}
	
	/*
	 * 1.初始化生成h+1个nodeList，存放ZH树中每一层的树节点
	 * 2.依次读取pointList中的数据点，按照Z_address生成树节点，并存储到最底层的nodeList中（存放树的叶子节点）。
	 * 3.自底向上构成ZH树
	 */
	
	public Map<String,LinkedList<ZHtreeNode>> ZHtree(List<Point> pointList) {
		//1.利用HashMap生成h+1个nodeList
		Map<String,LinkedList<ZHtreeNode>> nodeListMap = new HashMap<String,LinkedList<ZHtreeNode>>();
		for(int i=0; i<=h; i++) {
			LinkedList<ZHtreeNode> nodeList = new LinkedList<ZHtreeNode>();
			String str = "nodeList" + i;
			nodeListMap.put(str, nodeList);
		}
		
		//2.依次访问数据点，构建ZH树的叶子节点，存在nodeListh列表中
		Object[] key = nodeListMap.keySet().toArray();
		LinkedList<ZHtreeNode> leafNodeListh = nodeListMap.get(key[0].toString());//获取ZH树的叶子节点
		Iterator<Point> iter = pointList.iterator();
		while(iter.hasNext()) {
			Point p = iter.next();
			if(leafNodeListh.isEmpty() || !p.getLabel().equals(leafNodeListh.getLast().getLabel()) ) {
				ZHtreeNode zhtLNode = new ZHtreeNode();
				zhtLNode.setChildrenList(new LinkedList<Object>());
				zhtLNode.setLabel(p.getLabel());
				double[] minpt = MinBoundary(p.getLabel());
				double[] maxpt = MaxBoundary(p.getLabel());
				zhtLNode.setMinpt(minpt);
				zhtLNode.setMaxpt(maxpt);
				zhtLNode.getChildrenList().add(p);
				leafNodeListh.add(zhtLNode);
			}else{
				leafNodeListh.getLast().getChildrenList().add(p);
			}
		}
		
		/**
		 * 3.根据最底层的叶子节点，构建ZH树
		 */
		
		int i = 1;
		while(i <= h) {
			LinkedList<ZHtreeNode>  midNodeList = nodeListMap.get(key[i].toString());
			//if(i >= 2) {
				LinkedList<ZHtreeNode> nodeList = nodeListMap.get(key[i-1].toString());
				for(int j=0; j<nodeList.size(); j++) {//依次读取nodeListh中的节点
					ZHtreeNode node = nodeList.get(j);
					String perfixLabel = getPrefix(node.getLabel());
					//若上一层树节点链表为空，或者上一层树节点链表的最后一个节点的标签与当前叶子节点标签的前缀不同时，要创建一个新的树节点
					if(midNodeList.isEmpty() || !perfixLabel.equals(midNodeList.getLast().getLabel())) {
						ZHtreeNode zhtNode = new ZHtreeNode();
						zhtNode.setChildrenList(new LinkedList<Object>());
						zhtNode.setLabel(perfixLabel);;
						zhtNode.getChildrenList().add(node);
						midNodeList.add(zhtNode);
					}else {
						midNodeList.getLast().getChildrenList().add(node);
						node.setFather(midNodeList.getLast());
					}
				}
			/*}else {
				LinkedList<ZHtreeNode> leafNodeList = nodeListMap.get(key[i-1].toString());
				for(int j=0; j<leafNodeList.size(); j++) {
					ZHtreeNode leafNode = leafNodeList.get(j);
					String perfixLabel = getPrefix(leafNode.getLabel());
					if(midNodeList.isEmpty() || !perfixLabel.equals(midNodeList.getLast().getLabel())) {
						ZHtreeNode zhtNode = new ZHtreeNode();
						zhtNode.setChildrenList(new LinkedList<Object>());
						zhtNode.setLabel(perfixLabel);
						zhtNode.getChildrenList().add(leafNode);
						midNodeList.add(zhtNode);
						leafNode.setFather(zhtNode);
					}else {
						midNodeList.getLast().getChildrenList().add(leafNode);
						leafNode.setFather(midNodeList.getLast());
					}
				}
			}*/
			i++;
		}
		
		//测试ZH树的构建
		/*Set<String> keys = nodeListMap.keySet();
		Iterator<String> ite = keys.iterator();
		while(ite.hasNext()) {
			LinkedList<ZHtreeNode> treeNodeList = nodeListMap.get(ite.next());
			for(int j=0; j<treeNodeList.size(); j++) {
				ZHtreeNode treeNode = treeNodeList.get(j); 
				System.out.print(treeNode.getLabel()+"\t");
			}
			System.out.println();
		}*/
		
		return nodeListMap;
	}
	
	/**
	 * 逐个比较叶子节点，将被支配的节点删除。
	 * @return
	 */
	public Map<String,LinkedList<ZHtreeNode>> Prunning(List<Point> pointList){
		Map<String,LinkedList<ZHtreeNode>> nodeListMap = ZHtree(pointList); //调用ZHtree方法，生成ZH树
		//遍历叶子节点，进行比较过滤
		Set<String> keys = nodeListMap.keySet();
		Iterator<String> ite = keys.iterator();
		LinkedList<ZHtreeNode> nodeList = nodeListMap.get(ite.next());
		for(int i=0; i<nodeList.size(); i++) {
			for(int j=0; j<nodeList.size();j++) {
				if(j!=i) {
					ZHtreeNode dominatedNode = regionDominate(nodeList.get(i), nodeList.get(j));
					if(null!=dominatedNode) {
						removeNode(nodeListMap,dominatedNode);
					}
				}
			}
		}
		
		//测试ZH树的修改
		/*System.out.println("*************测试ZH树的修改*******************");
		Set<String> keyset = nodeListMap.keySet();
		Iterator<String> iter = keyset.iterator();
		while(iter.hasNext()) {
			LinkedList<ZHtreeNode> treeNodeList = nodeListMap.get(iter.next());
			for(int j=0; j<treeNodeList.size(); j++) {
				ZHtreeNode treeNode = treeNodeList.get(j); 
				System.out.print(treeNode.getLabel()+"\t");
				System.out.print(Arrays.toString(treeNode.getMinpt())+"\t");
				System.out.println(Arrays.toString(treeNode.getMaxpt())+"\t");
			}
			System.out.println();
		}*/
		return nodeListMap;
	}
	
	/**
	 * 递归修剪ZH树
	 */
	public void removeNode(Map<String,LinkedList<ZHtreeNode>> nodeListMap,ZHtreeNode dominatedNode) {
		Iterator<String> ite = nodeListMap.keySet().iterator();
		LinkedList<ZHtreeNode> nodeList = nodeListMap.get(ite.next());
		dominatedNode.getFather().getChildrenList().remove(dominatedNode);
		nodeList.remove(dominatedNode);
		if(0==dominatedNode.getFather().getChildrenList().size()) {
			removeNode(nodeListMap,dominatedNode.getFather());
		}
	}
	
	/**
	 * 根据Z_address、h、dimension求网格的最小角点（minpt）
	 */
	
	public double[] MinBoundary(String label) {
		//存放网格的最小角点坐标
		double[] minpt = new double[dimension];
		
		//存放根据网格的标签label，求得的每一维度对应的二进制编码
		String[] coordinate_code = new String[dimension];
		
		String[] label_split;
		label_split = label.split("");
		
		//存储每一维度对应的二进制编码
		String str = "";
		for(int i=0; i<dimension; i++) {
			for(int j=0; j<h; j++) {
				str += label_split[i + j*dimension];
			}
			coordinate_code[i] = str;
			str = "";
		}
		
		//根据维度对应的二进制编码求得minpt的坐标
		for(int i=0; i<coordinate_code.length; i++) {
			minpt[i] = Integer.parseInt(coordinate_code[i], 2)*Math.pow(2, -h);
		}
		
		return minpt;
	}
	
	/**
	 * 根据Z_address、h、dimension求网格的最大角点（maxpt）
	 */
	public double[] MaxBoundary(String label) {
		//存放网格的最大角点坐标
		double[] maxpt = new double[dimension];
		
		//存放根据网格的标签label，求得的每一维度对应的二进制编码
		String[] coordinate_code = new String[dimension];
		
		String[] label_split;
		label_split = label.split("");
		
		String str = "";
		for(int i=0; i<dimension; i++) {
			for(int j=0; j<h; j++) {
				str += label_split[i + j*dimension];
			}
			coordinate_code[i] = str;
			str = "";
		}
		
		//根据维度对应的二进制编码求得maxpt的坐标
		for(int i=0; i<coordinate_code.length; i++) {
			maxpt[i] = (Integer.parseInt(coordinate_code[i], 2)+1)*Math.pow(2, -h);
		}
		
		return maxpt;
	}
	
	/**
	 * 求网格块标签的前缀
	 */
	public String getPrefix(String label) {
		if(dimension==label.length()) {
			return "0";
		}else {
			return label.substring(0, (h-1)*dimension);
		}
	}
	
	/**
	 * 判断leafNode1是否支配leafNode2，若支配则返回leafNode2，否则，返回null
	 * @param leafNode1 
	 * @param leafNode2 
	 * @return leafNode1最大角点支配leafNode2的最小角点，则返回leafNode2
	 */
	public ZHtreeNode regionDominate(ZHtreeNode leafNode1, ZHtreeNode leafNode2) {
		double[] tuple = new double[leafNode1.getMinpt().length];
		for(int i=0; i<tuple.length;i++) {
			tuple[i] = leafNode1.getMaxpt()[i] - leafNode2.getMinpt()[i];
		}
		
		int numofNeg = 0;//记录负数的个数
		int numofZero = 0;//记录零的个数
		for(double temp : tuple) {
			if(temp < 0) numofNeg++;
			if(temp == 0) numofZero++;
		}
		
		if(numofZero < tuple.length && numofNeg+numofZero == tuple.length || numofZero == tuple.length) return leafNode2;//leafNode1支配leafNode2
		return null;//leafNode1不支配leafNode2
	}
	
}
