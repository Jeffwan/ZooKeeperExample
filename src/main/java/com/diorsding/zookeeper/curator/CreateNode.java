package com.diorsding.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import com.diorsding.zookeeper.helper.ZookeeperClientHelper;

public class CreateNode {
	
	static String path = "/zk-book/c1";
	static RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
	static CuratorFramework client = CuratorFrameworkFactory.builder()
			.connectString(ZookeeperClientHelper.connectionString)
			.sessionTimeoutMs(ZookeeperClientHelper.timeout)
			.retryPolicy(retryPolicy)
			.build();
	
	public static void main(String[] args) throws Exception {
		client.start();

		String result = client.create()
				.creatingParentsIfNeeded()
				.withMode(CreateMode.EPHEMERAL)
				.forPath(path, "init".getBytes());

		// Path /zk-book/c1 is returned
		System.out.println(result);
	}
}
