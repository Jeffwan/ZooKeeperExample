package com.diorsding.zookeeper.naive;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import com.diorsding.zookeeper.helper.ZookeeperClientHelper;

public class GetDataAPIASync implements Watcher {

	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	private static ZooKeeper zookeeper = null;
	
	public static void main(String[] args) throws Exception {
		String path = "/zk-book";
		zookeeper = new ZooKeeper(ZookeeperClientHelper.connectionString, ZookeeperClientHelper.timeout, new GetDataAPIASync());
		
		connectedSemaphore.await();
		
		zookeeper.create(path, "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		zookeeper.getData(path, true, new IDataCallback(), null);
		zookeeper.setData(path, "132".getBytes(), -1);
		
		Thread.sleep(Integer.MAX_VALUE);
	}
	
	public void process(WatchedEvent event) {
		if (KeeperState.SyncConnected == event.getState()) {
			if (EventType.None == event.getType() && null == event.getPath()) {
				connectedSemaphore.countDown();
			} else if (event.getType() == EventType.NodeDataChanged) {
				try {
					zookeeper.getData(event.getPath(), true, new IDataCallback(), null);
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}
}

class IDataCallback implements AsyncCallback.DataCallback {

	public void processResult(int rc, String path, Object ctx, byte[] data,
			Stat stat) {
		System.out.println(rc + " , " + path + " , " + new String(data));
		System.out.println(stat.getCzxid() + " , " + stat.getMzxid() + " , " + stat.getVersion());
	}
	
}
