package com.experiment.net.tcp.chat.demo01;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
		Socket socket = server.accept();
		System.out.println("一个客户端建立连接");
		//发送数据
		String msg = "欢迎使用";
		//输出流
		/*
		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(
						socket.getOutputStream()));
		bw.write(msg);
		bw.newLine();
		bw.flush();
		*/
		TraData traData = new TraData();
		traData.setLabel("01");
		traData.setAge(11);
		TraData traData1 = new TraData();
		traData1.setLabel("02");
		traData1.setAge(12);
		List<TraData> list = new ArrayList<TraData>();
		list.add(traData);
		list.add(traData1);
//		JSONObject jsonObj = JSONObject.fromObject(traData);
		JSONArray jsonArr = JSONArray.fromObject(list);
//		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		String str = jsonArr.toString();
		System.out.println(str);
		bw.write(str);
		bw.flush();
		bw.close();
		
	}
}
