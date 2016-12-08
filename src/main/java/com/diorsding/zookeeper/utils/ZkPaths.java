package com.diorsding.zookeeper.utils;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.curator.utils.ZKPaths.PathAndNode;
import org.apache.zookeeper.ZooKeeper;

import com.diorsding.zookeeper.constants.Constants;

public class ZkPaths {

	static String path = "/curator_zkpath_sample";
	static CuratorFramework client = CuratorFrameworkFactory.builder()
			.connectString(Constants.connectionString)
			.sessionTimeoutMs(Constants.timeout)
			.retryPolicy(new ExponentialBackoffRetry(Constants.timeout, 3))
			.build();
	
	public static void main(String[] args) throws Exception {
		client.start();
		
		ZooKeeper zookeeper = client.getZookeeperClient().getZooKeeper();
		System.out.println(ZKPaths.fixForNamespace(path, "sub"));
		System.out.println(ZKPaths.makePath(path, "sub"));
		
		System.out.println(ZKPaths.getNodeFromPath("/curator_zkpath_sample/sub1"));
		
		PathAndNode pathAndNode = ZKPaths.getPathAndNode("/curator_zkpath_sample/sub1");
		System.out.println(pathAndNode.getPath());
		System.out.println(pathAndNode.getNode());
		
		
		String dir1 = path + "/child1";
		String dir2 = path + "/child2";
		ZKPaths.mkdirs(zookeeper, dir1);
		ZKPaths.mkdirs(zookeeper, dir2);
		
		System.out.println(ZKPaths.getSortedChildren(zookeeper, path));
		ZKPaths.deleteChildren(client.getZookeeperClient().getZooKeeper(), path, true);
	}
	
}
