package com.experiment.net.tcp.chat.demo03;

import java.io.Closeable;
import java.io.IOException;

/**
 * �ر����ķ���
 * @author Kevin762
 *
 */
public class CloseUtil {
	
	public static void closeAll(Closeable... io) {
		for(Closeable temp:io) {
			if(temp!=null) {
				try {
					temp.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
