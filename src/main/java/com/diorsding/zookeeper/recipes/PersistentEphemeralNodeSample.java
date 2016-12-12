package com.diorsding.zookeeper.recipes;

import com.diorsding.zookeeper.helper.ZookeeperClientHelper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.nodes.PersistentEphemeralNode;
import org.apache.curator.framework.recipes.nodes.PersistentEphemeralNode.Mode;
import org.apache.curator.test.KillSession;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;

import java.io.Closeable;
import java.util.concurrent.TimeUnit;

/**
 * Created by jeffwan on 12/9/16.
 */
public class PersistentEphemeralNodeSample {

    private static final String PATH = "/example/ephemeralNode";
    private static final String PATH2 = "/example/node";



    public static void main(String[] args) {
        CuratorFramework client = ZookeeperClientHelper.createCuratorFrameworkClient();
        client.start();

        PersistentEphemeralNode node = null;

        try {
            node = new PersistentEphemeralNode(client, Mode.EPHEMERAL, PATH, "test".getBytes());
            node.start();

            node.waitForInitialCreate(3, TimeUnit.SECONDS);
            String actualPath = node.getActualPath();
            System.out.println("node " + actualPath + " value : " + new String(client.getData().forPath(actualPath)));

            client.create().withMode(CreateMode.PERSISTENT).forPath(PATH2, "persistent node".getBytes());
            System.out.println("node " + PATH2 + " value: " + new String(client.getData().forPath(PATH2)));
            KillSession.kill(client.getZookeeperClient().getZooKeeper(), ZookeeperClientHelper.connectionString);
            System.out.println("node " + actualPath + " doesn't exist: " + (client.checkExists().forPath(actualPath) == null));
            System.out.println("node " + PATH2 + " value: " + new String(client.getData().forPath(PATH2)));

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(node);
            CloseableUtils.closeQuietly(client);
        }
    }

}
