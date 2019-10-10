package com.demo.distrilock;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;

public class ZkDemoApp
{
	public static void main(String[] args) throws InterruptedException 
	{
		SpringApplication.run(ZkDemoApp.class, args);
		
		DistributedLock distributedLock = new DistributedLock();
		String lockName = distributedLock.getLock();
		if(lockName!=null)
		{
			TimeUnit.SECONDS.sleep(20);
			distributedLock.releaseLock(lockName);
		}
		distributedLock.closeZkClient();
		
	}

}
