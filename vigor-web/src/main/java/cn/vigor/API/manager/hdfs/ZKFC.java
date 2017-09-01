package cn.vigor.API.manager.hdfs;



import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class ZKFC extends ComponentCommon{

	
	
	public ZKFC(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.HDFS_COMPONENT_ZKFC,ComponentDic.SERVICE_HDFS);
	}
	
	
	public ZKFC(String host, String port, String clustername, String targethost) {
		super(host, port, clustername, ComponentDic.HDFS_COMPONENT_ZKFC, targethost, ComponentDic.SERVICE_HDFS);
		// TODO Auto-generated constructor stub
	}


}
