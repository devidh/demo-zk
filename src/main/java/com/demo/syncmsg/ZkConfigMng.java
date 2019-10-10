package com.demo.syncmsg;

import org.I0Itec.zkclient.ZkClient;

public class ZkConfigMng 
{
	private String nodePath="/databaseInfo";
	
	private DatabaseConfig databaseConfig;
	
	private ZkClient zkClient;
	
	public DatabaseConfig initConfig(DatabaseConfig databaseConfig)
	{
		if(databaseConfig == null)
		{
			this.databaseConfig = new DatabaseConfig("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/mysql?useUnicode=true&characterEncoding=utf-8", "root", "root");
		}
		else
		{
			this.databaseConfig = databaseConfig;
		}
		return this.databaseConfig;
	}
	
	public DatabaseConfig updateConfig(DatabaseConfig databaseConfig)
	{
		if(databaseConfig != null)
		{
			this.databaseConfig = databaseConfig;
		}
		syncConfigToZookeeper();
		return this.databaseConfig;
	}
	
	
	public void syncConfigToZookeeper()
	{
		if(zkClient == null) 
		{
			zkClient = new ZkClient("127.0.0.1:2181");
		}
		
		if(!zkClient.exists(nodePath))
		{
			zkClient.createPersistent(nodePath);
		}
		
		zkClient.writeData(nodePath, databaseConfig);
	}
}
