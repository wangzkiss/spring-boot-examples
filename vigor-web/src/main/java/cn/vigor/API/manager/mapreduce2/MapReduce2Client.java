package cn.vigor.API.manager.mapreduce2;



import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class MapReduce2Client extends ComponentCommon{

	public MapReduce2Client(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.MAPREDUCE2_COMPONENT_MAPREDUCE2_CLIENT,ComponentDic.SERVICE_MAPREDUCE2);
	}

	public MapReduce2Client(String host, String port, String clustername, String targethost) {
		super(host, port, clustername, ComponentDic.MAPREDUCE2_COMPONENT_MAPREDUCE2_CLIENT, targethost, ComponentDic.SERVICE_MAPREDUCE2);
		// TODO Auto-generated constructor stub
	}



}
