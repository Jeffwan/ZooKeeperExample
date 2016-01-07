package com.diorsding.zookeeper.recipes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.diorsding.zookeeper.constants.Constants;

public class Lock {
	
	static String lockPath = "/curator_recipes_lock_path";
	static CuratorFramework client = CuratorFrameworkFactory.builder()
			.connectString(Constants.connectionString)
			.sessionTimeoutMs(Constants.timeout)
			.retryPolicy(new ExponentialBackoffRetry(Constants.timeout, 3))
			.build();
	
	public static void main(String[] args) {
		client.start();
		final InterProcessMutex lock = new InterProcessMutex(client, lockPath);
		final CountDownLatch down = new CountDownLatch(1);
		for (int i = 0; i < 30; i++) {
			new Thread (new Runnable() {
				
				public void run() {
					try {
						down.await();
						lock.acquire();
					} catch (Exception e) {
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss|SSS");
						String orderNo = simpleDateFormat.format(new Date());
						System.out.println("Generated Order Id is : " + orderNo);
						
						try {
							lock.release();
						} catch (Exception e2) {}
					}
				}
			}).start();
		}
		down.countDown();
	}
}
