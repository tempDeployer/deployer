package org.kana.rockhopper.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

import org.apache.commons.lang.SystemUtils;
import org.kana.rockhopper.ConfigurationUtil;
import org.kana.rockhopper.ConvertVmxToOvf;
import org.kana.rockhopper.Ec2UploadVmx;
import org.kana.rockhopper.KnifeVmxGeneration;
import org.kana.rockhopper.chefapi.ChefBootstrapper;
import org.kana.rockhopper.chefapi.Utils;
import org.kana.rockhopper.CUtils;

public class VmxService implements Runnable {

	private String ip;
	private String logFilePath;
	private String timeStamp;
	boolean deployEc2 = false;

	public VmxService(String timeStamp, String ip, boolean deployEc2) {
		this.ip = ip;
		this.timeStamp = timeStamp;
		this.logFilePath = ConfigurationUtil.getKey("deployer.logs.dir") + "/"
				+ timeStamp + ".log";
		this.deployEc2 = deployEc2;
	}

	public String getLogfilePath() {
		return this.logFilePath;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		BufferedWriter bw = null;
		try {
			File fos = new File(this.logFilePath);
			bw = new BufferedWriter(new FileWriter(fos));
			KnifeVmxGeneration knifevmx = new KnifeVmxGeneration();
			String cloneName = knifevmx.cloneVm(this.ip, bw, this.timeStamp);
			if (this.deployEc2) {
				System.out.println("Uncompressing " + cloneName);
				KnifeVmxGeneration.uncompressTGZ(
						ConfigurationUtil.getKey("deployer.vmx.dest"),
						cloneName, ".tgz", bw);
				ConvertVmxToOvf convertToOva = new ConvertVmxToOvf();
				String vmxDestPath = ConfigurationUtil
						.getKey("deployer.vmx.dest");
				String destDir = vmxDestPath + cloneName + "-ovf/";
				File file = new File(destDir);
				if (!file.exists()) {
					if (file.mkdir()) {
						System.out.println("Directory is created!");
					} else {
						System.out.println("Failed to create directory!");
					}
				}
				convertToOva.convert(vmxDestPath + cloneName + "/", cloneName,
						destDir, bw);
				String ec2Log = ConfigurationUtil.getKey("deployer.logs.dir")
						+ "/" + timeStamp + "-ec2.log";
				CUtils.removeDir(vmxDestPath + cloneName);
				System.out.println("Creating Ec2 instance and logging info in "
						+ ec2Log);
				bw.newLine();
				bw.write("Creating Ec2 instance and logging info in " + ec2Log);
				Runnable createEc2Ins = new Ec2UploadVmx(destDir, cloneName,
						ec2Log);
				new Thread(createEc2Ins).start();
			}
			bw.newLine();
			bw.write("Please download the new vmx from : http//"
					+ InetAddress.getLocalHost().getHostAddress() + "/vmx/"
					+ cloneName + ".tgz");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

	}
}
