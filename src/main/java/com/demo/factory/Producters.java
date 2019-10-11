package com.demo.factory;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

public class Producters implements Runnable
{
	private static final String ZK_CONNECT_STR = "127.0.0.1:2181";
	
	private static final String NODE_PATH = "/mailbox";
	
	private static final String CHILD_NODE_PATH = "/mail_";
	
	private static final int SESSION_OUT = 3000;
	
	private static final int MAILBOX_SIZE = 20;
	
	private ZkClient zkClient;
	
	//初始化
	public Producters()
	{
		zkClient = new ZkClient(new ZkConnection(ZK_CONNECT_STR,SESSION_OUT));
		if(!zkClient.exists(NODE_PATH))
		{
			zkClient.createPersistent(NODE_PATH);
		}
	}
	
	@Override
	public void run() 
	{
		sendMail();
		//监听节点变化
		IZkChildListener iZkChildListener = new IZkChildListener() {
			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				System.out.println("mailBox listener num:"+zkClient.numberOfListeners());
				sendMail();
			}
		};
		zkClient.subscribeChildChanges(NODE_PATH, iZkChildListener);
		
	}
	
	
	public void sendMail()
	{
		//获取子节点列表数据量
		int mailNum = zkClient.getChildren(NODE_PATH).size();
		if(mailNum<MAILBOX_SIZE)
		{
			// 发送邮件
			String cretePath = zkClient.createEphemeralSequential(NODE_PATH + CHILD_NODE_PATH, "your mail");
			System.out.println("your mail has been send:" + cretePath);
			// 模拟随机间隔的发送邮件(0-10S)
			try 
			{
				TimeUnit.MILLISECONDS.sleep(new Random().nextInt(1000));
			} 
			catch (InterruptedException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("mailbox size: " + mailNum +",main box is full!");
		}
		
	}
	
}