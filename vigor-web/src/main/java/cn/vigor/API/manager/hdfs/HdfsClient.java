package cn.vigor.API.manager.hdfs;


import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class HdfsClient extends ComponentCommon{

	public HdfsClient(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.HDFS_COMPONENT_HDFS_CLIENT,ComponentDic.SERVICE_HDFS);
	}

	public HdfsClient(String host, String port, String clustername, String targethost) {
		super(host, port, clustername, ComponentDic.HDFS_COMPONENT_HDFS_CLIENT, targethost, ComponentDic.SERVICE_HDFS);
		// TODO Auto-generated constructor stub
	}


}
