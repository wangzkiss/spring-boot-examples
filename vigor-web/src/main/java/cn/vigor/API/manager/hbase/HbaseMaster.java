package cn.vigor.API.manager.hbase;


import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class HbaseMaster extends ComponentCommon{

	public HbaseMaster(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.HBASE_COMPONENT_HBASE_MASTER,ComponentDic.SERVICE_HBASE);
	}

	public HbaseMaster(String host, String port, String clustername, String targethost) {
		super(host, port, clustername, ComponentDic.HBASE_COMPONENT_HBASE_MASTER, targethost, ComponentDic.SERVICE_HBASE);
		// TODO Auto-generated constructor stub
	}



}
