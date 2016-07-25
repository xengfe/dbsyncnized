package com.yeecare.util;

public final class Log {
	
	private static boolean DEBUG = true;
	
	public static void i(String msg){
		if (DEBUG) {
			System.out.println(msg);
		}
	}

}
