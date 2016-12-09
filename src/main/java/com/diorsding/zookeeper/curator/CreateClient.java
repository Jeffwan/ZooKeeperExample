package com.diorsding.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.diorsding.zookeeper.constants.Constants;

public class CreateClient {
	
	public static void main(String[] args) throws Exception {
		CuratorFramework client = getClientSimple();
		client = getClientByFluentBuilder();
		
		client.start();
		Thread.sleep(Integer.MAX_VALUE);
	}

	static private CuratorFramework getClientSimple() {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(Constants.timeout, 3);

		CuratorFramework client = CuratorFrameworkFactory.newClient(Constants.connectionString, retryPolicy);

		return client;
	}

	static private CuratorFramework getClientByFluentBuilder() {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(Constants.timeout, 3);

		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(Constants.connectionString)
				.sessionTimeoutMs(Constants.timeout)
				.retryPolicy(retryPolicy)
				.namespace("Triton")  // prepend the namespace to all path
				.build();

		return client;
	}
}
