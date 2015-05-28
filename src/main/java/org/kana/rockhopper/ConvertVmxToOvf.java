package org.kana.rockhopper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConvertVmxToOvf {
	
	public void convert(String srcDir, String vmxName, String outputDir, BufferedWriter bw) throws IOException {
		try {
			Process p = Runtime.getRuntime().exec(String.format("cmd /c cd %s && ovftool.exe %s %s.ovf", ConfigurationUtil.getKey("deployer.ovf.dir"), srcDir + vmxName +".vmx", outputDir + vmxName + ".ovf"));
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
