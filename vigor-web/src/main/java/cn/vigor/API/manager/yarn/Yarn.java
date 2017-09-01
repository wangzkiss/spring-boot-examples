package cn.vigor.API.manager.yarn;



import java.util.HashMap;
import java.util.Map;

import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.manager.ServiceCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class Yarn extends ServiceCommon{
	
	
	private static Map<String,ComponentCommon> initComponents(String host, String port, String clustername){
		Map<String,ComponentCommon> components = new HashMap<String,ComponentCommon>();
		components.put(ComponentDic.YARN_COMPONENT_APP_TIMELINE_SERVER,new AppTimelineServer(host,port,clustername));
		components.put(ComponentDic.YARN_COMPONENT_NODEMANAGER,new Nodemanager(host,port,clustername));
		components.put(ComponentDic.YARN_COMPONENT_RESOURCEMANAGER,new Resourcemanager(host,port,clustername));
		components.put(ComponentDic.YARN_COMPONENT_YARN_CLIENT,new YarnClient(host,port,clustername));
		return components;
	}

	public Yarn(String host, String port, String clustername, String content) {
		super(host, port, clustername, ComponentDic.SERVICE_YARN, content, initComponents(host,port,clustername));
	}


}
