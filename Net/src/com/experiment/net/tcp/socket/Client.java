package com.experiment.net.tcp.socket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * 1�������ͻ���  ����ָ��������+�˿�Socket(String host,int port)
 * 2����������
 * 3�������վ�
 * @author Kevin762
 *
 */
public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException {
		//1�������ͻ���  ����ָ��������+�˿ڡ�
		Socket client = new Socket("localhost",8888);
		//��������
		DataOutputStream dos = new DataOutputStream(client.getOutputStream());
		int[] arr = new int[] {1,2,3,4,5};
		String str = Arrays.toString(arr);
		dos.write(str.getBytes());
// 		dos.writeUTF("����");
		//2����������
		DataInputStream dis = new DataInputStream(client.getInputStream());
		String msg = dis.readUTF();
		System.out.println(msg);
	}

}
