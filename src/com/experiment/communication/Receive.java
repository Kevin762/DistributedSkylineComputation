package com.experiment.communication;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;

import com.experiment.regionSkylineComputation.ZHtreeNode;

/**
 * 接收数据  线程
 * @author Kevin762
 *
 */
public class Receive implements Runnable{
	private ObjectInputStream ois;
	private boolean isRunning = true;
	private LinkedList<ZHtreeNode> linkedList;
	public Receive() {
		
	}
	public Receive(Socket client) {
		try {
			ois = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
		} catch (IOException e) {
			//e.printStackTrace();
			isRunning = false;
			CloseUtil.closeAll(ois);
		}
	}
	/**
	 * 接收数据
	 * @throws ClassNotFoundException 
	 */
	public void receive() throws ClassNotFoundException {
		try {
			Object obj = ois.readObject();
			if(obj instanceof LinkedList<?>) {
				linkedList = (LinkedList<ZHtreeNode>)obj;
			}
		} catch (IOException e) {
			//e.printStackTrace();
			isRunning = false;
			CloseUtil.closeAll(ois);
		}
	}
	
	@Override
	public void run() {
//		while(isRunning) {
			try {
				receive();
				System.out.println(linkedList.size());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
//		}
		
	}
	public LinkedList<ZHtreeNode> getLinkedList() {
		return linkedList;
	}
}
