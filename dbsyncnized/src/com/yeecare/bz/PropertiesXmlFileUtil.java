package com.yeecare.bz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import com.yeecare.bean.DataBaseConfigInfo;
import com.yeecare.util.Log;


public class PropertiesXmlFileUtil {

//	public static void main(String[] args) {
//		readXML("com/yeecare/bz/master_database.xml");
//
//	}

	public static void writeXML(String xmlName, DataBaseConfigInfo confing) {
		try {
			File file = new File(xmlName);
			if (!file.exists()) {
				file.createNewFile();
			}
			Properties properties = new Properties();
			properties.setProperty("url", confing.url);
			properties.setProperty("username", confing.username);
			properties.setProperty("password", confing.password);
			properties.setProperty("time", confing.time);


			FileOutputStream fileOut = new FileOutputStream(xmlName);
			properties.storeToXML(fileOut, confing.comment);
			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取xml 信息
	 */
	public static DataBaseConfigInfo readXML(String xmlName) {
		DataBaseConfigInfo configInfo = new DataBaseConfigInfo();;
		try {
//			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//			InputStream input = classLoader.getResourceAsStream(xmlName);
			File file = new File(xmlName);
			if (file.exists()) {
				FileInputStream input = new FileInputStream(file);
				Properties properties = new Properties();
				properties.loadFromXML(input);
				input.close();
				
				Enumeration enuKeys = properties.keys();
				while (enuKeys.hasMoreElements()) {

					String key = (String) enuKeys.nextElement();
					String value = properties.getProperty(key);
					
					Log.i("key =" + key + ",value = " + value);
					if (key.equals("comment")) {
						configInfo.comment = value;
					} else if (key.equals("url")) {
						configInfo.url = value;
					} else if (key.equals("username")) {
						configInfo.username = value;
					} else if (key.equals("password")) {
						configInfo.password = value;
					} else if (key.equals("time")) {
						configInfo.time = value;
					}

				}
			}else{
				configInfo.url = "";
				configInfo.username = "";
				configInfo.password = "";
				configInfo.time = "";
				writeXML(xmlName,configInfo);
			}
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return configInfo;
	}

}
