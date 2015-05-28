package org.kana.rockhopper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationUtil {

	private static String DEFAULT_FILE_PATH = "deployer.properties";
	
	private static Properties prop = new Properties();

	private static void loadVariables() {
		String filePath = System.getProperty("deployer.conf.file", DEFAULT_FILE_PATH);
		
		InputStream input = null;
		 
		try {
	 
			input = new FileInputStream(filePath);

			prop.load(input);
	 
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String getKey(String key) {
		return prop.getProperty(key);
	}

	static {
		loadVariables();
	}
}
