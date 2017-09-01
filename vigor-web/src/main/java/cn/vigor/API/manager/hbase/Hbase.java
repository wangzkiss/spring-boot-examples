package cn.vigor.API.manager.hbase;


import java.util.HashMap;
import java.util.Map;

import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.manager.ServiceCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class Hbase extends ServiceCommon{
	
	
	
	private static Map<String,ComponentCommon> initComponents(String host, String port, String clustername){
		Map<String,ComponentCommon> components = new HashMap<String,ComponentCommon>();
		components.put(ComponentDic.HBASE_COMPONENT_HBASE_CLIENT, new HbaseClient(host,port,clustername));
		components.put(ComponentDic.HBASE_COMPONENT_HBASE_MASTER, new HbaseMaster(host,port,clustername));
		components.put(ComponentDic.HBASE_COMPONENT_HBASE_REGIONSERVER, new HbaseRegionserver(host,port,clustername));
		components.put(ComponentDic.HBASE_COMPONENT_PHOENIX_QUERY_SERVER, new PhoenixQueryServer(host,port,clustername));
		return components;
	}

	public Hbase(String host, String port, String clustername,String content) {
		super(host, port, clustername, ComponentDic.SERVICE_HBASE, content, initComponents(host,port,clustername));
	}




}
