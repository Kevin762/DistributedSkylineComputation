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
 * 1��������������ָ���˿�ServerSocket(int port)
 * 2�����տͻ�������
 * 3����������+��������
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
	//MyChannel�ǷǾ�̬�࣬�޷����ⲿ��ľ�̬������ִ��
	public void start() throws IOException {
		//1��������������ָ���˿�
		ServerSocket server = new ServerSocket(8888);
		//��������ͻ������ӣ�����һ���ͻ��˱���ȴ���һ�����ӵĿͻ�����ɲ��������ܽ��в���
		//���Ը�ÿһ���ͻ��˴���һ���̣߳���������е�ȱ��
		while(true) {
			//2�����տͻ�������  ����ʽ��û�н��յ����ӣ���������ִ�У�
			Socket client = server.accept();
			MyChannel channel = new MyChannel(client);
			all.add(channel);
			new Thread(channel).start();
		}
	}
	
	/**
	 * һ���ͻ���һ���ܵ�
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
		 * ��������
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
		 * ��������
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
		 * �����յ����ݷ��͸������ͻ���
		 */
		private void sendOthers() {
			String msg = receive();
			//����������ȡ�����ͻ��˵Ĺܵ�
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

