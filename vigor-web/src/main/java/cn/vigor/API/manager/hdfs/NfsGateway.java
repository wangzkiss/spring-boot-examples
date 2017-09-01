package cn.vigor.API.manager.hdfs;


import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class NfsGateway extends ComponentCommon {

	
	public NfsGateway(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.HDFS_COMPONENT_NFS_GATEWAY,ComponentDic.SERVICE_HDFS);
	}
	
	public NfsGateway(String host, String port, String clustername,
			String targethost) {
		super(host, port, clustername, ComponentDic.HDFS_COMPONENT_NFS_GATEWAY,
				targethost, ComponentDic.SERVICE_HDFS);
		// TODO Auto-generated constructor stub
	}

}
