package com.diorsding.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import com.diorsding.zookeeper.constants.Constants;

public class CreateNode {
	
	static String path = "/zk-book/c1";
	static RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
	static CuratorFramework client = CuratorFrameworkFactory.builder()
			.connectString(Constants.connectionString)
			.sessionTimeoutMs(Constants.timeout)
			.retryPolicy(retryPolicy)
			.build();
	
	
	public static void main(String[] args) throws Exception {
		client.start();
		
		client.create()
			.creatingParentsIfNeeded()
			.withMode(CreateMode.EPHEMERAL)
			.forPath(path, "init".getBytes());
	}
}
