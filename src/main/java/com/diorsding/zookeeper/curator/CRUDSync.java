package com.diorsding.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import com.diorsding.zookeeper.helper.ZookeeperClientHelper;

public class CRUDSync {

	static String path = "/zk-book/c1";
	static CuratorFramework client = CuratorFrameworkFactory.builder()
			.connectString(ZookeeperClientHelper.connectionString)
			.sessionTimeoutMs(ZookeeperClientHelper.timeout)
			.retryPolicy(new ExponentialBackoffRetry(ZookeeperClientHelper.timeout, 3))
			.build();
	
	public static void main(String[] args) throws Exception {
		client.start();
		
		client.create()
			.creatingParentsIfNeeded()
			.withMode(CreateMode.EPHEMERAL)
			.forPath(path, "init".getBytes());
		
		Stat stat = new Stat();
		client.getData().storingStatIn(stat).forPath(path);

		System.out.println("[Version: " + stat.getVersion() + ", zxid: " + stat.getCzxid() + " ]");
		System.out.println(stat);


		int newVersion = client.setData().withVersion(stat.getVersion()).forPath(path).getVersion();
		System.out.println("Success set node for : " + path + ", new version: " + newVersion);

		try {
			client.setData().withVersion(stat.getVersion()).forPath(path); // old version
		} catch (Exception e) {
			System.out.println("Fail set node due to " + e.getMessage());
		}


		// We have to cache stat to finish a deletion?
		client.delete().deletingChildrenIfNeeded()
			.withVersion(newVersion).forPath(path);
	}
}
