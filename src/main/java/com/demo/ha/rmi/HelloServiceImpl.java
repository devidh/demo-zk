package com.demo.ha.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloServiceImpl extends UnicastRemoteObject implements HelloService
{

	protected HelloServiceImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String sayHello(String name) 
	{
	    return String.format("Hello %s", name);
	}

}
