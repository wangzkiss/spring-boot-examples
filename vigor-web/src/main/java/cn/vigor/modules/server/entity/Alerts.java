package cn.vigor.modules.server.entity;

import cn.vigor.common.persistence.DataEntity;

public class Alerts extends DataEntity<Alerts>{

	private static final long serialVersionUID = 1350222778210751059L;
	
	private String cluster_name;
	
	private String component_name;
	
	private Integer definition_id;
	
	private String definition_name;
	
	private String host_name;
	
	private String instance;
	
	private String label;

	private Long latest_timestamp;
	
	private String maintenance_state;
	
	private Long original_timestamp;
	
	private String scope;
	
	private String service_name;
	
	private String state;
	
	private String text;
	
	private String detailUrl;

	public String getCluster_name() {
		return cluster_name;
	}

	public String getComponent_name() {
		return component_name;
	}

	public Integer getDefinition_id() {
		return definition_id;
	}

	public String getDefinition_name() {
		return definition_name;
	}

	public String getHost_name() {
		return host_name;
	}

	public String getInstance() {
		return instance;
	}

	public String getLabel() {
		return label;
	}

	public Long getLatest_timestamp() {
		return latest_timestamp;
	}

	public String getMaintenance_state() {
		return maintenance_state;
	}

	public Long getOriginal_timestamp() {
		return original_timestamp;
	}

	public String getScope() {
		return scope;
	}

	public String getService_name() {
		return service_name;
	}

	public String getState() {
		return state;
	}

	public String getText() {
		return text;
	}

	public void setCluster_name(String cluster_name) {
		this.cluster_name = cluster_name;
	}

	public void setComponent_name(String component_name) {
		this.component_name = component_name;
	}

	public void setDefinition_id(Integer definition_id) {
		this.definition_id = definition_id;
	}

	public void setDefinition_name(String definition_name) {
		this.definition_name = definition_name;
	}

	public void setHost_name(String host_name) {
		this.host_name = host_name;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setLatest_timestamp(Long latest_timestamp) {
		this.latest_timestamp = latest_timestamp;
	}

	public void setMaintenance_state(String maintenance_state) {
		this.maintenance_state = maintenance_state;
	}

	public void setOriginal_timestamp(Long original_timestamp) {
		this.original_timestamp = original_timestamp;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public void setService_name(String service_name) {
		this.service_name = service_name;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Alerts [cluster_name=");
		builder.append(cluster_name);
		builder.append(", component_name=");
		builder.append(component_name);
		builder.append(", definition_id=");
		builder.append(definition_id);
		builder.append(", definition_name=");
		builder.append(definition_name);
		builder.append(", host_name=");
		builder.append(host_name);
		builder.append(", instance=");
		builder.append(instance);
		builder.append(", label=");
		builder.append(label);
		builder.append(", latest_timestamp=");
		builder.append(latest_timestamp);
		builder.append(", maintenance_state=");
		builder.append(maintenance_state);
		builder.append(", original_timestamp=");
		builder.append(original_timestamp);
		builder.append(", scope=");
		builder.append(scope);
		builder.append(", service_name=");
		builder.append(service_name);
		builder.append(", state=");
		builder.append(state);
		builder.append(", text=");
		builder.append(text);
		builder.append(", detailUrl=");
		builder.append(detailUrl);
		builder.append("]");
		return builder.toString();
	}

	public String getDetailUrl() {
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}
}
