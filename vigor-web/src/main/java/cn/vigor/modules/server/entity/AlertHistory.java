package cn.vigor.modules.server.entity;

import cn.vigor.common.persistence.DataEntity;

/**
 * ambari节点历史通知表
 * @author 38342
 * @version v1.0
 *
 */
public class AlertHistory extends DataEntity<AlertHistory>{

	private static final long serialVersionUID = 1199842630539991291L;
	
	/**
	 * 主键
	 */
	private Integer alertId;
	
	/**
	 * 集群id
	 */
	private Integer clusterId;
	
	/**
	 * 
	 */
	private Integer alertDefinitionId;
	
	/**
	 * 服务名
	 */
	private String serviceName;
	
	/**
	 * 组件名
	 */
	private String componentName;
	
	/**
	 * 主机名
	 */
	private String hostName;
	
	/**
	 * 通知实例
	 */
	private String alertInstance;
	
	/**
	 * 通知时间
	 */
	private String alertTimestamp;
	
	/**
	 * 通知标签
	 */
	private String alertLabel;
	
	/**
	 * 通知状态
	 */
	private String alertState;
	
	/**
	 * 通知描述
	 */
	private String alertText;

	public Integer getAlertId() {
		return alertId;
	}

	public Integer getClusterId() {
		return clusterId;
	}

	public Integer getAlertDefinitionId() {
		return alertDefinitionId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getComponentName() {
		return componentName;
	}

	public String getHostName() {
		return hostName;
	}

	public String getAlertInstance() {
		return alertInstance;
	}

	public String getAlertTimestamp() {
		return alertTimestamp;
	}

	public String getAlertLabel() {
		return alertLabel;
	}

	public String getAlertState() {
		return alertState;
	}

	public String getAlertText() {
		return alertText;
	}

	public void setAlertId(Integer alertId) {
		this.alertId = alertId;
	}

	public void setClusterId(Integer clusterId) {
		this.clusterId = clusterId;
	}

	public void setAlertDefinitionId(Integer alertDefinitionId) {
		this.alertDefinitionId = alertDefinitionId;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public void setAlertInstance(String alertInstance) {
		this.alertInstance = alertInstance;
	}

	public void setAlertTimestamp(String alertTimestamp) {
		this.alertTimestamp = alertTimestamp;
	}

	public void setAlertLabel(String alertLabel) {
		this.alertLabel = alertLabel;
	}

	public void setAlertState(String alertState) {
		this.alertState = alertState;
	}

	public void setAlertText(String alertText) {
		this.alertText = alertText;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AlertHistory [alertId=");
		builder.append(alertId);
		builder.append(", clusterId=");
		builder.append(clusterId);
		builder.append(", alertDefinitionId=");
		builder.append(alertDefinitionId);
		builder.append(", serviceName=");
		builder.append(serviceName);
		builder.append(", componentName=");
		builder.append(componentName);
		builder.append(", hostName=");
		builder.append(hostName);
		builder.append(", alertInstance=");
		builder.append(alertInstance);
		builder.append(", alertTimestamp=");
		builder.append(alertTimestamp);
		builder.append(", alertLabel=");
		builder.append(alertLabel);
		builder.append(", alertState=");
		builder.append(alertState);
		builder.append(", alertText=");
		builder.append(alertText);
		builder.append("]");
		return builder.toString();
	}
}
