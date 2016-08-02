package com.yeecare.util;

public final class Log {
	
	private static boolean DEBUG = false;
	
	public static void i(String msg){
		if (DEBUG) {
			System.out.println(msg);
		}
	}

}
