package com.demo.factory;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

public class Consumers implements Runnable
{
    private static final String ZK_CONNECT_STR = "127.0.0.1:2181";
	
	private static final String NODE_PATH = "/mailbox";
	
	private static final int SESSION_OUT = 3000;
	
	private ZkClient zkClient;
	
	//初始化
	public Consumers()
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
		receiveMail();
		try 
		{
			CountDownLatch latch = new CountDownLatch(1);
			//监听子节点变化
			IZkChildListener iZkChildListener = new IZkChildListener() 
			{
				@Override
				public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception
				{
					receiveMail();
					System.out.println("mailBox listener num:"+zkClient.numberOfListeners());
					latch.countDown();
				}
				
			};
			zkClient.subscribeChildChanges(NODE_PATH, iZkChildListener);
			latch.await();
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void receiveMail()
	{
		List<String> mails = zkClient.getChildren(NODE_PATH);
		if(mails.size()>0)
		{
			mails.sort((m01,m02)->Integer.parseInt(m01.split("_")[1])-Integer.parseInt(m02.split("_")[1]));
			// 接收邮件
			for(String mail:mails)
			{
				zkClient.delete(NODE_PATH +"/"+mail);
				System.out.println("your mail has been received:" + mail);
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
			
		}
		else
		{
			System.out.println("Cann't find new mails !");
		}
	}
}
