package com.yeecare.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateStrUtl {

	public static String getFormatTimestamp(Timestamp timestamp) {
		SimpleDateFormat hm =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=hm.format(timestamp);
		return time;
	}
	
	public static String getFormatDate(Date date) {
		SimpleDateFormat hm =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=hm.format(date);
		return time;
	}
}
