package cn.vigor.API.manager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import cn.vigor.API.util.HttpTools;
import cn.vigor.API.util.JSONTools;
import cn.vigor.API.util.ManagerHandler;

public abstract class ServiceCommon extends ManagerCommon {

	protected String content;
	protected Map<String,ComponentCommon> components;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map<String, ComponentCommon> getComponents() {
		return components;
	}

	public void setComponents(Map<String, ComponentCommon> components) {
		this.components = components;
	}

	public ServiceCommon(String host, String port, String clustername,String name, String content,Map<String,ComponentCommon>components) {
		super(host, port, clustername, name);
		this.content = content;
		this.components = components;
	}

	protected String getEntity(String content, String st) {
		Map<String, Map> etity = new HashMap<String, Map>();
		Map<String, String> requestinfo = new HashMap<String, String>();
		requestinfo.put("context", content);
		etity.put("RequestInfo", requestinfo);
		Map<String, Map> serviceInfo = new HashMap<String, Map>();
		Map<String, String> state = new HashMap<String, String>();
		state.put("state", st);
		serviceInfo.put("ServiceInfo", state);
		etity.put("Body", serviceInfo);
		String entityString = JSONTools.toJson(etity);
		return entityString;
	}

	protected String getContent(String component, String state)
			throws ClientProtocolException, UnsupportedEncodingException,
			IOException {
		String url = ManagerHandler.returnServicesUrl(host, port, clustername,
				component);
		String entityString = getEntity(content, state);
		String ct = HttpTools.getInstance().putContent(url, entityString);
		return ct;
	}
}
