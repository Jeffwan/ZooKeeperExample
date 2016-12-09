package com.diorsding.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

import com.diorsding.zookeeper.constants.Constants;

public class ConnectionWatchWithSidPassWD implements Watcher {
	
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	
	public static void main(String[] args) throws IOException, InterruptedException {
		ZooKeeper zookeeper = new ZooKeeper(Constants.connectionString, Constants.timeout, new ConnectionWatchWithSidPassWD());
		connectedSemaphore.await();

		// SessionId is only available after SyncConnected status. sessionId = 0 in connecting status.
		long sessionId = zookeeper.getSessionId();
		byte[] passwd = zookeeper.getSessionPasswd();
		System.out.println("SessionId: " + sessionId + " ,Passwd: " + passwd);
		
		// Use illegal sessionId and sessionPassWd - Expired
		zookeeper = new ZooKeeper(Constants.connectionString, Constants.timeout, new ConnectionWatchWithSidPassWD(), 1l, "test".getBytes());
	
		// Use correct sessionId and sessionPassWd - firstly Disconnected and then SyncConnected.
		// Why here repeatedly show disconnected and syncConnected.
		zookeeper = new ZooKeeper(Constants.connectionString, Constants.timeout, new ConnectionWatchWithSidPassWD(), sessionId, passwd);
		
		Thread.sleep(Integer.MAX_VALUE);
	}	
	
	public void process(WatchedEvent event) {
		System.out.println("Receive watched event: " + event);
		
		if (KeeperState.SyncConnected == event.getState()) {
			connectedSemaphore.countDown();
		}
	}
}
