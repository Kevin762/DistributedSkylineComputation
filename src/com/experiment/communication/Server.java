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
 * ��������ɫ��������������
 * 1���������ӣ���������
 * 2����������
 * 3��ת���������ͻ���
 * @author Kevin762
 *
 */

public class Server {
	//��ſͻ��˵����ӹܵ�
	private List<MyChannel> all = new ArrayList<MyChannel>();
	private List<LinkedList<ZHtreeNode>> grsList;
	private boolean locked=true;
		
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
			System.out.println("���ӳɹ�");
			MyChannel channel = new MyChannel(client);
			all.add(channel);
//			new Thread(channel).start();
			channel.run();
		}
	}
	
	/**
	 * һ���ͻ���һ���ܵ�
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
		 * ��������
		 * @throws ClassNotFoundException 
		 */
		private void receive() throws ClassNotFoundException {
			try {
				System.out.println("��ʼ��������");
				Object obj = ois.readObject();
				if(obj instanceof LinkedList<?>) {
					linkedList = (LinkedList<ZHtreeNode>)obj;
				}
				System.out.println("�����ѽ���");
			} catch (IOException e) {
				//e.printStackTrace();
				CloseUtil.closeAll(ois);
				isRunning=false;
				all.remove(this);
			}
		}
		 
		/**
		 * ��������
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
		 * �����յ����ݷ��͸������ͻ���
		 */
		private void sendOthers() {
			/*//����������ȡ�����ͻ��˵Ĺܵ�
			for(MyChannel others:all) {
				if(others ==this) {
					continue;
				}
				others.send(msg);
			}*/
		}
		/**
		 * �����յ������ݷ��͸����ͻ���
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
