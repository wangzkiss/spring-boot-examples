package cn.vigor.API.manager.hive;



import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class HiveClient extends ComponentCommon{

	public HiveClient(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.HIVE_COMPONENT_HIVE_CLIENT,ComponentDic.SERVICE_HIVE);
	}

	public HiveClient(String host, String port, String clustername,String targethost) {
		super(host, port, clustername, ComponentDic.HIVE_COMPONENT_HIVE_CLIENT, targethost, ComponentDic.SERVICE_HIVE);
		// TODO Auto-generated constructor stub
	}



}
