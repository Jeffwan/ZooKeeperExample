package com.diorsding.zookeeper.crud;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import com.diorsding.zookeeper.constants.Constants;

public class GetDataAPISync implements Watcher{

	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	private static ZooKeeper zookeeper = null;
	private static Stat stat = new Stat();
	
	public static void main(String[] args) throws Exception {
		String path = "/zk-book"; 

		zookeeper = new ZooKeeper(Constants.connectionString, Constants.timeout, new GetDataAPISync());
		
		connectedSemaphore.await();
		
		zookeeper.create(path, "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		
		System.out.println(new String(zookeeper.getData(path, true, stat)));
		System.out.println(stat.getCzxid() + " , " + stat.getMzxid() + " , " + stat.getVersion());
		
		zookeeper.setData(path, "132".getBytes(), stat.getVersion());
		
		Thread.sleep(Integer.MAX_VALUE);
	}
	
	public void process(WatchedEvent event) {
		if (KeeperState.SyncConnected == event.getState()) {
			if (EventType.None == event.getType() && null == event.getPath()) {
				connectedSemaphore.countDown();
			} else if (event.getType() == EventType.NodeDataChanged) {
				try {
					System.out.println(new String(zookeeper.getData(event.getPath(), true, stat)));
					System.out.println(stat.getCzxid() + " , " + stat.getMzxid() + " , " + stat.getVersion());
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}

}
