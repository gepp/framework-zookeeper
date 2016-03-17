package com.jdk2010.framework.zookeeper.exception;

public class MyZookeeperException extends RuntimeException {

    private static final long serialVersionUID = 229502672931905609L;

    public MyZookeeperException(String message) {
        super(message);
    }

    public MyZookeeperException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyZookeeperException(Exception e) {
        super(e);
    }
}
