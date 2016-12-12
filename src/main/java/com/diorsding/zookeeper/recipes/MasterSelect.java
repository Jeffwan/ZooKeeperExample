package com.diorsding.zookeeper.recipes;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.diorsding.zookeeper.helper.ZookeeperClientHelper;

public class MasterSelect {

	static String masterPath = "/curator_recipes_master_path";
	static CuratorFramework client = CuratorFrameworkFactory.builder()
			.connectString(ZookeeperClientHelper.connectionString)
			.sessionTimeoutMs(ZookeeperClientHelper.timeout)
			.retryPolicy(new ExponentialBackoffRetry(ZookeeperClientHelper.timeout, 3))
			.build();
	
	
	public static void main(String[] args) throws Exception {
		client.start();
		LeaderSelector selector = new LeaderSelector(client, masterPath, new LeaderSelectorListenerAdapter() {
			
			public void takeLeadership(CuratorFramework client) throws Exception {
				System.out.println("Become Master Role");
				Thread.sleep(3000);
				System.out.println("Finish master operation, release Master Permission");
			}
		});
		
		selector.autoRequeue();
		selector.start();
		Thread.sleep(Integer.MAX_VALUE);
	}
}
