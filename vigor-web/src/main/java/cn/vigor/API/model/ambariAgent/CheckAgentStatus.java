package cn.vigor.API.model.ambariAgent;

import java.util.List;

public class CheckAgentStatus {

	/**
	 * 有RUNNING ,SUCCESS
	 */
	private String status;
	private String log;
	private List<HostsStatus> hostsStatus;
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public List<HostsStatus> getHostsStatus() {
		return hostsStatus;
	}
	public void setHostsStatus(List<HostsStatus> hostsStatus) {
		this.hostsStatus = hostsStatus;
	} 
	 
	
	
}
