package com.demo.balance;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

public class ServiceProvider 
{
	// 静态常量
	private static String ZK_CONNECT_STR = "127.0.0.1:2181";
	private static String NODE_PATH = "/service";
	private static String SERIVCE_NAME = "/myService";
	private ZkClient zkClient;
	
	public ServiceProvider()
	{
		zkClient = new ZkClient(new ZkConnection(ZK_CONNECT_STR));
		if(!zkClient.exists(NODE_PATH))
		{
			zkClient.createPersistent(NODE_PATH);
		}
	}
	
	public void registryService(String localIP,Object obj)
	{
		if(!zkClient.exists(NODE_PATH+SERIVCE_NAME))
		{
			zkClient.createPersistent(NODE_PATH+SERIVCE_NAME);
		}
		zkClient.createEphemeral(NODE_PATH+SERIVCE_NAME+"/"+localIP, obj);
		System.out.println(localIP+"注册成功！");
	}
}
