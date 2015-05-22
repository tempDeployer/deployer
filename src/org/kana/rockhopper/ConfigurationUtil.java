package org.kana.rockhopper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class ConfigurationUtil {

	private static Map<String, String> systemVariables = new java.util.HashMap<String, String>();
	private static String filePath = "C:/deployerEnvironment.txt";

	private static void loadVariables() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String text = null;
			// repeat until all lines is read
			while ((text = reader.readLine()) != null) {
				if (text.startsWith("//") || text.isEmpty())
					continue;
				String[] pairs = text.split("=");
				if (pairs.length == 2) {
					systemVariables.put(pairs[0].trim(), pairs[1].trim());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getKey(String key) {
		return systemVariables.get(key);
	}

	static {
		loadVariables();
	}
}
