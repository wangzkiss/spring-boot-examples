package cn.vigor.modules.server.entity;

import java.io.Serializable;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class ServiceConfigTemp implements Serializable{

	private static final long serialVersionUID = -4802689544810156520L;
	
	private JSONObject Config = null;
	
	private String type;
	
	private String tag = "version1";
	
	private Integer version = 1;
	
	private JSONObject properties = new JSONObject();
	
	private JSONObject properties_attributes = new JSONObject();
	
	public ServiceConfigTemp(Map<String,Object> map){
		Config = new JSONObject(map);
	}

	public JSONObject getConfig() {
		return Config;
	}

	public String getType() {
		return type;
	}

	public String getTag() {
		return tag;
	}

	public Integer getVersion() {
		return version;
	}

	public JSONObject getProperties() {
		return properties;
	}

	public JSONObject getProperties_attributes() {
		return properties_attributes;
	}

	public void setConfig(JSONObject config) {
		Config = config;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public void setProperties(JSONObject properties) {
		this.properties = properties;
	}

	public void setProperties_attributes(JSONObject properties_attributes) {
		this.properties_attributes = properties_attributes;
	}
}
