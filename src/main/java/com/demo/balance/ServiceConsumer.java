package com.demo.balance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.zookeeper.CreateMode;

public class ServiceConsumer 
{
	// 静态常量
	private static String ZK_CONNECT_STR = "127.0.0.1:2181";
	private static String NODE_PATH = "/service";
	private static String SERIVCE_NAME = "/myService";
	private ZkClient zkClient;
	private List<String> serviceList = new ArrayList<String>();
	
	public ServiceConsumer()
	{
		zkClient = new ZkClient(new ZkConnection(ZK_CONNECT_STR));
		System.out.println("sucess connected to zookeeper server!");
		// 不存在就创建NODE_PATH节点
		if(!zkClient.exists(NODE_PATH))
		{
			zkClient.create(NODE_PATH, "this is mailbox", CreateMode.PERSISTENT);
		}
	}
	
	public void subscribeService()
	{
		serviceList = zkClient.getChildren(NODE_PATH+SERIVCE_NAME);
		zkClient.subscribeChildChanges(NODE_PATH+SERIVCE_NAME,new IZkChildListener() {
			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception
			{
				serviceList=currentChilds;
				
			}
			
		});
	}
	
	public void consume()
	{
		//负载均衡算法获取某台机器调用服务
		int index = new Random().nextInt(serviceList.size());
		String url = zkClient.readData(NODE_PATH + SERIVCE_NAME +"/"+serviceList.get(index));
		System.out.println("调用[" + NODE_PATH + SERIVCE_NAME +"/"+serviceList.get(index)+"]服务：" +url);
	}

}
