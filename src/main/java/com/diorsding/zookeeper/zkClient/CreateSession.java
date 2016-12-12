package com.diorsding.zookeeper.zkClient;

import org.I0Itec.zkclient.ZkClient;

import com.diorsding.zookeeper.helper.ZookeeperClientHelper;

public class CreateSession {
	
	public static void main(String[] args) {
		ZkClient zkClient = new ZkClient(ZookeeperClientHelper.connectionString, ZookeeperClientHelper.timeout);
		System.out.println("Zookeeper session established");
	}
}
