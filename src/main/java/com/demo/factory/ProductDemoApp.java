package com.demo.factory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.boot.SpringApplication;

public class ProductDemoApp 
{
	public static void main(String[] args) 
	{
		SpringApplication.run(ProductDemoApp.class, args);
		ExecutorService executor = Executors.newFixedThreadPool(1);
		Producters producter = new Producters();
		executor.submit(producter);
		//executor.submit(producter);
		//executor.submit(producter);
	}

}
