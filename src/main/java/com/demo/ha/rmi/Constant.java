package com.demo.ha.rmi;

public interface Constant
{
	String ZK_CONNECTION_STRING = "127.0.0.1:2181";
    int ZK_SESSION_TIMEOUT = 5000;
    String ZK_REGISTRY_PATH = "/registry";
    String ZK_PROVIDER_PATH = "/provider";
}
