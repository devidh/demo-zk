package com.demo.syncmsg;

import java.io.Serializable;

public class DatabaseConfig implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String driverClass;
	private String dbUrl;
	private String username;
	private String password;
	
	
	public DatabaseConfig(String driverClass,String dbUrl,String username,String password)
	{
		this.dbUrl = dbUrl;
		this.driverClass=driverClass;
		this.username=username;
		this.password=password;
	}
	
	public String getDriverClass() {
		return driverClass;
	}
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDbUrl() {
		return dbUrl;
	}
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
	
	@Override
	public String toString() {
		return "BatabaseConfig [driverClass=" + driverClass + ", username=" + username + ", password=" + password
				+ ", dbUrl=" + dbUrl + "]";
	}
	

}
