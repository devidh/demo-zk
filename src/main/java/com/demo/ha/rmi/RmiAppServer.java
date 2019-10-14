package com.demo.ha.rmi;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import org.springframework.boot.SpringApplication;

public class RmiAppServer 
{
	public static void main(String[] args)
	{
		SpringApplication.run(RmiAppServer.class, args);
		try
		{
			HelloService helloService = new HelloServiceImpl();
			ServiceProvider serviceProvider = new ServiceProvider();
			serviceProvider.publishService("127.0.0.1", 12012, helloService);
		} 
		catch (RemoteException | MalformedURLException | AlreadyBoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
