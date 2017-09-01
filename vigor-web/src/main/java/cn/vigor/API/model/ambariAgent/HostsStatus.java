package cn.vigor.API.model.ambariAgent;

public class HostsStatus {
	
	private String hostName;
	
	/**
	 * 有RUNNING ,SUCCESS
	 */
	private String status;
	private String log;
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
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
	
	
}
