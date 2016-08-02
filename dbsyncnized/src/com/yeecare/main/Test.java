package com.yeecare.main;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import com.yeecare.bean.Entity;
import com.yeecare.bz.DBManager;

public class Test {

	public Test() {
	}

	public static void main(String[] args) {
		int delay = 0;// 毫秒
		int time = 1000;// 1s

		TimerTask task = new TimerTask() {
			long C_DSYNC = 0;

			@Override
			public void run() {
				long beginTime = System.currentTimeMillis();
				ArrayList<Entity> list = new ArrayList<Entity>();
				for (int i = 0; i < 100; i++) {
					C_DSYNC++;
					Timestamp c_TIME = new Timestamp(2016, 8, 02, 10, 30, 30, 30);
					Entity bloodglucose = new Entity(UUID.randomUUID()
							.toString(), "test", "", C_DSYNC, c_TIME, 8.5, (short) 0,
							(short) 0, (short) 1, (short) 1, c_TIME,
							"192.168.1.0:8080");
					list.add(bloodglucose);
				}
				DBManager.saveClientTest(list);

				long endTime = System.currentTimeMillis();

				System.out.println("program running time:" + (endTime - beginTime)/1000
						+ " ms");
			}

		};
		Timer timer = new Timer();
		timer.schedule(task, delay, time);

	}

}
