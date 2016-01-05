package com.diorsding.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

import com.diorsding.zookeeper.constants.Constants;

public class ConstructorUsageSimple implements Watcher {
	
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	
	public static void main(String[] args) throws Exception {
		ZooKeeper zookeeper = new ZooKeeper(Constants.connectionString, Constants.timeout, new ConstructorUsageSimple());
		System.out.println(zookeeper.getState());  // CONNECTING
		
		try {
			connectedSemaphore.await();
		} catch (InterruptedException e) {
			System.out.println("Zookeeper Session Established");
			System.out.println(zookeeper.getState());
		}
	}
	
	public void process(WatchedEvent event) {
		System.out.println("Receive watched event: " + event); // Receive watched event: WatchedEvent state:SyncConnected type:None path:null
		if (KeeperState.SyncConnected == event.getState()) {
			connectedSemaphore.countDown();
		}
	}

}
