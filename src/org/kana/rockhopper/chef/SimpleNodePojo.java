package org.kana.rockhopper.chef;

import java.io.Serializable;

public class SimpleNodePojo implements Serializable {
	
	private static final long serialVersionUID = 99531477107263111L;
	
	private String name;
	private String nodeRunList;
	private String ipaddress;	
	
	
	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
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

