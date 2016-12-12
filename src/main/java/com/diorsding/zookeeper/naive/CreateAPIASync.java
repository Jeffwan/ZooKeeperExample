package com.diorsding.zookeeper.naive;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;

import com.diorsding.zookeeper.helper.ZookeeperClientHelper;

public class CreateAPIASync implements Watcher {
	
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	
	public static void main(String[] args) throws Exception {
		ZooKeeper zookeeper = new ZooKeeper(ZookeeperClientHelper.connectionString, ZookeeperClientHelper.timeout, new CreateAPIASync());
		connectedSemaphore.await();
		
		zookeeper.create("/zk-test-ephemeral-", "".getBytes(), Ids.OPEN_ACL_UNSAFE, 
				CreateMode.EPHEMERAL, new IStringCallback(), "I am contect");
		
		zookeeper.create("/zk-test-ephemeral-", "".getBytes(), Ids.OPEN_ACL_UNSAFE, 
				CreateMode.EPHEMERAL, new IStringCallback(), "I am contect");
		
		zookeeper.create("/zk-test-ephemeral-", "".getBytes(), Ids.OPEN_ACL_UNSAFE, 
				CreateMode.EPHEMERAL_SEQUENTIAL, new IStringCallback(), "I am Context");
	
		Thread.sleep(Integer.MAX_VALUE);
	}
	
	public void process(WatchedEvent event) {
		if (KeeperState.SyncConnected == event.getState()) {
			connectedSemaphore.countDown();
		}
	}
}

class IStringCallback implements AsyncCallback.StringCallback {
	public void processResult(int rc, String path, Object ctx,
			String name) {
		System.out.println("Create Path Resuklt: [" + rc + ", " + path + ", " + ctx + ", result path name: " + name);
		
	}

}
