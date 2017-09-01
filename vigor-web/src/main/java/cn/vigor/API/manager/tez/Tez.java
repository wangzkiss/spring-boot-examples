package cn.vigor.API.manager.tez;



import java.util.HashMap;
import java.util.Map;

import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.manager.ServiceCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class Tez extends ServiceCommon{
	
	
	private static Map<String,ComponentCommon> initComponents(String host, String port, String clustername){
		Map<String,ComponentCommon> components = new HashMap<String,ComponentCommon>();
		components.put(ComponentDic.TEZ_COMPONENT_TEZ_CLIENT,new TezClient(host,port,clustername));
		return components;
	}


	public Tez(String host, String port, String clustername,String content) {
		super(host, port, clustername, ComponentDic.SERVICE_TEZ, content, initComponents(host,port,clustername));
	}




}
