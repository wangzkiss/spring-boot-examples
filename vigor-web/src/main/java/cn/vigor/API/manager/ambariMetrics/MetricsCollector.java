package cn.vigor.API.manager.ambariMetrics;


import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class MetricsCollector extends ComponentCommon{


	public MetricsCollector(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.AMBARI_COMPONENT_METRICS_COLLECTOR,ComponentDic.SERVICE_AMBARI_METRICS);
	}

	public MetricsCollector(String host, String port, String clustername,String targethost) {
		super(host, port, clustername, ComponentDic.AMBARI_COMPONENT_METRICS_COLLECTOR, targethost,ComponentDic.SERVICE_AMBARI_METRICS);
		// TODO Auto-generated constructor stub
	}



}
