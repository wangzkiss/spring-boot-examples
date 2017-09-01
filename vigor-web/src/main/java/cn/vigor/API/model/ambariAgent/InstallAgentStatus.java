package cn.vigor.API.model.ambariAgent;

public class InstallAgentStatus {

	/**
	 * 状态 初次请求返回为ok
	 */
	private String status;
	private String log;
	private String requestId;
	
	
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
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	
}
