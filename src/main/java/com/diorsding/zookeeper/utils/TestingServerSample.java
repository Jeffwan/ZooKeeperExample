package com.diorsding.zookeeper.utils;

import java.io.File;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;

import com.diorsding.zookeeper.constants.Constants;

public class TestingServerSample {
	
	static String path = "/zookeeper";

	public static void main(String[] args) throws Exception {
		TestingServer server = new TestingServer(2181, new File("/Users/jiashan/zk-book-data"));
		
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(Constants.connectionString)
				.sessionTimeoutMs(Constants.timeout)
				.retryPolicy(new ExponentialBackoffRetry(Constants.timeout, 3))
				.build();
		
		client.start();
		System.out.println(client.getChildren().forPath(path));
		server.close();
	}
	
}
