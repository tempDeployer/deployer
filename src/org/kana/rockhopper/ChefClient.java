package org.kana.rockhopper;

import org.kana.rockhopper.chefapi.ChefApiClient;

public class ChefClient {
	
	public static final String PEM_FILE_PATH = "C:/Users/gcoia/Work/centos69repo/.chef/gcoia.pem";
	public static final String CHEF_USER = "gcoia";
	public static final String CHEF_SERVER_URL = "https://centos69.kana-test.com/organizations/team";
	
	private static ChefApiClient cac = new ChefApiClient(CHEF_USER, PEM_FILE_PATH, CHEF_SERVER_URL);
	
	public String getCookbooks() {	
		return cac.get("/cookbooks").execute().getResponseBodyAsString();
	}
	
	public String getRoles() {
		return cac.get("/roles").execute().getResponseBodyAsString();
	}
	
	public String getEnvironments() {
		return cac.get("/environments").execute().getResponseBodyAsString();
	}
	
	public String getNodes() {
		return cac.get("/nodes").execute().getResponseBodyAsString();
	}
	
	public String getNode(String nodeName) {
		return cac.get("/nodes/"+nodeName).execute().getResponseBodyAsString();
	}

}
