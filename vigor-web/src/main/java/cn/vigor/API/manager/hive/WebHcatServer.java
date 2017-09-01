package cn.vigor.API.manager.hive;



import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class WebHcatServer extends ComponentCommon{

	public WebHcatServer(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.HIVE_COMPONENT_WEBHCAT_SERVER,ComponentDic.SERVICE_HIVE);
	}

	public WebHcatServer(String host, String port, String clustername, String targethost) {
		super(host, port, clustername, ComponentDic.HIVE_COMPONENT_WEBHCAT_SERVER, targethost, ComponentDic.SERVICE_HIVE);
	}



}
