package cn.vigor.API.manager.hbase;


import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;
public class HbaseClient extends ComponentCommon{

	
	
	public HbaseClient(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.HBASE_COMPONENT_HBASE_CLIENT,ComponentDic.SERVICE_HBASE);
	}


	public HbaseClient(String host, String port, String clustername,String targethost) {
		super(host, port, clustername, ComponentDic.HBASE_COMPONENT_HBASE_CLIENT, targethost, ComponentDic.SERVICE_HBASE);
		// TODO Auto-generated constructor stub
	}


}
