package com.demo.balance;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;

public class ServieDemoApp
{
	public static void main(String[] args) throws InterruptedException 
	{
		SpringApplication.run(ServieDemoApp.class, args);
		ServiceProvider serviceProvider = new ServiceProvider();
		serviceProvider.registryService("10.131.20.21", "http://localhost:2181/bquery");
		serviceProvider.registryService("10.131.20.22", "http://localhost:2181/bquery");
		serviceProvider.registryService("10.131.20.23", "http://localhost:2181/bquery");
		serviceProvider.registryService("10.131.20.24", "http://localhost:2181/bquery");
		TimeUnit.MINUTES.sleep(4);
		serviceProvider.registryService("10.131.21.10", "http://localhost:2181/bquery");
		serviceProvider.registryService("10.131.21.11", "http://localhost:2181/bquery");
		TimeUnit.MINUTES.sleep(6);
		
	}

}
