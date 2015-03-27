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
	
	public int createRole(String roleJson) {
		return cac.post("/roles").body(roleJson).execute().getReturnCode();
	}
	
	public int deleteRole(String roleName) {
		return cac.delete("/roles/"+roleName).execute().getReturnCode();
	}
	
	public String getEnvironments() {
		return cac.get("/environments").execute().getResponseBodyAsString();
	}
	
	public String getEnvironment(String environmentName) {
		return cac.get("/environments/"+environmentName).execute().getResponseBodyAsString();
	}
	
	public int createEnvironment(String environmentJson) {
		return cac.post("/environments").body(environmentJson).execute().getReturnCode();
	}
	
	public int deleteEnvironment(String environmentName) {
		return cac.delete("/environments/"+environmentName).execute().getReturnCode();
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
	
	public int deleteNode(String nodeName) {
		return cac.delete("/nodes/"+nodeName).execute().getReturnCode();
	}
	
	//placeholder for bootstrap code
	public int bootstrapNode(String nodeName, String hostname, String userName, String password, String runList) {
		return 0;
	}

}
