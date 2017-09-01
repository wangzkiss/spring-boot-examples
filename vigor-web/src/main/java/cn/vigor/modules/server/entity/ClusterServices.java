package cn.vigor.modules.server.entity;

import cn.vigor.common.persistence.DataEntity;
/**
 * 集群服务表
 * @author 38342
 * @version v1.0
 *
 */
public class ClusterServices extends DataEntity<ClusterServices>{

	private static final long serialVersionUID = 1476575174854599543L;
	
	private String serviceName;
	
	private Integer clusterId;
	
	private int serviceEnabled;

	public String getServiceName() {
		return serviceName;
	}

	public Integer getClusterId() {
		return clusterId;
	}

	public int getServiceEnabled() {
		return serviceEnabled;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setClusterId(Integer clusterId) {
		this.clusterId = clusterId;
	}

	public void setServiceEnabled(int serviceEnabled) {
		this.serviceEnabled = serviceEnabled;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ClusterServices [serviceName=");
		builder.append(serviceName);
		builder.append(", clusterId=");
		builder.append(clusterId);
		builder.append(", serviceEnabled=");
		builder.append(serviceEnabled);
		builder.append("]");
		return builder.toString();
	}
}
