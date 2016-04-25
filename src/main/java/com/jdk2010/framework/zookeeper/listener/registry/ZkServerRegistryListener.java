package com.jdk2010.framework.zookeeper.listener.registry;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CreateBuilder;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自动重新注册
 *
 */
public class ZkServerRegistryListener implements ConnectionStateListener {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private String zkRegPathPrefix;
    private String regContent;

    public ZkServerRegistryListener(String zkRegPathPrefix, String regContent) {
        this.zkRegPathPrefix = zkRegPathPrefix;
        this.regContent = regContent;
    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState connectionState) {
        if (connectionState == ConnectionState.LOST) {
            while (true) {
                try {
                    if (client.blockUntilConnected(5, TimeUnit.SECONDS)) {
                        CreateBuilder builder = client.create();
                        builder.withMode(CreateMode.EPHEMERAL_SEQUENTIAL);
                        builder.creatingParentsIfNeeded();
                        builder.forPath(zkRegPathPrefix, regContent.getBytes());
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    logger.error("listen throws exception:"+e.getMessage());
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("listen throws exception:"+e.getMessage());
                    break;
                }
            }
        }
        
    }
}