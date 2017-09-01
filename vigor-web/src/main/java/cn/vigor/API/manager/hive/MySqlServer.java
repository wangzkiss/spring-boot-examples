package cn.vigor.API.manager.hive;



import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class MySqlServer extends ComponentCommon{

	
	
	public MySqlServer(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.HIVE_COMPONENT_MYSQL_SERVER,ComponentDic.SERVICE_HIVE);
	}
	
	
	public MySqlServer(String host, String port, String clustername, String targethost) {
		super(host, port, clustername, ComponentDic.HIVE_COMPONENT_MYSQL_SERVER, targethost, ComponentDic.SERVICE_HIVE);
		// TODO Auto-generated constructor stub
	}


}
