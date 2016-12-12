package com.diorsding.zookeeper.recipes;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

import com.diorsding.zookeeper.helper.ZookeeperClientHelper;

public class DistAtomicInt {
	
	static String distatomicintPath = "/curator_recipes_distatomicint_path";
	static CuratorFramework client = CuratorFrameworkFactory.builder()
			.connectString(ZookeeperClientHelper.connectionString)
			.sessionTimeoutMs(ZookeeperClientHelper.timeout)
			.retryPolicy(new ExponentialBackoffRetry(ZookeeperClientHelper.timeout, 3))
			.build();
	
	public static void main(String[] args) throws Exception {
		client.start();
		
		DistributedAtomicInteger atomicInteger = new DistributedAtomicInteger(client, distatomicintPath, 
				new RetryNTimes(3, 1000));
		
		AtomicValue<Integer> rc = atomicInteger.add(8);
		System.out.println("Result: " + rc.succeeded());
	}
}
