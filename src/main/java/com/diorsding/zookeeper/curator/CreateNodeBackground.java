package com.diorsding.zookeeper.curator;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import com.diorsding.zookeeper.helper.ZookeeperClientHelper;

public class CreateNodeBackground {

	static String path = "/zk-book/c1";
	static CuratorFramework client = CuratorFrameworkFactory.builder()
			.connectString(ZookeeperClientHelper.connectionString)
			.sessionTimeoutMs(ZookeeperClientHelper.timeout)
			.retryPolicy(new ExponentialBackoffRetry(ZookeeperClientHelper.timeout, 3))
			.build();

	static CountDownLatch semaphore = new CountDownLatch(2);

	// Specify Executor in background.
	static ExecutorService tp = Executors.newFixedThreadPool(2);
	
	public static void main(String[] args) throws Exception {
		client.start();
		
		System.out.println("Main thread : " + Thread.currentThread().getName());

		// Thread of processResult: pool-3-thread-1
		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
			public void processResult(CuratorFramework client, CuratorEvent event)
					throws Exception {
				System.out.println("event[code : " + event.getResultCode() + ", type: " + event.getType() + "]");
				System.out.println("Thread of processResult: " + Thread.currentThread().getName());
				
				semaphore.countDown();
			}
		}, tp).forPath(path, "init".getBytes());

		// Thread of processResult: main-EventThread
		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
			
			public void processResult(CuratorFramework client, CuratorEvent event)
					throws Exception {
				System.out.println("event[code : " + event.getResultCode() + ", type: " + event.getType() + "]");
				System.out.println("Thread of processResult: " + Thread.currentThread().getName());
				semaphore.countDown();
			}
		}).forPath(path, "init".getBytes());
		
		semaphore.await();
		tp.shutdown();
	}
}
