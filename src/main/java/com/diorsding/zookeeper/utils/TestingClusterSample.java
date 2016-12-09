package com.diorsding.zookeeper.utils;

import org.apache.curator.test.TestingCluster;
import org.apache.curator.test.TestingZooKeeperServer;

public class TestingClusterSample {

	
	public static void main(String[] args) throws Exception {
		TestingCluster cluster = new TestingCluster(3);
		cluster.start();
		Thread.sleep(2000);
		
		TestingZooKeeperServer leader = null;
		
		for (TestingZooKeeperServer zooKeeperServer : cluster.getServers()) {
			System.out.print(zooKeeperServer.getInstanceSpec().getServerId() + "-");
			System.out.print(zooKeeperServer.getQuorumPeer().getServerState() + "-");
			System.out.print(zooKeeperServer.getInstanceSpec().getDataDirectory().getAbsolutePath());
			System.out.println("");
			
			if (zooKeeperServer.getQuorumPeer().getServerState().equals("leading")) {
				leader = zooKeeperServer;
			}
		}
		
		leader.kill();
		
		System.out.println("-- After leader kill: ");
		
		for (TestingZooKeeperServer zooKeeperServer : cluster.getServers()) {
			System.out.print(zooKeeperServer.getInstanceSpec().getServerId() + "-");
			System.out.print(zooKeeperServer.getQuorumPeer().getServerState() + "-");
			System.out.print(zooKeeperServer.getInstanceSpec().getDataDirectory().getAbsolutePath());
			System.out.println("");
		}
		
		cluster.stop();
	}
}
