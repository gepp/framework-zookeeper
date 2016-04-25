package com.jdk2010.framework.zookeeper.test;

import java.util.List;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jdk2010.framework.zookeeper.client.ZookeeperClient;

public class Client {

   

    public static void main(String[] args) throws InterruptedException {
        BeanFactory factory = new ClassPathXmlApplicationContext("client.xml");
       
        while(true){
            Thread.sleep(3000);
            ZookeeperClient client = factory.getBean(ZookeeperClient.class);
            String serverName=client.getServerNameByType("1");
            System.out.println("serverName:================="+serverName);
        }
    }
}
