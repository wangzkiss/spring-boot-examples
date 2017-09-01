package cn.vigor.API.manager.zookeeper;



import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class ZookeeperServer extends ComponentCommon{
	
	public ZookeeperServer(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.ZOOKEEPER_COMPONENT_ZOOKEEPER_SERVER,ComponentDic.SERVICE_ZOOKEEPER);
	}


	public ZookeeperServer(String host, String port, String clustername,String targethost) {
		super(host, port, clustername, ComponentDic.ZOOKEEPER_COMPONENT_ZOOKEEPER_SERVER, targethost, ComponentDic.SERVICE_ZOOKEEPER);
	}



}
