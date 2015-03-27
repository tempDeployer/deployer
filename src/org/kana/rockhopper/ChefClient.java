package org.kana.rockhopper;

import org.kana.rockhopper.chefapi.ChefApiClient;

public class ChefClient {
	
	private static final String PEM_FILE_PATH = "C:/Users/gcoia/Work/centos69repo/.chef/gcoia.pem";
	private static final String CHEF_USER = "gcoia";
	private static final String CHEF_SERVER_URL = "https://centos69.kana-test.com/organizations/team";
	
	private static ChefApiClient cac = new ChefApiClient(CHEF_USER, PEM_FILE_PATH, CHEF_SERVER_URL);
	
	public String getCookbooks() {	
		return cac.get("/cookbooks").execute().getResponseBodyAsString();
	}
	
	public String getCookbook(String cookbookName) {
		return cac.get("/cookbooks/"+cookbookName).execute().getResponseBodyAsString();
	}
	
	public String getRoles() {
		return cac.get("/roles").execute().getResponseBodyAsString();
	}
	
	public String getRole(String roleName) {
		return cac.get("/roles/"+roleName).execute().getResponseBodyAsString();
	}
	
	public String getEnvironments() {
		return cac.get("/environments").execute().getResponseBodyAsString();
	}
	
	public String getEnvironment(String environmentName) {
		return cac.get("/environments/"+environmentName).execute().getResponseBodyAsString();
	}
	
	public String getNodes() {
		return cac.get("/nodes").execute().getResponseBodyAsString();
	}
	
	public String getNode(String nodeName) {
		return cac.get("/nodes/"+nodeName).execute().getResponseBodyAsString();
	}
	
	public String updateNode(String nodeName, String updateMessage) {
		return cac.put("/nodes/"+nodeName).body(updateMessage).execute().getResponseBodyAsString();
	}

}
