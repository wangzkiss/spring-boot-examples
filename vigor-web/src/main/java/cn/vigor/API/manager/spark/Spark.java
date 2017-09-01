package cn.vigor.API.manager.spark;


import java.util.HashMap;
import java.util.Map;

import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.manager.ServiceCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class Spark extends ServiceCommon{
	
	private static Map<String,ComponentCommon> initComponents(String host, String port, String clustername){
		Map<String,ComponentCommon> components = new HashMap<String,ComponentCommon>();
		components.put(ComponentDic.SPARK_COMPONENT_SPARK_CLIENT,new SparkClient(host,port,clustername));
		components.put(ComponentDic.SPARK_COMPONENT_SPARK_JOBHISTORYSERVER,new SparkJobhistoyServer(host,port,clustername));
		components.put(ComponentDic.SPARK_COMPONENT_SPARK_THRIFTSERVER,new SparkThriftServer(host,port,clustername));
		return components;
	}
	

	public Spark(String host, String port, String clustername, String content) {
		super(host, port, clustername, ComponentDic.SERVICE_SPARK, content, initComponents(host,port,clustername));
	}




}
