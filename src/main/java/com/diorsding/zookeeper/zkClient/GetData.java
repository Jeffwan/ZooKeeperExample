package com.diorsding.zookeeper.zkClient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import com.diorsding.zookeeper.constants.Constants;

public class GetData {

	public static void main(String[] args) throws Exception {
		String path = "/zk-book";
		
		ZkClient zkClient = new ZkClient(Constants.connectionString, Constants.timeout);
		zkClient.createEphemeral(path, "123");
		
		zkClient.subscribeDataChanges(path, new IZkDataListener() {
			
			public void handleDataDeleted(String dataPath) throws Exception {
				System.out.println("Node " + dataPath + " deleted");
			}
			
			public void handleDataChange(String dataPath, Object data) throws Exception {
				System.out.println("Node " + dataPath + " changed, new data : " + data);
			}
		});
		
		System.out.println(zkClient.readData(path));
		zkClient.writeData(path, "456");
		Thread.sleep(1000);
		
		zkClient.delete(path);
		Thread.sleep(Integer.MAX_VALUE);
	}
}
