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
 * 1�������ͻ���  ����ָ��������+�˿�Socket(String host,int port)
 * 2�������վ�
 * @author Kevin762
 *
 */
public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException {
		//1�������ͻ���  ����ָ��������+�˿ڡ�
		Socket client = new Socket("localhost",8888);
		//2����������
		/*BufferedReader br = new BufferedReader(
				new InputStreamReader(client.getInputStream()));
		String echo = br.readLine();//���ж�ȡ������Ҫ���н�������
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
