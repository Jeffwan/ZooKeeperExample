package com.diorsding.zookeeper.helper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZookeeperClientHelper {
	
	public static final String host = "45.55.66.14";
	public static final long port = 2181;
	public static final String connectionString = host + ":" +String.valueOf(port);
	
	public static final int timeout = 5000;


	public static CuratorFramework createCuratorFrameworkClient() {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient(ZookeeperClientHelper.connectionString, retryPolicy);

		return client;
	}
	
	
}
