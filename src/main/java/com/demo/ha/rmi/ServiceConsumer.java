package com.demo.ha.rmi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event;
import org.apache.zookeeper.ZooKeeper;

public class ServiceConsumer 
{
	private ZkClient zkClient;	
	private volatile List<String> rmis = new ArrayList<String>();
	private volatile List<String> urlList = new ArrayList<>();
	private CountDownLatch latch = new CountDownLatch(1);
	public ServiceConsumer()
	{
		//初始化zk链接
		zkClient = new ZkClient(new ZkConnection(Constant.ZK_CONNECTION_STRING, Constant.ZK_SESSION_TIMEOUT));
		if(!zkClient.exists(Constant.ZK_REGISTRY_PATH))
		{
			
				zkClient.createPersistent(Constant.ZK_REGISTRY_PATH);
		}
		
		rmis = getChilds(Constant.ZK_REGISTRY_PATH,zkClient.getChildren(Constant.ZK_REGISTRY_PATH));
		
		/*
		 * ZooKeeper zk = connectServer(); // 连接 ZooKeeper 服务器并获取 ZooKeeper 对象 if (zk !=
		 * null ) { watchNode(zk); // 观察 /registry 节点的所有子节点并更新 urlList 成员变量 }
		 */
	}
	
	public String getUrl()
	{
		int index = new Random().nextInt(urlList.size());
		return urlList.get(index);
	}
	
	public Remote lookUp()
	{
		Remote remote = null;
		try 
		{
			int index = new Random().nextInt(rmis.size());
			String rmiStr = rmis.get(index);
			System.out.println(rmiStr);
		    remote = Naming.lookup(rmiStr);
		} 
		catch (MalformedURLException | RemoteException | NotBoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return remote;
	}
	
	
	public void watchNode()
	{
		try 
		{
			//CountDownLatch latch = new CountDownLatch(1);
			//IZkChildListener iZKChildListener = 
			zkClient.subscribeChildChanges(Constant.ZK_REGISTRY_PATH, new IZkChildListener()
			{
				@Override
				public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception 
				{
					urlList = currentChilds;
				//	latch.countDown();
					
				}
			});
			//latch.await();
			
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private List<String> getChilds(String parentPath, List<String> currentChilds)
	{
		List<String> childs = new ArrayList<String>();
		for(String data:currentChilds)
		{
			String rmi = zkClient.readData(parentPath+"/"+data);
			childs.add(rmi);
		}
		return childs;
	}
	
	private ZooKeeper connectServer() 
	{
        ZooKeeper zk = null ;
        try {
            zk = new ZooKeeper(Constant.ZK_CONNECTION_STRING, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown(); // 唤醒当前正在执行的线程
                    }
                }
            });
            latch.await(); // 使当前线程处于等待状态
        } catch (IOException | InterruptedException e) {
          //  LOGGER.error( "" , e);
        	System.out.println(e);
        }
        return zk;
    }
	
	 private void watchNode( final ZooKeeper zk) {
         try {
             List<String> nodeList = zk.getChildren(Constant.ZK_REGISTRY_PATH, new Watcher() {
                 @Override
                 public void process(WatchedEvent event) {
                     if (event.getType() == Event.EventType.NodeChildrenChanged) {
                         watchNode(zk); // 若子节点有变化，则重新调用该方法（为了获取最新子节点中的数据）
                     }
                     latch.countDown();
                 }
             });
             latch.await(); 
             List<String> dataList = new ArrayList<>(); // 用于存放 /registry 所有子节点中的数据
             for (String node : nodeList) {
                 byte [] data = zk.getData(Constant.ZK_REGISTRY_PATH + "/" + node, false , null ); // 获取 /registry 的子节点中的数据
                 dataList.add( new String(data));
             }
             System.out.println( "node data: {}"+dataList);
             urlList = dataList; // 更新最新的 RMI 地址
         } 
         catch (Exception e) 
         {
        	 System.out.println(e);
         }
     }

}
