package com.demo.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RmiClient 
{
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException 
	{
		String url = "rmi://localhost:1098/HelloServiceImpl";
        HelloService helloService = (HelloService) Naming.lookup(url);
        String result = helloService.sayHello("abc");
        System.out.println(result);
	}

}
