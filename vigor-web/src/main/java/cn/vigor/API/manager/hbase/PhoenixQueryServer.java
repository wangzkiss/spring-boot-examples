package cn.vigor.API.manager.hbase;


import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class PhoenixQueryServer extends ComponentCommon{


	
	public PhoenixQueryServer(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.HBASE_COMPONENT_PHOENIX_QUERY_SERVER,ComponentDic.SERVICE_HBASE);
	}

	public PhoenixQueryServer(String host, String port, String clustername,String targethost) {
		super(host, port, clustername, ComponentDic.HBASE_COMPONENT_PHOENIX_QUERY_SERVER, targethost, ComponentDic.SERVICE_HBASE);
		// TODO Auto-generated constructor stub
	}



}
