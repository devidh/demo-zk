package com.demo.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RmiServer 
{
	public static void main(String[] args) throws RemoteException, MalformedURLException 
	{
		int port = 1098;
        String url = "rmi://localhost:1098/HelloServiceImpl";
        LocateRegistry.createRegistry(port);
        Naming.rebind(url, new HelloServiceImpl());
	}

}
