package org.kana.rockhopper.chefapi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.SystemUtils;
import org.kana.rockhopper.ConfigurationUtil;

public class ChefBootstrapper {
	
	String finalRunList = "";
	
	private static final String COMMAND = "cd %s && knife bootstrap %s --sudo -x %s -P %s";
	private static final String COMMAND_WITH_RUNLIST = COMMAND+" -r %s";
	
	public void bootstrapLinuxNode(String ip, String userName, String password) {
		try {
			String command = COMMAND;
			
			if(SystemUtils.IS_OS_WINDOWS) {
				command = "cmd /c "+command;
			}
			
			String tStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
			.format(new java.util.Date());
			File fout = new File(ConfigurationUtil.getKey("deployer.logs.dir") + "/" + tStamp + ".txt");
			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			
			Process p = Runtime.getRuntime().exec(String.format(command, ConfigurationUtil.getKey("deployer.chef.work"), ip, userName, password));
			p.waitFor();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				bw.write(line);
				bw.newLine();
			}
			bw.close();

		} catch (Exception e) {

		}
	}
	
	public void bootstrapLinuxNodeWithRunList(String ip, String userName, String password,String runlist, String runlistItem ) {
		
		try {
			
			if ((runlist == null || runlist.isEmpty()) || (runlistItem == null || runlistItem.isEmpty())) {
				bootstrapLinuxNode(ip, userName, password);
				return;
			}
			
			File fout = new File(ConfigurationUtil.getKey("deployer.logs.dir") + "log-" + Math.random()*10 + ".txt");
			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		 
			if (runlist.equalsIgnoreCase("Cookbooks"))
				
			{
				finalRunList = "\'recipe[" + runlistItem + "]\'";
				System.out.println(finalRunList);
			}
			
			else if(runlist.equalsIgnoreCase("Roles")){
				
				finalRunList = "\'role[" + runlistItem + "]\'";
			}
			
			String command = COMMAND_WITH_RUNLIST;
			
			if(SystemUtils.IS_OS_WINDOWS) {
				command = "cmd /c "+command;
			}
			
			Process p = Runtime.getRuntime().exec(String.format(command, ConfigurationUtil.getKey("deployer.chef.work"), ip, userName, password,finalRunList));
			p.waitFor();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				bw.write(line);
				bw.newLine();
			}
			bw.close();
		} catch (Exception e) {

	
		}
	}
	
}
