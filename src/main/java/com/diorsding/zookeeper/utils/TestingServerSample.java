package com.diorsding.zookeeper.utils;

import java.io.File;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;

import com.diorsding.zookeeper.helper.ZookeeperClientHelper;

public class TestingServerSample {
	
	static String path = "/zookeeper";

	public static void main(String[] args) throws Exception {
		TestingServer server = new TestingServer(2181, new File("/Users/jiashan/zk-book-data"));
		
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(ZookeeperClientHelper.connectionString)
				.sessionTimeoutMs(ZookeeperClientHelper.timeout)
				.retryPolicy(new ExponentialBackoffRetry(ZookeeperClientHelper.timeout, 3))
				.build();
		
		client.start();
		System.out.println(client.getChildren().forPath(path));
		server.close();
	}
	
}
