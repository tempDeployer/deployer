package org.kana.rockhopper.chefapi;

import java.io.IOException;

public class ChefBoostrapperService implements Runnable{

	private String logFilePath;
	private String ip;
	private String userName;
	private String password;
	private String roles;
	private String cookbooks;
	public ChefBoostrapperService(String logPath, String ip, String usename, String pass, String roles, String cookbooks)
	{
		this.logFilePath = logPath;
		this.ip = ip;
		this.userName = usename;
		this.password = pass;
		this.cookbooks = cookbooks;
		this.roles = roles;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ChefBootstrapper bootstrapper = new ChefBootstrapper();
		try {
			bootstrapper.bootstrapLinuxNodeWithRunList(ip, userName, password, cookbooks, roles, logFilePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
