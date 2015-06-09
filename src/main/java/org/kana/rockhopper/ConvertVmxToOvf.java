package org.kana.rockhopper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.SystemUtils;

public class ConvertVmxToOvf {
	private static final String WINDOWS_COMMAND = "cmd /c cd %s && %s";
	private static final String COMMAND = "ovftool %s %s.ovf";
	
	public void convert(String srcDir, String vmxName, String outputDir, BufferedWriter bw) throws IOException {
		
		try {
			String command = String.format(COMMAND, ConfigurationUtil.getKey("deployer.ovf.dir"), srcDir + vmxName +".vmx", outputDir + vmxName + ".ovf");
			
			if(SystemUtils.IS_OS_WINDOWS) {
				command = String.format(WINDOWS_COMMAND, ConfigurationUtil.getKey("deployer.ovf.dir"), command);
			}
			
			Process p = Runtime.getRuntime().exec(command);
			//p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				bw.write(line);
				bw.newLine();
			}
		} catch (Exception e) {
			bw.write(e.getMessage());
			bw.newLine();
			bw.write(e.toString());
			bw.newLine();
		}
		finally{
			bw.close();
		}
	}	
}

