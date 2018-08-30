package com.experiment.net.tcp.chat.demo02;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 1��������������ָ���˿�ServerSocket(int port)
 * 2�����տͻ�������
 * 3����������+��������
 * @author Kevin762
 *
 */
public class Server {
	
	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		//1��������������ָ���˿�
		ServerSocket server = new ServerSocket(8888);
		//��������ͻ������ӣ�����һ���ͻ��˱���ȴ���һ�����ӵĿͻ�����ɲ��������ܽ��в���
		//���Ը�ÿһ���ͻ��˴���һ���̣߳���������е�ȱ��
		while(true) {
			//2�����տͻ�������  ����ʽ��û�н��յ����ӣ���������ִ�У�
			Socket client = server.accept();
			DataInputStream dis = new DataInputStream(client.getInputStream());
			DataOutputStream dos = new DataOutputStream(client.getOutputStream());
			//��������
			while(true) {
				//������
				String msg = dis.readUTF();
				System.out.println(msg);
				//�����
				dos.writeUTF("������-->"+msg);
				dos.flush();
			}
		}
	}

}
