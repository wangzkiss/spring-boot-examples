package cn.vigor.API.manager.yarn;



import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class Resourcemanager extends ComponentCommon{

	public Resourcemanager(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.YARN_COMPONENT_RESOURCEMANAGER,ComponentDic.SERVICE_YARN);
	}

	public Resourcemanager(String host, String port, String clustername,String targethost) {
		super(host, port, clustername, ComponentDic.YARN_COMPONENT_RESOURCEMANAGER, targethost, ComponentDic.SERVICE_YARN);
		// TODO Auto-generated constructor stub
	}



}
