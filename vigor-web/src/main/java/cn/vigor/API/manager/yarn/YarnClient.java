package cn.vigor.API.manager.yarn;


import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class YarnClient extends ComponentCommon{

	public YarnClient(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.YARN_COMPONENT_YARN_CLIENT,ComponentDic.SERVICE_YARN);
	}

	public YarnClient(String host, String port, String clustername, String targethost) {
		super(host, port, clustername, ComponentDic.YARN_COMPONENT_YARN_CLIENT, targethost, ComponentDic.SERVICE_YARN);
		// TODO Auto-generated constructor stub
	}



}
