package com.demo.syncmsg;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.boot.SpringApplication;

public class ZkClient 
{
	public static void main(String[] args) 
	{
		SpringApplication application = new SpringApplication(ZkClient.class);
		//application.setBanner(banner);
		//application.setBannerMode(Banner.Mode.OFF);
		application.run(args);
		
		//SpringApplication.run(ZkClient.class, args);
		 
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		// 模拟多个客户端获取配置
		executorService.submit(new ZkConfigClient());
		executorService.submit(new ZkConfigClient());
		executorService.submit(new ZkConfigClient());
	}

}
