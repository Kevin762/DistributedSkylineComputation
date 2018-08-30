package com.experiment.net.tcp.chat.demo02;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 1�������ͻ���  ����ָ��������+�˿�Socket(String host,int port)
 * 2�������վ�
 * @author Kevin762
 *
 */
public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException {
		//1�������ͻ���  ����ָ��������+�˿ڡ�
		Socket client = new Socket("localhost",8888);
		//����̨������
		new Thread(new Send(client)).start();
		new Thread(new Receive(client)).start();
	}

}
