package org.kana.rockhopper.chef;

import java.io.Serializable;

public class SimpleNodePojo implements Serializable {
	
	private static final long serialVersionUID = 99531477107263111L;
	
	private String name;
	private String nodeRunList;
	private String ipaddress;	
	private boolean isMachineUp;
	private String hostName;
	
	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
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
	
	public void setIsMachineUp(boolean isMachineUp){
		this.isMachineUp = isMachineUp;
	}
	public boolean getIsMachineUp(){
		return this.isMachineUp;
	}
}

