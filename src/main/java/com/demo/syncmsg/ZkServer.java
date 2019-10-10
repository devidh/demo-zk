package com.demo.syncmsg;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;

public class ZkServer 
{
	public static void main(String[] args) throws InterruptedException
	{
       // SpringApplication.run(ZookeeperApiDemoApplication.class, args);
		SpringApplication.run(ZkServer.class, args);
		
		ZkConfigMng zkConfigMng = new ZkConfigMng();
		zkConfigMng.initConfig(null);
		zkConfigMng.syncConfigToZookeeper();
		TimeUnit.SECONDS.sleep(10);
		
		// 修改值
		zkConfigMng.updateConfig(new DatabaseConfig("com.mysql.jdbc.Driver","jdbc:mysql://127.0.0.1:3306/zkdata?useUnicode=true&characterEncoding=utf-8",
				"root", "root"));
	}

}
