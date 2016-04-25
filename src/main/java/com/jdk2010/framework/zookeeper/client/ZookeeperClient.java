package com.jdk2010.framework.zookeeper.client;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;

public interface ZookeeperClient {
    /**
     * 获取配置
     */
    public String getConfig(String path);

    /**
     * 创建配置
     */
    public boolean createConfig(String path, String value);

    /**
     * 创建配置 type true 默认true 永久节点 false 虚拟节点
     */
    public boolean createConfig(String path, String value, boolean type);

    /**
     * 修改配置
     */
    public void updateConfig(String path, String value);

    /**
     * 删除配置
     */
    public void deleteConfig(String path);

    /**
     * 是否存在
     */
    public boolean isExistsNode(String path);

    /**
     * 获取子节点
     */
    public List<String> getChildList(String path);

    /**
     * 注册服务
     */
    public boolean register(String regContent);

    /**
     * 发现服务
     * 
     * @return
     */
    public String discovery();

    public CuratorFramework getClient();

    /**
     * 客户端获取所有的服务器列表
     * @return
     */
    public String getServerNameByType(String type);
}
