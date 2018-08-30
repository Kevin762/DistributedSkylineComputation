package com.experiment.communication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.experiment.regionSkylineComputation.ZHtreeNode;



/**
 * 服务器角色，用来监听连接
 * 1、监听连接，接收数据
 * 2、处理数据
 * 3、转发给其他客户端
 * @author Kevin762
 *
 */

public class Server {
	//存放客户端的连接管道
	private List<MyChannel> all = new ArrayList<MyChannel>();
	private List<LinkedList<ZHtreeNode>> grsList;
	private boolean locked=true;
		
	public static void main(String[] args) throws IOException {
		new Server().start();
	}
	
	//MyChannel是非静态类，无法在外部类的静态方法中执行
	public void start() throws IOException {
		//1、创建服务器，指定端口
		ServerSocket server = new ServerSocket(8888);
		//监听多个客户端连接，但是一个客户端必须等待上一个连接的客户端完成操作，才能进行操作
		//可以给每一个客户端创建一个线程，来解决串行的缺点
		while(true) {
			//2、接收客户端连接  阻塞式（没有接收到连接，不会往下执行）
			Socket client = server.accept();
			System.out.println("连接成功");
			MyChannel channel = new MyChannel(client);
			all.add(channel);
//			new Thread(channel).start();
			channel.run();
		}
	}
	
	/**
	 * 一个客户端一个管道
	 * @author Kevin762
	 *
	 */
	private class MyChannel implements Runnable{
		private ObjectInputStream ois;
		private ObjectOutputStream oos;
		private boolean isRunning=true;
		private LinkedList<ZHtreeNode> linkedList;
		
		public MyChannel(Socket client) {
			try {
				ois = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
				oos = new ObjectOutputStream(new BufferedOutputStream(client.getOutputStream()));
			} catch (IOException e) {
				//e.printStackTrace();
				CloseUtil.closeAll(ois,oos);
				isRunning=false;
				all.remove(this);
			}
		}
		
		/**
		 * 接收数据
		 * @throws ClassNotFoundException 
		 */
		private void receive() throws ClassNotFoundException {
			try {
				System.out.println("开始接收数据");
				Object obj = ois.readObject();
				if(obj instanceof LinkedList<?>) {
					linkedList = (LinkedList<ZHtreeNode>)obj;
				}
				System.out.println("数据已接收");
			} catch (IOException e) {
				//e.printStackTrace();
				CloseUtil.closeAll(ois);
				isRunning=false;
				all.remove(this);
			}
		}
		 
		/**
		 * 发送数据
		 * @throws ClassNotFoundException 
		 */
		private void send() throws ClassNotFoundException {
				receive();
				if(null!=linkedList) {
					try {
						oos.writeObject(linkedList);
						oos.flush();
					} catch (IOException e) {
						//e.printStackTrace();
						CloseUtil.closeAll(oos);
						isRunning=false;
						all.remove(this);
					}
				}
		}
		
		/**
		 * 将接收的数据发送给其他客户端
		 */
		private void sendOthers() {
			/*//遍历容器获取其他客户端的管道
			for(MyChannel others:all) {
				if(others ==this) {
					continue;
				}
				others.send(msg);
			}*/
		}
		/**
		 * 将接收到的数据发送给本客户端
		 * @throws ClassNotFoundException 
		 */
		private void ownSelf() throws ClassNotFoundException {
			this.send();
		}
		
		@Override
		public void run() {
//			while(true) {
//				sendOthers();
				try {
					ownSelf();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
//			}
		}
		
	}
}
