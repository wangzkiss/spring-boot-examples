package cn.vigor.API.manager.spark;



import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class SparkJobhistoyServer extends ComponentCommon{


	public SparkJobhistoyServer(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.SPARK_COMPONENT_SPARK_JOBHISTORYSERVER,ComponentDic.SERVICE_SPARK);
	}

	public SparkJobhistoyServer(String host, String port, String clustername,String targethost) {
		super(host, port, clustername, ComponentDic.SPARK_COMPONENT_SPARK_JOBHISTORYSERVER, targethost, ComponentDic.SERVICE_SPARK);
		// TODO Auto-generated constructor stub
	}


}
