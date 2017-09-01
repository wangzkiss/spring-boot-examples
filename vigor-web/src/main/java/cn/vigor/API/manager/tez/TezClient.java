package cn.vigor.API.manager.tez;



import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class TezClient extends ComponentCommon{

	public TezClient(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.TEZ_COMPONENT_TEZ_CLIENT,ComponentDic.SERVICE_TEZ);
	}

	public TezClient(String host, String port, String clustername,String targethost) {
		super(host, port, clustername, ComponentDic.TEZ_COMPONENT_TEZ_CLIENT, targethost, ComponentDic.SERVICE_TEZ);
	}



}
