package com.experiment.util;

import java.io.Closeable;

/*
 * 关闭io流的工具
 */
public class CloseUtil {
	public static void closeAll(Closeable... io){
		for(Closeable temp:io){
			try {
				if (null != temp) {
					temp.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
