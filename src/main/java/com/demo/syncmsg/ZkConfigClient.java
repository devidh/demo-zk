package com.demo.syncmsg;

import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

public class ZkConfigClient implements Runnable
{
    private String nodePath="/databaseInfo";
	
	private DatabaseConfig databaseConfig;

	public void run() 
	{
		ZkClient zkclient = new ZkClient(new ZkConnection("127.0.0.1:2181", 5000));
		while(!zkclient.exists(nodePath))
		{
			System.out.println("配置节点不存在！");
			
			try 
			{
				TimeUnit.SECONDS.sleep(1);
				
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		
		//获取节点
		databaseConfig = (DatabaseConfig)zkclient.readData(nodePath);
		System.out.println(databaseConfig.toString());
		
		zkclient.subscribeDataChanges(nodePath, new IZkDataListener() {

			public void handleDataChange(String dataPath, Object data) throws Exception
			{
				if(dataPath.equals(nodePath)) {
					System.out.println("节点：" + dataPath + ", 数据：" + data + " - 更新");
					databaseConfig = (DatabaseConfig) data;
				}
				
			}

			public void handleDataDeleted(String dataPath) throws Exception
			{
				if(dataPath.equals(nodePath)) {
					System.out.println("节点：" + dataPath + "被删除了！");
				}
				
			}
			
		});
	}
}
