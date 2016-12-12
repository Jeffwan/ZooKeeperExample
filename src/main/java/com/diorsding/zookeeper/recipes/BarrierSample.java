package com.diorsding.zookeeper.recipes;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.diorsding.zookeeper.helper.ZookeeperClientHelper;

public class BarrierSample {
	
	static String barrierPath = "/curator_recipes_barrier_path";
	static DistributedBarrier barrier;  
	
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
						
						client.start();
						barrier = new DistributedBarrier(client, barrierPath);
						System.out.println(Thread.currentThread().getName());
						
						barrier.setBarrier();
						barrier.waitOnBarrier();
						System.out.println("Starting.. ");
					} catch (Exception e) {}
				}
			}).start();
		}
		
		Thread.sleep(2000);
		barrier.removeBarrier();
	}

}
