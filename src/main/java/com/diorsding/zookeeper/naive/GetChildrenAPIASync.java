package com.diorsding.zookeeper.naive;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import com.diorsding.zookeeper.helper.ZookeeperClientHelper;

public class GetChildrenAPIASync implements Watcher {

	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	private static ZooKeeper zookeeper = null;
	
	public static void main(String[] args) throws Exception {
		String path = "/zk-book";
		
		zookeeper = new ZooKeeper(ZookeeperClientHelper.connectionString, ZookeeperClientHelper.timeout, new GetChildrenAPIASync());
		connectedSemaphore.await();

		zookeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zookeeper.create(path + "/c1", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		zookeeper.getChildren(path, true, new IChildren2CallBack(), null);
		zookeeper.create(path + "/c2", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		
		Thread.sleep(Integer.MAX_VALUE);

	}
	
	public void process(WatchedEvent event) {
		if (KeeperState.SyncConnected == event.getState()) {
			if (EventType.None == event.getType() && null == event.getPath()) {
				connectedSemaphore.countDown();
			} else if (event.getType() == EventType.NodeChildrenChanged) {
				try {
					System.out.println("ReGet Child: " + zookeeper.getChildren(event.getPath(), true));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}

}


class IChildren2CallBack implements AsyncCallback.Children2Callback {

	public void processResult(int rc, String path, Object ctx,
			List<String> children, Stat stat) {
		
		System.out.println("Get Children znode Result: [Response Code: " + rc + ", Path " + path 
				+ ", Ctx" + ctx + ", " + "children list: " + children + ", stat: " + stat);
	}
	
}