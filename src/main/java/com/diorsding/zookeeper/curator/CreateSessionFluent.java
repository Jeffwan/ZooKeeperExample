package com.diorsding.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.diorsding.zookeeper.constants.Constants;

public class CreateSessionFluent {
	
	public static void main(String[] args) throws Exception {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(Constants.timeout, 3);
		
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(Constants.connectionString)
				.sessionTimeoutMs(Constants.timeout)
				.retryPolicy(retryPolicy)
				.build();
		
		client.start();
		Thread.sleep(Integer.MAX_VALUE);
	}
}
