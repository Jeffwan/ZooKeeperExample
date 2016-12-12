package com.diorsding.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.diorsding.zookeeper.helper.ZookeeperClientHelper;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;

public class CreateClient {
	private static final String PATH = "/exmaple";

	public static void main(String[] args) throws Exception {
		CuratorFramework client = createClientSimple();
		client.start();
		client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(PATH, "test".getBytes());
		System.out.println(new String(client.getData().forPath(PATH)));
		CloseableUtils.closeQuietly(client); // We need to use persistent node because client will be closed before setData


		client = createClientWithOptions();
		client.start();
		client.setData().forPath(PATH, "testAgain".getBytes());
		client.setData().forPath(PATH, "testAgainAgain".getBytes());
		System.out.println(new String(client.getData().forPath(PATH)));

		// Both way works for client close
		client.close();
	}

	static private CuratorFramework createClientSimple() {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(ZookeeperClientHelper.timeout, 3);

		CuratorFramework client = CuratorFrameworkFactory.newClient(ZookeeperClientHelper.connectionString, retryPolicy);

		return client;
	}

	// FluentBuilder
	static private CuratorFramework createClientWithOptions() {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(ZookeeperClientHelper.timeout, 3);

		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(ZookeeperClientHelper.connectionString)
				.sessionTimeoutMs(ZookeeperClientHelper.timeout)
				.connectionTimeoutMs(ZookeeperClientHelper.timeout)
						// etc. etc.
				.retryPolicy(retryPolicy)
				//.namespace("Triton")  // prepend the namespace to all path
				.build();

		return client;
	}
}
