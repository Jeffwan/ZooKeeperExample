package com.diorsding.zookeeper.crud;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import com.diorsding.zookeeper.constants.Constants;

public class SetDataAPISync implements Watcher {

	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	private static ZooKeeper zookeeper = null;
	
	public static void main(String[] args) throws Exception {
		String path = "/zk-book"; 

		zookeeper = new ZooKeeper(Constants.connectionString, Constants.timeout, new SetDataAPISync());
		
		connectedSemaphore.await();
		
		zookeeper.create(path, "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		
		zookeeper.getData(path, true, null);
		
		Stat stat = zookeeper.setData(path, "456".getBytes(), -1);
		System.out.println(stat.getCzxid() + " , " + stat.getMzxid() + " , " + stat.getVersion());
		
		Stat stat2 = zookeeper.setData(path, "456".getBytes(), stat.getVersion());
		System.out.println(stat2.getCzxid() + " , " + stat2.getMzxid() + " , " + stat2.getVersion());
		
		
		try {
			zookeeper.setData(path, "123".getBytes(), stat.getVersion());
		} catch (KeeperException e) {
			System.out.println("Error: " + e.code() + ", " + e.getMessage());
		}
		
		Thread.sleep(Integer.MAX_VALUE);
	}
	
	public void process(WatchedEvent event) {
		if (KeeperState.SyncConnected == event.getState()) {
			if (EventType.None == event.getType() && null == event.getPath()) {
				connectedSemaphore.countDown();
			}
		}
	}
}


