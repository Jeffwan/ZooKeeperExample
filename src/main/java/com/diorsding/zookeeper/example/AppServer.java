package com.diorsding.zookeeper.example;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * Created by jiashan on 8/27/16.
 * docker run -d -p 2181:2181 jplock/zookeeper
 *
 */
public class AppServer {

    private String pnode="sgroup";
    private String snode="sub";
    private String host="localhost:2181";
    public void connect(String content) throws Exception {
        ZooKeeper zk=new ZooKeeper(host,5000,new Watcher(){
            public void process(WatchedEvent event){
                //no process
            }
        });
        String createdPath=zk.create("/"+pnode+"/"+snode, content.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("create :"+createdPath);
    }
    public void handle() throws InterruptedException{
        Thread.sleep(Long.MAX_VALUE);
    }
    public static void main(String[] args) throws Exception {
        AppServer as=new AppServer();
        as.connect("register");
        as.handle();
    }
}
