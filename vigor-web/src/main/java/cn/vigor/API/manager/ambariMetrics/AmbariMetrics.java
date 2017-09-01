package cn.vigor.API.manager.ambariMetrics;


import java.util.HashMap;
import java.util.Map;

import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.manager.ServiceCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class AmbariMetrics extends ServiceCommon{
	
	
	
	private static Map<String,ComponentCommon> initComponents(String host, String port, String clustername){
		Map<String,ComponentCommon> components = new HashMap<String,ComponentCommon>();
		components.put(ComponentDic.AMBARI_COMPONENT_METRICS_COLLECTOR, new MetricsCollector(host,port,clustername));
		components.put(ComponentDic.AMBARI_COMPONENT_METRICS_GRAFANA, new MetricsGrafana(host,port,clustername));
		components.put(ComponentDic.AMBARI_COMPONENT_METRICS_MONITOR, new MetricsMonitor(host,port,clustername));
		return components;
	}
	
	public AmbariMetrics(String host, String port, String clustername,String content) { 
		super(host, port, clustername, ComponentDic.SERVICE_AMBARI_METRICS, content, initComponents(host,port,clustername));
	}

	
	
	







}
