package com.experiment.net.tcp.chat.demo02;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 1、创建客户端  必须指定服务器+端口Socket(String host,int port)
 * 2、接收收据
 * @author Kevin762
 *
 */
public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException {
		//1、创建客户端  必须指定服务器+端口、
		Socket client = new Socket("localhost",8888);
		//控制台输入流
		new Thread(new Send(client)).start();
		new Thread(new Receive(client)).start();
	}

}
