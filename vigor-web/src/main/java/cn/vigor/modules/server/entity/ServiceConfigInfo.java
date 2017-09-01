package cn.vigor.modules.server.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSONArray;

public class ServiceConfigInfo implements Serializable{

	private static final long serialVersionUID = -8806546683494266692L;
	
	private String href;
	
	private String cluster_name;
	
	private JSONArray configurations;
	
	private long createtime;
	
	private int group_id;
	
	private String group_name;
	
	private JSONArray hosts;
	
	private boolean is_cluster_compatible;
	
	private boolean is_current;
	
	private int service_config_version;
	
	private String service_config_version_note;
	
	private String service_name;
	
	private String stack_id;
	
	private String user;
	
	public String getHref() {
		return href;
	}

	public String getCluster_name() {
		return cluster_name;
	}

	public JSONArray getConfigurations() {
		return configurations;
	}

	public long getCreatetime() {
		return createtime;
	}

	public int getGroup_id() {
		return group_id;
	}

	public String getGroup_name() {
		return group_name;
	}

	public JSONArray getHosts() {
		return hosts;
	}

	public boolean isIs_cluster_compatible() {
		return is_cluster_compatible;
	}

	public boolean isIs_current() {
		return is_current;
	}

	public int getService_config_version() {
		return service_config_version;
	}

	public String getService_config_version_note() {
		return service_config_version_note;
	}

	public String getService_name() {
		return service_name;
	}

	public String getStack_id() {
		return stack_id;
	}

	public String getUser() {
		return user;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public void setCluster_name(String cluster_name) {
		this.cluster_name = cluster_name;
	}

	public void setConfigurations(JSONArray configurations) {
		this.configurations = configurations;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}

	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public void setHosts(JSONArray hosts) {
		this.hosts = hosts;
	}

	public void setIs_cluster_compatible(boolean is_cluster_compatible) {
		this.is_cluster_compatible = is_cluster_compatible;
	}

	public void setIs_current(boolean is_current) {
		this.is_current = is_current;
	}

	public void setService_config_version(int service_config_version) {
		this.service_config_version = service_config_version;
	}

	public void setService_config_version_note(String service_config_version_note) {
		this.service_config_version_note = service_config_version_note;
	}

	public void setService_name(String service_name) {
		this.service_name = service_name;
	}

	public void setStack_id(String stack_id) {
		this.stack_id = stack_id;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
