package org.kana.rockhopper.chefapi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;

import org.kana.rockhopper.ConfigurationUtil;

public class ChefBootstrapper {

	private void bootstrapLinuxNode(String ip, String userName, String password) throws IOException {
		String cmd = String.format(
				"cmd /c cd %s && knife bootstrap %s --sudo -x %s -P %s",
				ConfigurationUtil.getKey("deployer.chef.work"), ip, userName,
				password);
		bootStrapNode(cmd);

	}

	public void bootstrapLinuxNodeWithRunList(String ip, String userName,
			String password, String runlist, String runlistItem) throws IOException {
		String finalRunList = "";
		if ((runlist == null || runlist.isEmpty())
				|| (runlistItem == null || runlistItem.isEmpty())) {
			bootstrapLinuxNode(ip, userName, password);
			return;
		}

		if (runlist.equalsIgnoreCase("Cookbooks")) {
			finalRunList = buildArg(runlistItem, "recipe");
		} else if (runlist.equalsIgnoreCase("Roles")) {
			finalRunList = buildArg(runlistItem, "role");
		}
		String cmd = String.format(
				"cmd /c cd %s && knife bootstrap %s -x %s -P %s -r %s --sudo",
				ConfigurationUtil.getKey("deployer.chef.work"), ip, userName,
				password, finalRunList);
		System.out.println(cmd);
		bootStrapNode(cmd);
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

	private static void bootStrapNode(String cmd) throws IOException {
		BufferedWriter bw = null;
		try {
			String tStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
					.format(new java.util.Date());
			File fout = new File(ConfigurationUtil.getKey("deployer.logs.dir")
					+ "/" + tStamp + ".txt");
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
