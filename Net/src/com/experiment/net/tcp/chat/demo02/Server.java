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
		//监听多个客户端连接，但是一个客户端必须等待上一个连接的客户端完成操作，才能进行操作
		//可以给每一个客户端创建一个线程，来解决串行的缺点
		while(true) {
			//2、接收客户端连接  阻塞式（没有接收到连接，不会往下执行）
			Socket client = server.accept();
			DataInputStream dis = new DataInputStream(client.getInputStream());
			DataOutputStream dos = new DataOutputStream(client.getOutputStream());
			//发送数据
			while(true) {
				//输入流
				String msg = dis.readUTF();
				System.out.println(msg);
				//输出流
				dos.writeUTF("服务器-->"+msg);
				dos.flush();
			}
		}
	}

}
