package com.diorsding.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.diorsding.zookeeper.constants.Constants;

public class CreateSessionNamespace {
	public static void main(String[] args) {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(Constants.connectionString)
				.sessionTimeoutMs(Constants.timeout)
				.retryPolicy(retryPolicy)
				.build();	
		
		client.start();
	}
}
