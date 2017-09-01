package cn.vigor.API.manager.ambariMetrics;


import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class MetricsGrafana extends ComponentCommon{

	public MetricsGrafana(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.AMBARI_COMPONENT_METRICS_GRAFANA,ComponentDic.SERVICE_AMBARI_METRICS);
	}

	
	public MetricsGrafana(String host, String port, String clustername, String targethost) {
		super(host, port, clustername, ComponentDic.AMBARI_COMPONENT_METRICS_GRAFANA,targethost,ComponentDic.SERVICE_AMBARI_METRICS);
	}



}
