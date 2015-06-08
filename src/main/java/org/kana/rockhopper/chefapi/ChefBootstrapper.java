package org.kana.rockhopper.chefapi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.SystemUtils;
import org.kana.rockhopper.ConfigurationUtil;

public class ChefBootstrapper {
	
	String finalRunList = "";
	
	private static final String COMMAND = "cd %s && knife bootstrap %s --sudo -x %s -P %s";
	private static final String COMMAND_WITH_RUNLIST = COMMAND+" -r %s";

	private void bootstrapLinuxNodeWithoutRunlist(String ip, String userName, String password, String logFilePath) throws IOException {
		String command = COMMAND;
			
		if(SystemUtils.IS_OS_WINDOWS) {
			command = "cmd /c "+command;
		}

		String cmd = String.format(
				command,
				ConfigurationUtil.getKey("deployer.chef.work"), ip, userName,
				password);
		bootStrapNode(cmd, logFilePath);

	}

	public void bootstrapLinuxNodeWithRunList(String ip, String userName,
			String password, String cookbooks, String roles, String logFilePath) throws IOException {
		
		String finalRunList = "";
		if ((cookbooks == null || cookbooks.isEmpty())
				&& (roles == null || roles.isEmpty())) {
			bootstrapLinuxNodeWithoutRunlist(ip, userName, password, logFilePath);
			return;
		}
		if (roles != null && roles.length() > 0) {
			finalRunList = buildArg(roles, "role");
		} 
		if (cookbooks != null && cookbooks.length() > 0) {
			if(finalRunList.length() > 0)
				finalRunList += ",";
			finalRunList += buildArg(cookbooks, "recipe");
		}

		String command = COMMAND_WITH_RUNLIST;
			
		if(SystemUtils.IS_OS_WINDOWS) {
			command = "cmd /c "+command;
		}


		String cmd = String.format(
				command,
				ConfigurationUtil.getKey("deployer.chef.work"), ip, userName,
				password, finalRunList);
		System.out.println(cmd);
		bootStrapNode(cmd, logFilePath);
	}

	private static String buildArg(String runlistItem, String prefix) {
		String finalRunList = "";
		System.out.println(runlistItem);
		String[] recipies = runlistItem.split(",");
		for (String recipe : recipies) {
			finalRunList += "\'" + prefix + "[" + recipe + "]\',";
		}
		System.out.println(finalRunList);
		finalRunList = finalRunList.substring(0, finalRunList.length() - 1);
		System.out.println(finalRunList);
		return finalRunList;
	}

	private static void bootStrapNode(String cmd, String logFilePath) throws IOException {
		BufferedWriter bw = null;
		try {
			File fout = new File(logFilePath);
			FileOutputStream fos = new FileOutputStream(fout);
			bw = new BufferedWriter(new OutputStreamWriter(fos));
			System.out.println("Executing cmd : " + cmd);
			bw.write(cmd);
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				bw.write(line);
				bw.newLine();
			}
		} catch (Exception e) {
			System.out.println("Exception : " + e);
		} finally {
			if (bw != null)
				bw.close();
		}
	}

}
