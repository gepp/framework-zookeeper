package com.jdk2010.framework.zookeeper.listener.discovery;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.utils.ZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZkClientDiscoveryListener implements PathChildrenCacheListener {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
        System.out.println("================== catch children change ==================");
        System.out.println("===" + event.getType() + "," + event.getData().getPath() + "," + event.getData().getData());
        switch (event.getType()) {
            case CHILD_ADDED: {
                System.out.println("Node added: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                break;
            }
            case CHILD_UPDATED: {
                System.out.println("Node changed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                break;
            }
            case CHILD_REMOVED: {
                System.out.println("Node removed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                break;
            }

        }
    }

}
