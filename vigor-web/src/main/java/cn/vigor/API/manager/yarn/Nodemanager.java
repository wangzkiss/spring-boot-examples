package cn.vigor.API.manager.yarn;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;
import cn.vigor.API.util.HttpTools;
import cn.vigor.API.util.JSONTools;
import cn.vigor.API.util.ManagerHandler;

public class Nodemanager extends ComponentCommon{

	public Nodemanager(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.YARN_COMPONENT_APP_TIMELINE_SERVER,ComponentDic.SERVICE_YARN);
	}

	public Nodemanager(String host, String port, String clustername, String targethost) {
		super(host, port, clustername, ComponentDic.YARN_COMPONENT_APP_TIMELINE_SERVER, targethost, ComponentDic.SERVICE_YARN);
		// TODO Auto-generated constructor stub
	}

	
	private String getDecommissionEntity(String content, String hosts) {
		Map etity = new HashMap();
		Map requestinfo = new HashMap();
		requestinfo.put("context", content);
		requestinfo.put("command", "DECOMMISSION");
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("slave_type", "NODEMANAGER");
		parameters.put("excluded_hosts", hosts);
		Map<String, String> operation_level = new HashMap<String, String>();
		operation_level.put("level", "HOST_COMPONENT");
		operation_level.put("cluster_name", this.getClustername());
		requestinfo.put("parameters", parameters);
		requestinfo.put("operation_level", operation_level);
		etity.put("RequestInfo", requestinfo);
		List resource = new ArrayList();
		Map resourcemap = new HashMap();
		resourcemap.put("service_name", "YARN");
		resourcemap.put("component_name", "RESOURCEMANAGER");
		resource.add(resourcemap);
		etity.put("Requests/resource_filters", resource);
		String entityString = JSONTools.toJson(etity);
		return entityString;
	}
	
	public String decommission(List<String> hosts,String content) throws ClientProtocolException, UnsupportedEncodingException, IOException{
		String url = ManagerHandler.returnDecommissionUrl(this.getHost(), this.getPort(), this.getClustername());
		StringBuffer sb = new StringBuffer();
			int size = hosts.size();
			for(int i=0;i<size;i++){
				String path = hosts.get(i);
				if(i==size-1){
					sb.append(path);
				}else{
					sb.append(path+",");
				}
			}
		String entityString = getDecommissionEntity(content,sb.toString());
		String ct = HttpTools.getInstance().postContent(url, entityString);
		return ct;
	}


}
