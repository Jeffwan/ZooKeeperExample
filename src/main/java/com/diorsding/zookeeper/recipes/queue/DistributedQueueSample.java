package com.diorsding.zookeeper.recipes.queue;

import com.diorsding.zookeeper.helper.ZookeeperClientHelper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.queue.DistributedQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.recipes.queue.QueueSerializer;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

/**
 * Created by jeffwan on 12/9/16.
 *
 *
 * Single Consumer - lock is not needed
 *
 * Multiple Consumer - need lock on queue resource to avoid different consumers
 * consume the same resource at the same time.
 *
 * Consider to use other tools for distributed queue.
 * https://cwiki.apache.org/confluence/display/CURATOR/TN4
 *
 */
public class DistributedQueueSample {

    public static void main(String[] args) {
        CuratorFramework client = createCuratorFrameworkClient();
        QueueConsumer<String> consumer = createQueueConsumer();
        QueueSerializer<String> serializer = createQueueSerializer();
        String path = "/queue";

        QueueBuilder<String> builder = QueueBuilder.builder(client, consumer, serializer, path);
        DistributedQueue<String> queue = builder.buildQueue();

        try {
            client.start();
            queue.start();

            for (int i = 0; i < 10; i++) {
                queue.put(" test- " + i);
                Thread.sleep((long)(3 * Math.random()));
            }

            Thread.sleep(20000);

        } catch (Exception ex) {

        } finally {
            CloseableUtils.closeQuietly(queue);
            CloseableUtils.closeQuietly(client);
        }
    }

    private static QueueSerializer<String> createQueueSerializer() {
        return new QueueSerializer<String>() {
            public byte[] serialize(String s) {
                return s.getBytes();
            }

            public String deserialize(byte[] bytes) {
                return new String(bytes);
            }
        };
    }

    private static QueueConsumer<String> createQueueConsumer() {
        return new QueueConsumer<String>() {
            public void consumeMessage(String message) throws Exception {
                System.out.println("consume one message: " + message);
            }

            public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
                System.out.println("connection new state: " + connectionState.name());
            }
        };
    }

    private static CuratorFramework createCuratorFrameworkClient() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(ZookeeperClientHelper.connectionString, retryPolicy);

        client.getCuratorListenable().addListener(new CuratorListener() {
            public void eventReceived(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                System.out.println("CuratorEvent: " + curatorEvent.getType().name());
            }
        });
        return client;
    }
}
