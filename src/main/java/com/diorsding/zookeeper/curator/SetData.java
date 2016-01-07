package com.diorsding.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import com.diorsding.zookeeper.constants.Constants;

public class SetData {

	static String path = "/zk-book/c1";
	static CuratorFramework client = CuratorFrameworkFactory.builder()
			.connectString(Constants.connectionString)
			.sessionTimeoutMs(Constants.timeout)
			.retryPolicy(new ExponentialBackoffRetry(Constants.timeout, 3))
			.build();
	
	public static void main(String[] args) throws Exception {
		client.start();
		client.create()
			.creatingParentsIfNeeded()
			.withMode(CreateMode.EPHEMERAL)
			.forPath(path, "init".getBytes());
		
		Stat stat = new Stat();
		
		client.getData().storingStatIn(stat).forPath(path);
		System.out.println("Success set node for : " + path + ", new version: " 
				+ client.setData().withVersion(stat.getVersion()).forPath(path).getVersion());
		
		try {
			client.setData().withVersion(stat.getVersion()).forPath(path);
		} catch (Exception e) {
			System.out.println("Fail set node due to " + e.getMessage());
		}
	}
}
