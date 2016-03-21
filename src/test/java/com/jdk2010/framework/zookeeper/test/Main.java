package com.jdk2010.framework.zookeeper.test;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jdk2010.framework.zookeeper.client.ZookeeperClient;

public class Main {
    
    
    public static void main(String[] args) {
        BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
        ZookeeperClient client=factory.getBean(ZookeeperClient.class);
        client.createConfig("/gpp/test", "我的");
        while(true);
    }
}
