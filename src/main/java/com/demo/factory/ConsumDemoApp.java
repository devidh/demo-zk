package com.demo.factory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.boot.SpringApplication;

public class ConsumDemoApp 
{
	public static void main(String[] args) 
	{
		SpringApplication.run(ConsumDemoApp.class, args);
		ExecutorService executor = Executors.newFixedThreadPool(1);
		Consumers consumer = new Consumers();
		executor.submit(consumer);
		//executor.submit(producter);
		//executor.submit(producter);
		
		
	}
}
