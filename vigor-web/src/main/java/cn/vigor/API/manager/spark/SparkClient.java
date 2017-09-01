package cn.vigor.API.manager.spark;



import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class SparkClient extends ComponentCommon{

	public SparkClient(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.SPARK_COMPONENT_SPARK_CLIENT,ComponentDic.SERVICE_SPARK);
	}
	

	public SparkClient(String host, String port, String clustername,String targethost) {
		super(host, port, clustername, ComponentDic.SPARK_COMPONENT_SPARK_CLIENT, targethost, ComponentDic.SERVICE_SPARK);
		// TODO Auto-generated constructor stub
	}



}
