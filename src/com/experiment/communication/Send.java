package com.experiment.communication;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

import com.experiment.regionSkylineComputation.RegionSkyCom;
import com.experiment.regionSkylineComputation.ZHtreeNode;

import net.sf.json.JSONArray;

/**
 * 发送数据  线程
 * @author Kevin762
 *
 */
public class Send implements Runnable{
	//管道输出流
	private ObjectOutputStream oos;
	private boolean isRunning = true;
	private LinkedList<ZHtreeNode> linkedList;
	
	public Send(Socket client,LinkedList<ZHtreeNode> linkedList) {
		try {
			oos = new ObjectOutputStream(new BufferedOutputStream(client.getOutputStream()));
			this.linkedList = linkedList;
		} catch (IOException e) {
			//e.printStackTrace();
			isRunning = false;
			CloseUtil.closeAll(oos);
		}
	}
	
	public void send() {
		try {
			System.out.println("开始发送数据");
			oos.writeObject(linkedList);
			oos.flush();
			System.out.println("数据发送完毕");
		} catch (IOException e) {
//			e.printStackTrace();
			isRunning = false;
			CloseUtil.closeAll(oos);
		}
	}
	@Override
	public void run() {
//		while(isRunning) {
			send();
//		}
	}

}
