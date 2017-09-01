package cn.vigor.API.manager.zookeeper;


import java.util.HashMap;
import java.util.Map;

import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.manager.ServiceCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class Zookeeper extends ServiceCommon{

	private static Map<String,ComponentCommon> initComponents(String host, String port, String clustername){
		Map<String,ComponentCommon> components = new HashMap<String,ComponentCommon>();
		components.put(ComponentDic.ZOOKEEPER_COMPONENT_ZOOKEEPER_CLIENT,new ZookeeperClient(host,port,clustername));
		components.put(ComponentDic.ZOOKEEPER_COMPONENT_ZOOKEEPER_SERVER,new ZookeeperServer(host,port,clustername));
		return components;
	}

	public Zookeeper(String host, String port, String clustername,String content) {
		super(host, port, clustername, ComponentDic.SERVICE_ZOOKEEPER, content, initComponents(host,port,clustername));
	}


}
