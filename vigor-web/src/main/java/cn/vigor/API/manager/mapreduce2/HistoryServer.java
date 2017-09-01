package cn.vigor.API.manager.mapreduce2;



import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class HistoryServer extends ComponentCommon{

	public HistoryServer(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.MAPREDUCE2_COMPONENT_HISTORYSERVER,ComponentDic.SERVICE_MAPREDUCE2);
	}

	public HistoryServer(String host, String port, String clustername,String targethost) {
		super(host, port, clustername, ComponentDic.MAPREDUCE2_COMPONENT_HISTORYSERVER, targethost, ComponentDic.SERVICE_MAPREDUCE2);
		// TODO Auto-generated constructor stub
	}


}
