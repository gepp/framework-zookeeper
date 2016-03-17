package com.jdk2010.framework.zookeeper.client;

import java.util.List;

public interface ZookeeperClient {
    /**
     * 获取配置
     */
    public String getConfig(String path) ;

    /**
     * 创建配置
     */
    public boolean createConfig(String path, String value) ;

    /**
     * 修改配置
     */
    public void updateConfig(String path, String value) ;

    /**
     * 删除配置
     */
    public void deleteConfig(String path) ;

    /**
     * 是否存在
     */
    public boolean isExistsNode(String path) ;
    
    /**
     * 获取子节点
     */
    public List<String> getChildList(String path) ;
    
}
