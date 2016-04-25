package com.jdk2010.framework.zookeeper.test;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jdk2010.framework.zookeeper.client.ZookeeperClient;

public class Main {
    BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
    ZookeeperClient client = factory.getBean(ZookeeperClient.class);
    InterProcessMutex lock = new InterProcessMutex(client.getClient(), "/global_lock");

    public void test1() throws InterruptedException {
        
        client.createConfig("/gpp/test", "我的");
        while(true){
            Thread.sleep(1000);
        }
    }

    public void testLock() {
        Executor pool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            pool.execute(new Runnable() {
                public void run() {
                    try {
                        lock.acquire();
                        System.out.println(Thread.currentThread().getName());
                        TimeUnit.SECONDS.sleep(5);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            lock.release();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Main main=new Main();
        main.test1();
//        main.testLock();
    }
}
