package com.diorsding.zookeeper.utils;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.EnsurePath;

import com.diorsding.zookeeper.constants.Constants;

public class EnsurePathDemo {

	static String path = "/zk-book/c1";
	static CuratorFramework client = CuratorFrameworkFactory.builder()
			.connectString(Constants.connectionString)
			.sessionTimeoutMs(Constants.timeout)
			.retryPolicy(new ExponentialBackoffRetry(Constants.timeout, 3))
			.build();
	
	
	public static void main(String[] args) throws Exception {
		client.start();
		client.usingNamespace("zk-book");
		
		EnsurePath ensurePath = new EnsurePath(path);
		ensurePath.ensure(client.getZookeeperClient());
		ensurePath.ensure(client.getZookeeperClient());
		
		EnsurePath ensurePath2 = client.newNamespaceAwareEnsurePath("/c1");
		ensurePath2.ensure(client.getZookeeperClient());
	}
}
