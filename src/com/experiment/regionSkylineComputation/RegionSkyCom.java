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
	int h;  //�з��������
	int dimension; //��������ݵ�ά�ȡ�
	
	public RegionSkyCom() {
		
	}
	
	public RegionSkyCom(int dimension, int h) {
		this.dimension = dimension;
		this.h = h;
	}
	
	
	public List<Point> encoder(String url) {
		BufferedReader reader=null;
		String temp=null; //���ն�ȡ������
		int location;//��ǵ�����ֵ�����з��������λ��
		
		List<Point> pointList = new ArrayList<Point>();
		
		File file=new File(url);
		try{
				reader=new BufferedReader(new FileReader(file));
				while((temp=reader.readLine())!=null){
					String[] coordinate = new String[dimension];
					System.arraycopy(temp.split("\t"), 1, coordinate, 0, dimension);//����ȡ�����������������
					String[] coordinate_code = new String[dimension];
					for(int i=0; i < coordinate.length; i++) { //�����һλ����кţ���˴ӵڶ�λ��ʼ��ȡ����ֵ
						location = (int)Math.floor(Double.parseDouble(coordinate[i])*Math.pow(2, h));
						String biStr = Integer.toBinaryString(location);//���ж����Ʊ���
						//������λ��С��hʱ��Ҫ��ȫ��
						int bit = h - biStr.length();
						if(biStr.length() < h) {
							for(int j=0; j < bit; j++) {
								biStr = "0" + biStr;
							}
						}
						coordinate_code[i] = biStr; //������ֵ�����ŵ�������
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
			CloseUtil.closeAll(reader);   //�ر�io��
		}
		finally{
			CloseUtil.closeAll(reader);
		}
		
		Collections.sort(pointList,new PointComparator());
		
		return pointList;
	}
	
	/*
	 * 1.��ʼ������h+1��nodeList�����ZH����ÿһ������ڵ�
	 * 2.���ζ�ȡpointList�е����ݵ㣬����Z_address�������ڵ㣬���洢����ײ��nodeList�У��������Ҷ�ӽڵ㣩��
	 * 3.�Ե����Ϲ���ZH��
	 */
	
	public Map<String,LinkedList<ZHtreeNode>> ZHtree(List<Point> pointList) {
		//1.����HashMap����h+1��nodeList
		Map<String,LinkedList<ZHtreeNode>> nodeListMap = new HashMap<String,LinkedList<ZHtreeNode>>();
		for(int i=0; i<=h; i++) {
			LinkedList<ZHtreeNode> nodeList = new LinkedList<ZHtreeNode>();
			String str = "nodeList" + i;
			nodeListMap.put(str, nodeList);
		}
		
		//2.���η������ݵ㣬����ZH����Ҷ�ӽڵ㣬����nodeListh�б���
		Object[] key = nodeListMap.keySet().toArray();
		LinkedList<ZHtreeNode> leafNodeListh = nodeListMap.get(key[0].toString());//��ȡZH����Ҷ�ӽڵ�
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
		 * 3.������ײ��Ҷ�ӽڵ㣬����ZH��
		 */
		
		int i = 1;
		while(i <= h) {
			LinkedList<ZHtreeNode>  midNodeList = nodeListMap.get(key[i].toString());
			//if(i >= 2) {
				LinkedList<ZHtreeNode> nodeList = nodeListMap.get(key[i-1].toString());
				for(int j=0; j<nodeList.size(); j++) {//���ζ�ȡnodeListh�еĽڵ�
					ZHtreeNode node = nodeList.get(j);
					String perfixLabel = getPrefix(node.getLabel());
					//����һ�����ڵ�����Ϊ�գ�������һ�����ڵ���������һ���ڵ�ı�ǩ�뵱ǰҶ�ӽڵ��ǩ��ǰ׺��ͬʱ��Ҫ����һ���µ����ڵ�
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
		
		//����ZH���Ĺ���
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
	 * ����Ƚ�Ҷ�ӽڵ㣬����֧��Ľڵ�ɾ����
	 * @return
	 */
	public Map<String,LinkedList<ZHtreeNode>> Prunning(List<Point> pointList){
		Map<String,LinkedList<ZHtreeNode>> nodeListMap = ZHtree(pointList); //����ZHtree����������ZH��
		//����Ҷ�ӽڵ㣬���бȽϹ���
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
		
		//����ZH�����޸�
		/*System.out.println("*************����ZH�����޸�*******************");
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
	 * �ݹ��޼�ZH��
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
	 * ����Z_address��h��dimension���������С�ǵ㣨minpt��
	 */
	
	public double[] MinBoundary(String label) {
		//����������С�ǵ�����
		double[] minpt = new double[dimension];
		
		//��Ÿ�������ı�ǩlabel����õ�ÿһά�ȶ�Ӧ�Ķ����Ʊ���
		String[] coordinate_code = new String[dimension];
		
		String[] label_split;
		label_split = label.split("");
		
		//�洢ÿһά�ȶ�Ӧ�Ķ����Ʊ���
		String str = "";
		for(int i=0; i<dimension; i++) {
			for(int j=0; j<h; j++) {
				str += label_split[i + j*dimension];
			}
			coordinate_code[i] = str;
			str = "";
		}
		
		//����ά�ȶ�Ӧ�Ķ����Ʊ������minpt������
		for(int i=0; i<coordinate_code.length; i++) {
			minpt[i] = Integer.parseInt(coordinate_code[i], 2)*Math.pow(2, -h);
		}
		
		return minpt;
	}
	
	/**
	 * ����Z_address��h��dimension����������ǵ㣨maxpt��
	 */
	public double[] MaxBoundary(String label) {
		//�����������ǵ�����
		double[] maxpt = new double[dimension];
		
		//��Ÿ�������ı�ǩlabel����õ�ÿһά�ȶ�Ӧ�Ķ����Ʊ���
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
		
		//����ά�ȶ�Ӧ�Ķ����Ʊ������maxpt������
		for(int i=0; i<coordinate_code.length; i++) {
			maxpt[i] = (Integer.parseInt(coordinate_code[i], 2)+1)*Math.pow(2, -h);
		}
		
		return maxpt;
	}
	
	/**
	 * ��������ǩ��ǰ׺
	 */
	public String getPrefix(String label) {
		if(dimension==label.length()) {
			return "0";
		}else {
			return label.substring(0, (h-1)*dimension);
		}
	}
	
	/**
	 * �ж�leafNode1�Ƿ�֧��leafNode2����֧���򷵻�leafNode2�����򣬷���null
	 * @param leafNode1 
	 * @param leafNode2 
	 * @return leafNode1���ǵ�֧��leafNode2����С�ǵ㣬�򷵻�leafNode2
	 */
	public ZHtreeNode regionDominate(ZHtreeNode leafNode1, ZHtreeNode leafNode2) {
		double[] tuple = new double[leafNode1.getMinpt().length];
		for(int i=0; i<tuple.length;i++) {
			tuple[i] = leafNode1.getMaxpt()[i] - leafNode2.getMinpt()[i];
		}
		
		int numofNeg = 0;//��¼�����ĸ���
		int numofZero = 0;//��¼��ĸ���
		for(double temp : tuple) {
			if(temp < 0) numofNeg++;
			if(temp == 0) numofZero++;
		}
		
		if(numofZero < tuple.length && numofNeg+numofZero == tuple.length || numofZero == tuple.length) return leafNode2;//leafNode1֧��leafNode2
		return null;//leafNode1��֧��leafNode2
	}
	
}
