package com.yeecare.bz;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.yeecare.bean.Entity;
import com.yeecare.util.Base;
import com.yeecare.util.Log;


public class DBManager extends Base {
	private static List<String> sourceDeleteData = new ArrayList<String>();

	


	public static List<Entity> getNewBloodglucoseData(boolean first) {
		List<Entity> sourceData = readSourceDataBase(first);
		List<Entity> distData = readDistDataBase(first);
		compare(sourceData, distData);
		return sourceData;

	}

	/**
	 * @param sourceData
	 * @param distData
	 */
	public static void compare(List<Entity> sourceData, List<Entity> distData) {
		for (Iterator<Entity> iter = sourceData.listIterator(); iter.hasNext();) {
			Entity source = iter.next();
			for (Entity dist : distData) {
				if(source.C_ID.equals(dist.C_ID)){
					sourceDeleteData.add(source.C_ID);
					iter.remove();
					Log.i("source.C_ID="+source.C_ID + ",dist.C_ID = " + dist.C_ID);
				}
			}
		}
	}

	/**
	 * @param first
	 * @return
	 */
	public static List<Entity> readDistDataBase(boolean first) {
		configInfo = PropertiesXmlFileUtil.readXML(SUBORDINATE_DATABASE_XML2);
		List<Entity> distData = DbUtil.select(dist_table,first);
		Log.i("distData.size="+distData.size());
		return distData;
	}

	/**
	 * @param first
	 * @return
	 */
	public static List<Entity> readSourceDataBase(boolean first) {
		initSourceDataBaseConfig();
		List<Entity> sourceData = DbUtil.select(source_table,first);
		Log.i("sourceData.size="+sourceData.size());
		return sourceData;
	}

	/**
	 * 
	 */
	public static void initSourceDataBaseConfig() {
		configInfo = PropertiesXmlFileUtil.readXML(MASTER_DATABASE_XML2);
	}

	public static synchronized void save2DistDB(boolean first) {
		DbUtil.insert(getNewBloodglucoseData(first),dist_table);
		if (sourceDeleteData.size()>0) {
			deleteTempSourceData(sourceDeleteData);
		}
	}
	
	
	public static void deleteTempSourceData(List<String> datas){
		initSourceDataBaseConfig();
		DbUtil.delete(datas);
		clear();
	}
	
	private static void clear(){
		if (sourceDeleteData.size()>0) {
			sourceDeleteData.clear();
		}
	}
	
	
	public static void  saveClientTest(List<Entity> list) {
		initSourceDataBaseConfig();
		DbUtil.insert(list,source_table);
	}

}
