package org.kana.rockhopper.chefapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ChefBootstrapper {
	
	//private static final String WORKING_DIR = "C:\\Users\\gcoia\\Work\\centos69repo\\cookbooks"; //C:\Users\gcoia\Work\centos69repo\cookbooks
	private static final String WORKING_DIR = "C:/crepo";
	
	public void bootstrapLinuxNode(String ip, String userName, String password) {
		try {
			Process p = Runtime.getRuntime().exec(String.format("cmd /c cd %s && knife bootstrap %s --sudo -x %s -P %s", WORKING_DIR, ip, userName, password));
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
	
	public void bootstrapLinuxNode(String nodeName, String ip, String userName, String password) {
		try {

			Process p = Runtime.getRuntime().exec(String.format("cmd /c cd %s && knife bootstrap %s --sudo -x %s -P %s -N %s", WORKING_DIR, ip, userName, password, nodeName));
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
	
}
