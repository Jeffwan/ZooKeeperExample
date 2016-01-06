package com.diorsding.zookeeper.zkClient;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import com.diorsding.zookeeper.constants.Constants;

public class GetChildren {

	public static void main(String[] args) throws Exception {
		String path = "/zk-book";
		ZkClient zkClient = new ZkClient(Constants.connectionString, Constants.timeout);
		
		zkClient.subscribeChildChanges(path, new IZkChildListener() {
			
			public void handleChildChange(String parentPath, List<String> currentChilds)
					throws Exception {
				System.out.println(parentPath + " 's child changed, currentChilds : " + currentChilds);				
			}
		});
		
		zkClient.createPersistent(path);
		Thread.sleep(1000);
		System.out.println(zkClient.getChildren(path));
		Thread.sleep(1000);
		
		zkClient.createPersistent(path + "/c1");
		Thread.sleep(1000);
		zkClient.delete(path + "/c1");
		Thread.sleep(1000);
		zkClient.delete(path);
		
		Thread.sleep(Integer.MAX_VALUE);
	}
}
