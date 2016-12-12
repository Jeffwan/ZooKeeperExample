package com.diorsding.zookeeper.recipes;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import com.diorsding.zookeeper.helper.ZookeeperClientHelper;

public class PathChildrenCacheSample {
	
	static String path = "/zk-book/c1";
	static CuratorFramework client = CuratorFrameworkFactory.builder()
			.connectString(ZookeeperClientHelper.connectionString)
			.sessionTimeoutMs(ZookeeperClientHelper.timeout)
			.retryPolicy(new ExponentialBackoffRetry(ZookeeperClientHelper.timeout, 3))
			.build();
	
	public static void main(String[] args) throws Exception {
		client.start();
		
		PathChildrenCache cache = new PathChildrenCache(client, path, true);
		cache.start(StartMode.POST_INITIALIZED_EVENT);
		cache.getListenable().addListener(new PathChildrenCacheListener() {
			
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
					throws Exception {
				// TODO Auto-generated method stub
				
			}
		});
		
		client.create().withMode(CreateMode.PERSISTENT).forPath(path);
		Thread.sleep(1000);
		
		client.create().withMode(CreateMode.PERSISTENT).forPath(path + "/c1");
		Thread.sleep(1000);
		
		client.delete().forPath(path + "/c1");
		Thread.sleep(1000);
		
		client.delete().forPath(path);
		Thread.sleep(Integer.MAX_VALUE);
	}
	
}
