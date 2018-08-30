package com.experiment.net.tcp.socket;

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
		//2�����տͻ�������  ����ʽ��û�н��յ����ӣ���������ִ�У�
		Socket client = server.accept();
		//3����������
		DataInputStream dis = new DataInputStream(client.getInputStream());
		byte[] bytes = new byte[100];
		dis.read(bytes);
		String str = new String(bytes);
//		String msg = dis.readUTF();
		System.out.println(str);
		//��������   �����
		DataOutputStream dos = new DataOutputStream(client.getOutputStream());
		dos.writeUTF("������-->"+str);
		dos.flush();
		
	}

}
