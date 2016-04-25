package com.jdk2010.framework.zookeeper.entity;

import java.util.Set;

public class Znode {
    
    private Znode root;
    private String data;
    private Set<Znode> subNode;
    private String pathName;
    private String nodeName;

    public Znode getRoot() {
        return root;
    }

    public void setRoot(Znode root) {
        this.root = root;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Set<Znode> getSubNode() {
        return subNode;
    }

    public void setSubNode(Set<Znode> subNode) {
        this.subNode = subNode;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

}
