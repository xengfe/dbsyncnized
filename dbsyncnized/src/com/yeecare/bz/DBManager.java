package com.yeecare.bz;
import java.util.Iterator;
import java.util.List;

import com.yeecare.bean.Entity;
import com.yeecare.util.Base;
import com.yeecare.util.Log;


public class DBManager extends Base {

	


	public static List<Entity> compare(boolean first) {
		
		configInfo = PropertiesXmlFileUtil.readXML(MASTER_DATABASE_XML2);

		List<Entity> sourceData = DbUtil.select(source_table,first);
		Log.i("sourceData.size="+sourceData.size());
		
		configInfo = PropertiesXmlFileUtil.readXML(SUBORDINATE_DATABASE_XML2);


		List<Entity> distData = DbUtil.select(dist_table,first);
		Log.i("distData.size="+distData.size());
		for (Iterator<Entity> iter = sourceData.listIterator(); iter.hasNext();) {
			Entity source = iter.next();
			for (Entity dist : distData) {
				if(source.C_ID.equals(dist.C_ID)){
					iter.remove();
					Log.i("source.C_ID="+source.C_ID);
					Log.i("dist.C_ID = " + dist.C_ID);
				}
			}
		}

		return sourceData;

	}

	public static void save2DistDB(boolean first) {
		DbUtil.insert(compare(first));
	}
	
	
	public static void deleteTempSourceData(){
		DbUtil.delete();
	}

}
