package com.yeecare.util;

import com.yeecare.bean.DataBaseConfigInfo;



public class Base {
	protected static final String dist_table = "bloodglucose";
	protected static final String source_table = "temp_crm_bloodglucose";//crm_clientglucose  

	protected static DataBaseConfigInfo configInfo ;
	
	protected static final String SUBORDINATE_DATABASE_XML = "com/yeecare/bz/subordinate_database.xml";
	protected static final String MASTER_DATABASE_XML = "com/yeecare/bz/master_database.xml";
	
	protected static final String SUBORDINATE_DATABASE_XML2 = "subordinate_database.xml";
	protected static final String MASTER_DATABASE_XML2 = "master_database.xml";
	
}
