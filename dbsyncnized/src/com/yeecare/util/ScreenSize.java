package com.yeecare.util;

public class ScreenSize {

	public ScreenSize() {

	}

	public static int getScreenWidth() {
		return ((int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
	}

	public static int getScreenHeight() {
		return ((int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
	}

}
