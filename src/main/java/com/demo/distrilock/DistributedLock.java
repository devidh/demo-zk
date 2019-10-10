package com.demo.distrilock;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

public class DistributedLock
{
	//常量
	private static final int SESSION_TIMEOUT=10000;
	
	private static final String CONNECTION_STRING="127.0.0.1:2181";
	
	private static final String LOCK_NODE="/distributed_lock";
	
	private static final String CHILDREN_NODE="/task_";
	
	private ZkClient zkClient;
	
	//初始化zk链接,创建分布式锁节点
	public DistributedLock() 
	{
		zkClient = new ZkClient(new ZkConnection(CONNECTION_STRING),SESSION_TIMEOUT);
		if(!zkClient.exists(LOCK_NODE))
		{
			zkClient.createPersistent(LOCK_NODE);
		}
	}
	
	//获取分布式所节点
	public String getLock()
	{
		try 
		{
			//在分布式所节点下常见临时序列节点
			String lockName = zkClient.createEphemeralSequential(LOCK_NODE+CHILDREN_NODE, "");
			//尝试获取锁
			acquireLock(lockName);
			return lockName;
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//获取锁
	private Boolean acquireLock(String lockName) throws InterruptedException
	{
		//获取所有分布式锁节点下的子节点
		List<String> childrenNodes = zkClient.getChildren(LOCK_NODE);
		
		//lambda表达式
		childrenNodes.sort((node1,node2)->Integer.valueOf(node1.split("_")[1]).compareTo(Integer.valueOf(node2.split("_")[1])));
		
		//当前创建节点是不是第一位
		int lockPostion = childrenNodes.indexOf(lockName.split("/")[lockName.split("/").length-1]);
		if(lockPostion<0)
		{
			//不存在该节点
		    throw new ZkNodeExistsException("不存在的节点：" + lockName);
		}
		else if(lockPostion==0)
		{
			// 获取到锁
			System.out.println("获取到锁：" + lockName);
			return true;
		}
		else if(lockPostion > 0)
		{
			//为获取锁
			System.out.println("...... 未获取到锁，阻塞等待 。。。。。。");
			//如果为获取锁，创建当前创建节点的前一位
			final CountDownLatch latch = new CountDownLatch(1);
			
			IZkDataListener listener = new IZkDataListener() {
				@Override
				public void handleDataChange(String dataPath, Object data) throws Exception {
				}
				@Override
				public void handleDataDeleted(String dataPath) throws Exception 
				{
					System.out.println(dataPath +"节点被删除   。。。。。。");
					acquireLock(lockName);
					latch.countDown();
				}
				
			};
			zkClient.subscribeDataChanges(LOCK_NODE+"/"+childrenNodes.get(lockPostion-1), listener);
			latch.await();
		}
		return false;
	}
	
	//释放锁
	public void releaseLock(String lockName)
	{
		zkClient.delete(lockName);
		
	}
	
	public void closeZkClient()
	{
		zkClient.close();
	}
	

}
