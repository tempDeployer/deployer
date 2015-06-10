package org.kana.rockhopper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.kana.rockhopper.vsphere.VSphereException;
import org.kana.rockhopper.vsphere.VSphereService;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class KnifeVmxGeneration {

	private static final String DATASTORE_PATH = "/vmfs/volumes/LocalDS1";

	private static final String command1 = "tar zcf " + DATASTORE_PATH + "/%s.tar /vmfs/volumes/LocalDS1/%s\n";

	public String cloneVm(String ipAddress, BufferedWriter bw, String tStamp) {

		String hostName = null;
		String cloneName = null;

		try {
			hostName = VSphereService.getInstance().getVMNameByIPAddress(ipAddress);

			if (hostName == null || hostName.length() == 0) {
				throw new VSphereException("vmName doesn't exists ");
			}
			cloneName = "clone" + hostName + tStamp;

			System.out.println("Found VM with name " + hostName);

			VSphereService.getInstance().cloneVM(hostName, cloneName);

			this.compressAndDownloadVm(cloneName, bw);

			VSphereService.getInstance().removeVM(cloneName);

			System.out.println("Uncompressing " + cloneName);

			KnifeVmxGeneration.uncompressTGZ(ConfigurationUtil.getKey("deployer.vmx.dest"), cloneName, ".tgz", bw);

		} catch (IOException | VSphereException e) {
			e.printStackTrace();
		}

		return cloneName;
	}

	private void compressAndDownloadVm(String cloneName, BufferedWriter bw) {
		Session session = null;
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(ConfigurationUtil.getKey("deployer.esx.user"),
					ConfigurationUtil.getKey("deployer.esx.host"), 22);

			// TODO: You will probably want to use your client ssl certificate
			// instead of a password
			session.setPassword(ConfigurationUtil.getKey("deployer.esx.password"));

			// Not recommended - skips host check
			session.setConfig("StrictHostKeyChecking", "no");

			// session.connect(); - ten second timeout
			session.connect(10 * 1000);
			Channel channel = session.openChannel("exec");

			String command = "(cd " + DATASTORE_PATH + "; tar czf - " + cloneName + ")";

			((ChannelExec) channel).setCommand(command);

			java.io.FileOutputStream out = new java.io.FileOutputStream(ConfigurationUtil.getKey("deployer.vmx.dest")
					+ "/" + cloneName + ".tgz");
			channel.setOutputStream(out);
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);

			channel.connect();

			while (true) {
				System.out.println("compressing and downloading");
				bw.write("compressing and downloading");
				bw.newLine();
				if (channel.isClosed()) {
					System.out.println("exit-status: " + channel.getExitStatus());

					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}
			}
			System.out.println("Downloaded the compressed image");
			bw.write("Downloaded the compressed image : " + cloneName);
			out.close();
		} catch (Exception e) {
			System.out.println("Exception e : " + e);
		} finally {
			session.disconnect();
		}
	}

	private static void uncompressTGZ(String dirPath, String file, String extension, BufferedWriter bw)
			throws IOException {

		int BUFFER = 2048;
		FileInputStream fin = new FileInputStream(dirPath + file + extension);
		BufferedInputStream in = new BufferedInputStream(fin);
		GzipCompressorInputStream gzIn = new GzipCompressorInputStream(in);
		TarArchiveInputStream tarIn = new TarArchiveInputStream(gzIn);
		TarArchiveEntry entry = null;

		/** Read the tar entries using the getNextEntry method **/

		while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
			bw.write("Extracting: " + entry.getName());
			bw.newLine();
			System.out.println("Extracting: " + entry.getName());
			/** If the entry is a directory, create the directory. **/
			if (entry.isDirectory()) {
				File f = new File(dirPath + entry.getName());
				f.mkdirs();
			}
			/**
			 * 
			 * If the entry is a file,write the decompressed file to the disk
			 * and close destination stream.
			 **/

			else {
				int count;
				byte data[] = new byte[BUFFER];
				FileOutputStream fos = new FileOutputStream(dirPath + entry.getName());
				BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
				while ((count = tarIn.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, count);
				}
				dest.close();
			}

		}

		/** Close the input stream **/
		tarIn.close();
		bw.write("untar completed successfully!! " + dirPath + file + "got created");
		bw.newLine();
		System.out.println("untar completed successfully!!");
	}

	@SuppressWarnings("unused")
	private static void downloadFileToLocal(String cloneName) throws JSchException {

		Session session = null;
		try {

			JSch jsch = new JSch();
			session = jsch.getSession(ConfigurationUtil.getKey("deployer.esx.user"),
					ConfigurationUtil.getKey("deployer.esx.host"), 22);

			// TODO: You will probably want to use your client ssl certificate
			// instead of a password
			session.setPassword(ConfigurationUtil.getKey("deployer.esx.password"));

			// Not recommended - skips host check
			session.setConfig("StrictHostKeyChecking", "no");

			// session.connect(); - ten second timeout
			session.connect(10 * 1000);

			Channel channel = session.openChannel("shell");

			// TODO: You will probably want to use your own input stream,
			// instead of just reading a static string.
			String cmd = String.format(command1, cloneName, cloneName);
			//
			InputStream is = new ByteArrayInputStream(cmd.getBytes());
			channel.setInputStream(is);
			//
			// // Set the destination for the data sent back (from the server)
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(out);
			channel.setOutputStream(ps);
			channel.connect(15 * 1000);
			// // Wait thirty seconds for this demo to complete (ie: output to
			// be
			// // streamed to us).
			String console = "";
			while (!console.equals("~ # ")) {
				String[] lines = out.toString().split("\n");
				console = lines[lines.length - 1];
				Thread.sleep(30 * 1000);
			}
			// Disconnect (close connection, clean up system resources)
			channel.disconnect();
			copyFileToLocal(session, cloneName + ".tar");
		} catch (Exception e) {

		} finally {
			session.disconnect();
		}
	}

	private static void copyFileToLocal(Session session, String cloneName) throws SftpException, IOException {
		try {
			Channel channel2 = session.openChannel("sftp");
			channel2.connect();
			ChannelSftp sftpChannel = (ChannelSftp) channel2;
			byte[] buffer = new byte[1024];
			BufferedInputStream bis = new BufferedInputStream(sftpChannel.get(DATASTORE_PATH + "/" + cloneName));
			File newFile = new File(ConfigurationUtil.getKey("deployer.vmx.dest") + cloneName);
			OutputStream os = new FileOutputStream(newFile);
			BufferedOutputStream bos = new BufferedOutputStream(os);
			int readCount; // System.out.println("Getting: " + theLine);
			while ((readCount = bis.read(buffer)) > 0) {
				System.out.println("Writing: ");
				bos.write(buffer, 0, readCount);
			}
			bis.close();
			bos.close();
			// - See more at:
			// http://kodehelp.com/java-program-for-downloading-file-from-sftp-server/#sthash.T12bewTx.dpuf

			sftpChannel.exit();

			System.out.println("Done compression");

		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
