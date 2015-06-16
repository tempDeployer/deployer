package org.kana.rockhopper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.SystemUtils;

public class Ec2UploadVmx implements Runnable {

	private static final String COMMAND = "ec2-import-instance %s -f VMDK -t r3.large -a x86_64 -b %s -o %s -w %s -p Linux --region us-west-1 -O %s -W %s";
	private String logFile;
	private String vmdkPath;
	private String sourceDisk;

	public Ec2UploadVmx(String vmdkPath, String cloneName, String logFile) {
		this.logFile = logFile;
		this.vmdkPath = vmdkPath;
		this.sourceDisk = vmdkPath + cloneName + "-disk1.vmdk";
	}

	public void uploadVmxToS3() throws IOException {
		BufferedWriter bw = null;
		try {
			String command = COMMAND;
			System.out.println("uploading to EC2 ");
			if (SystemUtils.IS_OS_WINDOWS) {
				command = "cmd /c " + command;
			}
			command = String.format(command, this.sourceDisk,
					ConfigurationUtil.getKey("deployer.aws.s3.foldername"),
					ConfigurationUtil.getKey("deployer.aws.accesskey"),
					ConfigurationUtil.getKey("deployer.aws.secretkey"),
					ConfigurationUtil.getKey("deployer.aws.accesskey"),
					ConfigurationUtil.getKey("deployer.aws.secretkey"));
			System.out.println(command);
			File fos = new File(this.logFile);
			bw = new BufferedWriter(new FileWriter(fos));

			Runtime r = Runtime.getRuntime();
			System.out.println(" Free mem : " + r.freeMemory());
			System.out.println(" Available procss : " + r.availableProcessors());
			r.gc();
			System.out.println(" Free mem : " + r.freeMemory());
			System.out
					.println(" Available procss : " + r.availableProcessors());
			Process proc = r.exec(command);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));
			Thread.sleep(10000);
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				bw.write(line);
				bw.newLine();
			}
			bw.newLine();
			bw.write("Done uploading to EC2 ");
			System.out.println("Done uploading to EC2 ");
			proc.destroy();
			bw.newLine();
			bw.write("Deleting " + vmdkPath);
			System.out.println("Deleting " + vmdkPath);
			CUtils.removeDir(vmdkPath);
		} catch (Exception e) {
			System.out.println(e);
			if (bw != null) {
				bw.write(e.getMessage());
				bw.write(e.toString());
				bw.newLine();
			}
		}
		finally{
			if(bw != null){
				bw.close();
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			uploadVmxToS3();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
