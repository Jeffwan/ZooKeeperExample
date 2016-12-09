package com.diorsding.zookeeper.crud;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import com.diorsding.zookeeper.constants.Constants;

public class ExistAPISync implements Watcher {
	
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	private static ZooKeeper zookeeper = null;
	
	public static void main(String[] args) throws Exception {
		String path = "/zk-book"; 

		zookeeper = new ZooKeeper(Constants.connectionString, Constants.timeout, new ExistAPISync());
		connectedSemaphore.await();
		
		zookeeper.exists(path, true);
		zookeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zookeeper.setData(path, "123".getBytes(), -1);
		
		zookeeper.create(path + "/c1", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		
		zookeeper.delete(path + "/c1", -1);
		zookeeper.delete(path, -1);
		
		Thread.sleep(Integer.MAX_VALUE);
	}
	
	public void process(WatchedEvent event) {
		try {
			if (KeeperState.SyncConnected == event.getState()) {
				if (EventType.None == event.getType() && null == event.getPath()) {
					connectedSemaphore.countDown();
				} else if (EventType.NodeCreated == event.getType()) {
					System.out.println("Node(" + event.getPath() + ") DataCreated");
					zookeeper.exists(event.getPath(), true);
				} else if (EventType.NodeDeleted == event.getType()) {
					System.out.println("Node(" + event.getPath() + ") DataDeleted");
					zookeeper.exists(event.getPath(), true);
				} else if (EventType.NodeDataChanged == event.getType()) {
					System.out.println("Node(" + event.getPath() + ") DataChanged");
					zookeeper.exists(event.getPath(), true);
				}
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
