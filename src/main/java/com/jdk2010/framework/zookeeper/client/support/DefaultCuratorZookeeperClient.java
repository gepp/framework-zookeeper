package com.jdk2010.framework.zookeeper.client.support;

import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CreateBuilder;
import org.apache.curator.framework.api.DeleteBuilder;
import org.apache.curator.framework.api.ExistsBuilder;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.framework.api.SetDataBuilder;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import com.jdk2010.framework.zookeeper.client.ZookeeperClient;
import com.jdk2010.framework.zookeeper.exception.MyZookeeperException;
import com.jdk2010.framework.zookeeper.listener.ZkConnectionStateListener;

public class DefaultCuratorZookeeperClient implements ZookeeperClient, InitializingBean {
    
    Logger logger = LoggerFactory.getLogger(this.getClass());

    
    
    @Override
    public String getConfig(String path) {
        GetDataBuilder dataBuilder = client.getData();
        byte[] bytes;
        try {
            bytes = dataBuilder.forPath(path);
        } catch (Exception e) {
            if (e instanceof NoNodeException) {
                throw new MyZookeeperException(path + " 节点不存在!");
            } else {
                throw new MyZookeeperException(e);
            }

        }
        return new String(bytes);
    }

    @Override
    public boolean createConfig(String path, String value) {
        return createConfig(path, value, true);
    }

    @Override
    public boolean createConfig(String path, String value, boolean type) {
        boolean flag = true;
        CreateBuilder builder = client.create();
        if (type == false) {
            builder.withMode(CreateMode.EPHEMERAL_SEQUENTIAL);
        }
        builder.creatingParentsIfNeeded();
        try {
            builder.forPath(path, value.getBytes());
        } catch (Exception e) {
            if (e instanceof NodeExistsException) {
                flag = true;
            } else {
                throw new MyZookeeperException(e);
            }

        }
        return flag;
    }

    @Override
    public void updateConfig(String path, String value) {
        SetDataBuilder setDataBuilder = client.setData();
        try {
            setDataBuilder.withVersion(-1).forPath(path, value.getBytes());
        } catch (Exception e) {
            if (e instanceof NoNodeException) {
                throw new MyZookeeperException(path + " 节点不存在!");
            } else {
                throw new MyZookeeperException(e);
            }

        }
    }

    @Override
    public void deleteConfig(String path) {
        DeleteBuilder delteBuilder = client.delete();
        delteBuilder.guaranteed();// 保证节点必须删除，如果删除出现错误，则后台程序会不断去尝试删除。
        delteBuilder.deletingChildrenIfNeeded();// 如果存在子节点，先删除子节点
        delteBuilder.withVersion(-1);// 指定版本号
        try {
            delteBuilder.forPath(path);
        } catch (Exception e) {
            if (e instanceof NoNodeException) {
                throw new MyZookeeperException(path + " 节点不存在!");
            } else {
                throw new MyZookeeperException(e);
            }
        }// 指定路径
    }

    @Override
    public boolean isExistsNode(String path) {
        ExistsBuilder existsBuilder = client.checkExists();
        try {
            if (existsBuilder.forPath(path) != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new MyZookeeperException(e);
        }
    }

    @Override
    public List<String> getChildList(String path) {
        GetChildrenBuilder getChildBuilder = client.getChildren();
        List<String> nodelist;
        try {
            nodelist = getChildBuilder.forPath(path);
        } catch (Exception e) {
            if (e instanceof NoNodeException) {
                throw new MyZookeeperException(path + " 节点不存在!");
            } else {
                throw new MyZookeeperException(e);
            }
        }
        return nodelist;
    }
    
    @Override
    public boolean register(String regContent) {
        return createConfig(zkRegPathPrefix, regContent, false);

    }

    public String getZkHost() {
        return zkHost;
    }

    public void setZkHost(String zkHost) {
        this.zkHost = zkHost;
    }

    public Integer getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(Integer sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
    public String getRegisterHost() {
        return registerHost;
    }

    public void setRegisterHost(String registerHost) {
        this.registerHost = registerHost;
    }
     
    public String getZkRegPathPrefix() {
        return zkRegPathPrefix;
    }

    public void setZkRegPathPrefix(String zkRegPathPrefix) {
        this.zkRegPathPrefix = zkRegPathPrefix;
    }


    private String zkHost;
    
    private String zkRegPathPrefix;
    
    private String registerHost;

    private Integer sessionTimeout;

    private Integer connectionTimeout;

    private CuratorFramework client;

    
    public CuratorFramework getClient() {
        return client;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isEmpty(zkHost)) {
            throw new MyZookeeperException("zkHost不能为空！");
        }
        
        if (StringUtils.isEmpty(sessionTimeout)) {
            sessionTimeout = 5000;
        }
        if (StringUtils.isEmpty(connectionTimeout)) {
            connectionTimeout = 3000;
        }
        
        if(StringUtils.isEmpty(zkRegPathPrefix)){
            zkRegPathPrefix="/zkRegHosts/zkRegPathPrefix";
        }
        
        // 重试策略可以实现该接口。通过自定义规则，进行重试
        // 重试链接的策略的接口
        // (最开始时间间隔，最多重试次数) 随着次数递增，时间间隔还会增加
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        // (最多重试次数，每次重试间隔的时间长度)
        // RetryPolicy retryPolicy2 = new RetryNTimes(5, 1000);
        // (重试时间的总长度，每次重试的时间间隔) 这个基本可重试10次
        // RetryPolicy retryPolicy3 = new RetryUntilElapsed(10000, 1000);

       client = CuratorFrameworkFactory.builder()
       .connectString(zkHost)
       .sessionTimeoutMs(sessionTimeout)
       .connectionTimeoutMs(connectionTimeout)
       .canBeReadOnly(false)
       .retryPolicy(retryPolicy)
       .build();
       client.start();  
       
       //如果节点不为空,则注册中心节点
       if(!StringUtils.isEmpty(registerHost)){
           register(registerHost);
           ZkConnectionStateListener stateListener=new ZkConnectionStateListener(zkRegPathPrefix, registerHost);
           client.getConnectionStateListenable().addListener(stateListener);
           logger.info("向zookeeper中心注册节点: "+zkRegPathPrefix+" -->"+registerHost);
       }
      
       
    }

    
}
