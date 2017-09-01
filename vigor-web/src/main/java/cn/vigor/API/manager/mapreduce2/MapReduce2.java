package cn.vigor.API.manager.mapreduce2;



import java.util.HashMap;
import java.util.Map;

import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.manager.ServiceCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class MapReduce2 extends ServiceCommon{

	private static Map<String,ComponentCommon> initComponents(String host, String port, String clustername){
		Map<String,ComponentCommon> components = new HashMap<String,ComponentCommon>();
		components.put(ComponentDic.MAPREDUCE2_COMPONENT_HISTORYSERVER,new HistoryServer(host,port,clustername));
		components.put(ComponentDic.MAPREDUCE2_COMPONENT_MAPREDUCE2_CLIENT,new MapReduce2Client(host,port,clustername));
		return components;
	}

	public MapReduce2(String host, String port, String clustername,String content) {
		super(host, port, clustername, ComponentDic.SERVICE_MAPREDUCE2, content, initComponents(host,port,clustername));
	}


}
