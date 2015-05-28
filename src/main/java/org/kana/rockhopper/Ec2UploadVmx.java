package org.kana.rockhopper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Ec2UploadVmx {
	

	public void uploadVmxToS3(String vmName, BufferedWriter bw) throws IOException {
		try {
			Process p = Runtime.getRuntime().exec(String.format("cmd /c cd %s && ec2-import-instance %s -f VMDK -t r3.large -ax86_64 -b %s -o %s -w %s -p Linux --region us-west-1", ConfigurationUtil.getKey("deployer.aws.ec2.toolsdir"), vmName,  ConfigurationUtil.getKey("deployer.aws.s3.foldername"),  ConfigurationUtil.getKey("deployer.aws.accesskey"),  ConfigurationUtil.getKey("deployer.aws.secretkey")));
			//p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				bw.write(line);
				bw.newLine();
			}
		} catch (Exception e) {

			System.out.println(e);
			bw.write(e.getMessage());
			bw.write(e.toString());
			bw.newLine();
		}
	}
	
}