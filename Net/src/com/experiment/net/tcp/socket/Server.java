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
 * 1、创建服务器，指定端口ServerSocket(int port)
 * 2、接收客户端连接
 * 3、发送数据+接收数据
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
		//1、创建服务器，指定端口
		ServerSocket server = new ServerSocket(8888);
		//2、接收客户端连接  阻塞式（没有接收到连接，不会往下执行）
		Socket client = server.accept();
		//3、接收数据
		DataInputStream dis = new DataInputStream(client.getInputStream());
		byte[] bytes = new byte[100];
		dis.read(bytes);
		String str = new String(bytes);
//		String msg = dis.readUTF();
		System.out.println(str);
		//发送数据   输出流
		DataOutputStream dos = new DataOutputStream(client.getOutputStream());
		dos.writeUTF("服务器-->"+str);
		dos.flush();
		
	}

}
