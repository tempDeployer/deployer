package org.kana.rockhopper.chefapi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;

import org.kana.rockhopper.ConfigurationUtil;

public class ChefBootstrapper {
	
	String finalRunList = "";
	
	public void bootstrapLinuxNode(String ip, String userName, String password) {
		try {
			String tStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
			.format(new java.util.Date());
			File fout = new File(ConfigurationUtil.getKey("LOGS_DIR") + "/" + tStamp + ".txt");
			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			
			Process p = Runtime.getRuntime().exec(String.format("cmd /c cd %s && knife bootstrap %s --sudo -x %s -P %s", ConfigurationUtil.getKey("CHEF_WORKING_DIR"), ip, userName, password));
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
	
	public void bootstrapLinuxNode(String nodeName, String ip, String userName, String password) {
		try {

			Process p = Runtime.getRuntime().exec(String.format("cmd /c cd %s && knife bootstrap %s --sudo -x %s -P %s -N %s", ConfigurationUtil.getKey("CHEF_WORKING_DIR"), ip, userName, password, nodeName));
			p.waitFor();
			
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;
			
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			
			}

		} catch (Exception e) {

		}
	}
	public void bootstrapLinuxNodeWithRunList(String ip, String userName, String password,String runlist, String runlistItem ) {
		
		try {
			
			if ((runlist == null || runlist.isEmpty()) || (runlistItem == null || runlistItem.isEmpty())) {
				bootstrapLinuxNode(ip, userName, password);
				return;
			}
			
			File fout = new File(ConfigurationUtil.getKey("LOGS_DIR") + "log-" + Math.random()*10 + ".txt");
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
			
			Process p = Runtime.getRuntime().exec(String.format("cmd /c cd %s && knife bootstrap %s -x %s -P %s -r %s --sudo", ConfigurationUtil.getKey("CHEF_WORKING_DIR"), ip, userName, password,finalRunList));
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
