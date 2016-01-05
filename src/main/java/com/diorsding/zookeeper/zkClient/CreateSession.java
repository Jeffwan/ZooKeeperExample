package com.diorsding.zookeeper.zkClient;

import org.I0Itec.zkclient.ZkClient;

import com.diorsding.zookeeper.constants.Constants;

public class CreateSession {
	
	public static void main(String[] args) {
		ZkClient zkClient = new ZkClient(Constants.connectionString, Constants.timeout);
	}
}
