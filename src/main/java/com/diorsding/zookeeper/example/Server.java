package com.diorsding.zookeeper.example;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * Created by jiashan on 8/27/16.
 */
public class Server {

    private String pnode="sgroup";
    private String snode="sub";
    private String host="localhost:2181";
    public void connect(String content) throws Exception {
        ZooKeeper zk=new ZooKeeper(host,5000,new Watcher(){
            public void process(WatchedEvent event){
                //no process
            }
        });
        String createdPath=zk.create("/"+pnode+"/"+snode, content.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("create :"+createdPath);
    }

    public void handle() throws InterruptedException{
        Thread.sleep(Long.MAX_VALUE);
    }
    public static void main(String[] args){
        Multi server1=new Multi(1);
        Multi server2=new Multi(2);
        Multi server3=new Multi(3);
        new Thread(server1,"server1").start();
        new Thread(server2,"server1").start();
        new Thread(server3,"server1").start();
    }
}
class Multi implements Runnable {
    private int seq;
    public Multi(int seq){
        this.seq=seq;
    }
    public void run(){
        try {
            Server as=new Server();
            as.connect("register");
            System.out.println("server"+this.seq+" registered");
            as.handle();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
