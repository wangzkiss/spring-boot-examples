package cn.vigor.API.manager.yarn;



import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class AppTimelineServer extends ComponentCommon{

	public AppTimelineServer(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.YARN_COMPONENT_NODEMANAGER,ComponentDic.SERVICE_YARN);
	}

	public AppTimelineServer(String host, String port, String clustername, String targethost) {
		super(host, port, clustername, ComponentDic.YARN_COMPONENT_NODEMANAGER, targethost, ComponentDic.SERVICE_YARN);
		// TODO Auto-generated constructor stub
	}



}
