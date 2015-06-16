package org.kana.rockhopper;

import org.apache.commons.lang.SystemUtils;

public class CUtils {

	private static  String RM_COMMAND;

	public static void removeDir(String dir){
		try {
			String command = String.format(RM_COMMAND, dir);
			System.out.println("Running Command " + command);
			System.out.println("Deleting" + dir);
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor();
			p.destroy();
		} catch (Exception e) {
			System.out.println(e);
		}

	}
	static{
		if (SystemUtils.IS_OS_LINUX) {
			RM_COMMAND = "rm -rf %s";
		}
		else{
			RM_COMMAND = "cmd /c del /F /Q %s";
		}
	}
}
