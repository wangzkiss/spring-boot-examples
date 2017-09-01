package cn.vigor.API.manager.zookeeper;

import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class ZookeeperClient extends ComponentCommon{

	public ZookeeperClient(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.ZOOKEEPER_COMPONENT_ZOOKEEPER_CLIENT,ComponentDic.SERVICE_ZOOKEEPER);
	}

	public ZookeeperClient(String host, String port, String clustername, String targethost) {
		super(host, port, clustername, ComponentDic.ZOOKEEPER_COMPONENT_ZOOKEEPER_CLIENT, targethost, ComponentDic.SERVICE_ZOOKEEPER);
	}



}
