package cn.vigor.API.manager.hive;



import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class Hcat extends ComponentCommon{


	public Hcat(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.HIVE_COMPONENT_HCAT,ComponentDic.SERVICE_HIVE);
	}
	
	
	
	public Hcat(String host, String port, String clustername,String targethost) {
		super(host, port, clustername, ComponentDic.HIVE_COMPONENT_HCAT, targethost, ComponentDic.SERVICE_HIVE);
		// TODO Auto-generated constructor stub
	}



}
