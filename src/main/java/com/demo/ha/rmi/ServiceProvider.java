package com.demo.ha.rmi;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

public class ServiceProvider 
{
	private ZkClient zkClient;	
	//初始化zk链接
	public ServiceProvider()
	{
		zkClient = new ZkClient(new ZkConnection(Constant.ZK_CONNECTION_STRING, Constant.ZK_SESSION_TIMEOUT));
		if(!zkClient.exists(Constant.ZK_REGISTRY_PATH))
		{
			zkClient.createPersistent(Constant.ZK_REGISTRY_PATH);
		}		
	}
	
	
	public void publishService(String ip,int port,Remote remote) throws RemoteException, MalformedURLException, AlreadyBoundException
	{
		//发布rmi
		String url = publishRmi(ip,port,remote);
		//zk注册rmi
		registRmi(url);
	}
	
	
	//发布RMI服务
	private String publishRmi(String ip,int port,Remote remote) throws RemoteException, MalformedURLException, AlreadyBoundException
	{
		String url = String.format("rmi://%s:%d/%s", ip,port,remote.getClass().getName());
		LocateRegistry.createRegistry(port);
		Naming.bind(url, remote);
		System.out.println(String.format("publish rmi service url:{%s}",url));
		return url;
	}
	
	//zk注册rmi服务地址
	private void registRmi(String url)
	{
		String path = zkClient.createEphemeralSequential(Constant.ZK_REGISTRY_PATH+Constant.ZK_PROVIDER_PATH, url);
		System.out.println(String.format("ZK register node rmi path:{%s}",path));
	}

}
