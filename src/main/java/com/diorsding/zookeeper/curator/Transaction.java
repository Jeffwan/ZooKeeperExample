package com.diorsding.zookeeper.curator;

import com.diorsding.zookeeper.constants.Constants;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.transaction.CuratorTransaction;
import org.apache.curator.framework.api.transaction.CuratorTransactionFinal;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Op;

import java.util.Collection;

/**
 * Created by jeffwan on 12/9/16.
 */
public class Transaction {

    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 3);

        CuratorFramework client = CuratorFrameworkFactory.newClient(Constants.connectionString, retryPolicy);
        client.start();

        // Option 1.
        transaction(client);

        // Option 2.
        CuratorTransaction curatorTransaction = startTransaction(client);
        addCreateToTransaction(curatorTransaction);
        CuratorTransactionFinal transactionFinal = addDeleteToTransaction(curatorTransaction);
        commitTransaction(transactionFinal);

    }

    public static Collection<CuratorTransactionResult> transaction(CuratorFramework client) throws Exception {
        Collection<CuratorTransactionResult> results = client.inTransaction()
                .create().withMode(CreateMode.PERSISTENT).forPath("/a", "apath".getBytes())
                .and().setData().forPath("/a", "bpath".getBytes())
                .and().delete().forPath("/a")
                .and().commit();

        for (CuratorTransactionResult result : results) {
            System.out.println(result.getForPath() + " - " + result.getType());
        }

        return results;
    }

    /*
	 * These next four methods show how to use Curator's transaction APIs in a
	 * more traditional - one-at-a-time - manner
	 */
    public static CuratorTransaction startTransaction(CuratorFramework client) {
        // start the transaction builder
        return client.inTransaction();
    }
    public static CuratorTransactionFinal addCreateToTransaction(CuratorTransaction transaction) throws Exception {
        // add a create operation
        return transaction.create().forPath("/a", "some data".getBytes()).and();
    }
    public static CuratorTransactionFinal addDeleteToTransaction(CuratorTransaction transaction) throws Exception {
        // add a delete operation
        return transaction.delete().forPath("/a").and();
    }
    public static void commitTransaction(CuratorTransactionFinal transaction) throws Exception {
        // commit the transaction
        Collection<CuratorTransactionResult> commitResults = transaction.commit();

        for (CuratorTransactionResult result : commitResults) {
            System.out.println(result.getForPath() + " - " + result.getType());
        }
    }

}
