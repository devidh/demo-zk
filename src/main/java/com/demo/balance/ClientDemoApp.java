package com.demo.balance;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;

public class ClientDemoApp
{
	public static void main(String[] args) throws InterruptedException
	{
		SpringApplication.run(ClientDemoApp.class, args);
		ServiceConsumer serviceConsumer = new ServiceConsumer();
		serviceConsumer.subscribeService();
		
		while(true)
		{
			serviceConsumer.consume();
			TimeUnit.SECONDS.sleep(10);
		}
		
	}

}
