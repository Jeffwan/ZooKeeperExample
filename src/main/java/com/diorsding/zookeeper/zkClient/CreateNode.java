package com.diorsding.zookeeper.zkClient;

import org.I0Itec.zkclient.ZkClient;

import com.diorsding.zookeeper.constants.Constants;

public class CreateNode {
	
	public static void main(String[] args) {
		ZkClient zkClient = new ZkClient(Constants.connectionString, Constants.timeout);
		String path = "/zk-book/c1";
		
		// Automatically create parent not even if not exists.
		zkClient.createPersistent(path, true);
	}
	
}
