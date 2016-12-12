package com.diorsding.zookeeper.recipes;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.diorsding.zookeeper.helper.ZookeeperClientHelper;

public class Barrier2Sample {

	static String barrierPath = "/curator_recipes_barrier2_path";
	
	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 5; i++) {
			new Thread( new Runnable() {
				
				public void run() {
					try {
						CuratorFramework client = CuratorFrameworkFactory.builder()
								.connectString(ZookeeperClientHelper.connectionString)
								.sessionTimeoutMs(ZookeeperClientHelper.timeout)
								.retryPolicy(new ExponentialBackoffRetry(ZookeeperClientHelper.timeout, 3))
								.build();
						
						DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(client, barrierPath, 5);  
						Thread.sleep(Math.round(Math.random() * 3000));
						System.out.println(Thread.currentThread().getName() + " No Enter into Barrier");
						
						barrier.enter();
						System.out.println("Starting.. ");
						Thread.sleep(Math.round(Math.random() * 3000));
						barrier.leave();
						System.out.println("Exiting.. ");

					} catch (Exception e) {}
				}
			}).start();
		}
	}
	
}
