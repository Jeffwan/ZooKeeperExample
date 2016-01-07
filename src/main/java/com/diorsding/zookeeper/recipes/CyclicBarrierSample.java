package com.diorsding.zookeeper.recipes;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclicBarrierSample {
	
	public static CyclicBarrier barrier = new CyclicBarrier(3);
	
	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(3);
		
		executor.submit(new Thread(new Runner("No.1 ")));
		executor.submit(new Thread(new Runner("No.2 ")));
		executor.submit(new Thread(new Runner("No.3 ")));
		executor.shutdown();
	}
	
}

class Runner implements Runnable {

	private String name; 
	public Runner (String name) {
		this.name = name;
	}
	
	public void run() {
		System.out.println(name  + " is ready");
		try {
			CyclicBarrierSample.barrier.await();
		} catch (Exception e) {
			System.out.println(name + " is running");
		}
	}
	
}
