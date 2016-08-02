package com.yeecare.main;

import java.util.Timer;
import java.util.TimerTask;

import com.yeecare.bz.DBManager;

public class Test {

	public Test() {
	}

	public static void main(String[] args) {
		int delay = 0;// 毫秒
		int time =  1000;// 1s

		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				long beginTime = System.currentTimeMillis();
				DBManager.save2DistDB(false);
				
				long endTime = System.currentTimeMillis();
				System.out.println("running time:"+(endTime - beginTime) + " ms");
			}

		};
		Timer timer = new Timer();
		timer.schedule(task, delay, time);
		

	}

}
