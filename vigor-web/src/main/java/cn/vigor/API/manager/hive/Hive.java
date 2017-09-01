package cn.vigor.API.manager.hive;



import java.util.HashMap;
import java.util.Map;

import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.manager.ServiceCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class Hive extends ServiceCommon{
	
	
	private static Map<String,ComponentCommon> initComponents(String host, String port, String clustername){
		Map<String,ComponentCommon> components = new HashMap<String,ComponentCommon>();
		components.put(ComponentDic.HIVE_COMPONENT_HCAT,new Hcat(host,port,clustername));
		components.put(ComponentDic.HIVE_COMPONENT_HIVE_CLIENT,new HiveClient(host,port,clustername));
		components.put(ComponentDic.HIVE_COMPONENT_HIVE_METASTORE,new HiveMetastore(host,port,clustername));
		components.put(ComponentDic.HIVE_COMPONENT_HIVE_SERVER,new HiveServer(host,port,clustername));
		components.put(ComponentDic.HIVE_COMPONENT_MYSQL_SERVER,new MySqlServer(host,port,clustername));
		components.put(ComponentDic.HIVE_COMPONENT_WEBHCAT_SERVER,new WebHcatServer(host,port,clustername));
		return components;
	}



	public Hive(String host, String port, String clustername,String content) {
		super(host, port, clustername, ComponentDic.SERVICE_HIVE, content, initComponents(host,port,clustername));
	}




}
