package org.kana.rockhopper.chef;

import java.io.Serializable;

import org.json.JSONObject;

public class NodePojo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 99531477107263111L;
	
	private String name;
	private String chef_type;
	private String chef_environment;
	private String nodeRunList;
	private String uptime;
	private String hostname;
	private String fqdn;
	private String os_version;
	private String platform_version;
	private int cpu_total;
	private String platform;
	private String os;
	private String ipaddress;
	private String recipes;
	private String macaddress;
	private String virtualization;
	
	
	public String getVirtualization() {
		return virtualization;
	}
	public void setVirtualization(String virtualization) {
		this.virtualization = virtualization;
	}
	public String getMacaddress() {
		return macaddress;
	}
	public void setMacaddress(String macaddress) {
		this.macaddress = macaddress;
	}
	public String getChef_type() {
		return chef_type;
	}
	public void setChef_type(String chef_type) {
		this.chef_type = chef_type;
	}
	public String getUptime() {
		return uptime;
	}
	public void setUptime(String uptime) {
		this.uptime = uptime;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getFqdn() {
		return fqdn;
	}
	public void setFqdn(String fqdn) {
		this.fqdn = fqdn;
	}
	public String getOs_version() {
		return os_version;
	}
	public void setOs_version(String os_version) {
		this.os_version = os_version;
	}
	public String getPlatform_version() {
		return platform_version;
	}
	public void setPlatform_version(String platform_version) {
		this.platform_version = platform_version;
	}
	public int getCpu_total() {
		return cpu_total;
	}
	public void setCpu_total(int cpu_total) {
		this.cpu_total = cpu_total;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	public String getRecipes() {
		return recipes;
	}
	public void setRecipes(String recipes) {
		this.recipes = recipes;
	}
	public String getChef_environment() {
		return chef_environment;
	}
	public void setChef_environment(String chef_environment) {
		this.chef_environment = chef_environment;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNodeRunList() {
		return nodeRunList;
	}
	public void setNodeRunList(String nodeRunList) {
		this.nodeRunList = nodeRunList;
	}
	

}
