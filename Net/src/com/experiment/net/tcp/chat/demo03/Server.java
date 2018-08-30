package com.experiment.net.tcp.chat.demo03;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 1、创建服务器，指定端口ServerSocket(int port)
 * 2、接收客户端连接
 * 3、发送数据+接收数据
 * @author Kevin762
 *
 */
public class Server {
	private List<MyChannel> all = new ArrayList<MyChannel>();
	
	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
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
			MyChannel channel = new MyChannel(client);
			all.add(channel);
			new Thread(channel).start();
		}
	}
	
	/**
	 * 一个客户端一个管道
	 * @author Kevin762
	 *
	 */
	private class MyChannel implements Runnable{
		private DataInputStream dis;
		private DataOutputStream dos;
		private boolean isRunning=true;
		
		public MyChannel(Socket client) {
			try {
				dis = new DataInputStream(client.getInputStream());
				dos = new DataOutputStream(client.getOutputStream());
			} catch (IOException e) {
				//e.printStackTrace();
				CloseUtil.closeAll(dis,dos);
				isRunning=false;
				all.remove(this);
			}
		}
		
		/**
		 * 接收数据
		 */
		private String receive() {
			String msg = "";
			try {
				msg = dis.readUTF();
			} catch (IOException e) {
				//e.printStackTrace();
				CloseUtil.closeAll(dis);
				isRunning=false;
				all.remove(this);
			}
			return msg;
		}
		/**
		 * 发送数据
		 */
		private void send(String msg) {
			if(null!=msg && !msg.equals("")) {
				try {
					dos.writeUTF(msg);
					dos.flush();
				} catch (IOException e) {
					//e.printStackTrace();
					CloseUtil.closeAll(dos);
					isRunning=false;
					all.remove(this);
				}
			}
		}
		
		/**
		 * 将接收的数据发送给其他客户端
		 */
		private void sendOthers() {
			String msg = receive();
			//遍历容器获取其他客户端的管道
			for(MyChannel others:all) {
				if(others ==this) {
					continue;
				}
				others.send(msg);
			}
		}
		
		@Override
		public void run() {
			while(true) {
				sendOthers();
			}
		}
		
	}
}

