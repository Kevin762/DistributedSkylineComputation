package com.experiment.net.tcp.chat.demo01;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

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
		//2、接收数据
		/*BufferedReader br = new BufferedReader(
				new InputStreamReader(client.getInputStream()));
		String echo = br.readLine();//按行读取，必须要有行结束符！
		System.out.println(echo);*/
		
		//DataInputStream dis = new DataInputStream(client.getInputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
//		String echo = dis.readUTF();
		String echo = br.readLine();
		br.close();
		JSONArray jsonArr = JSONArray.fromObject(echo);
		List<?> list = JSONArray.toList(jsonArr, new TraData(), new JsonConfig());
		TraData recData = (TraData)list.get(0);
		System.out.println("Label:"+recData.getLabel()+"\n"+"Age:"+recData.getAge());
	}

}
