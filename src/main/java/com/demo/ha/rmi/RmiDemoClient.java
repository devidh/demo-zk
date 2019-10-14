package com.demo.ha.rmi;

import java.rmi.RemoteException;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;

public class RmiDemoClient 
{
	public static void main(String[] args) throws RemoteException, InterruptedException 
	{
		SpringApplication.run(RmiDemoClient.class, args);
		ServiceConsumer serviceConsumer = new ServiceConsumer();
		serviceConsumer.watchNode();
		while(true)
		{
			//System.out.println(serviceConsumer.getUrl());
			HelloService remot= (HelloService)serviceConsumer.lookUp();
			System.out.println(remot.sayHello("Jack"));
			TimeUnit.SECONDS.sleep(3);
		}
		
	}
	
	
	
	

}
