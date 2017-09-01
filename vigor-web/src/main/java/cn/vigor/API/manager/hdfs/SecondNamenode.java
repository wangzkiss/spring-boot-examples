package cn.vigor.API.manager.hdfs;



import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class SecondNamenode extends ComponentCommon{

	
	public SecondNamenode(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.HDFS_COMPONENT_SECONDARY_NAMENODE,ComponentDic.SERVICE_HDFS);
	}
	


	public SecondNamenode(String host, String port, String clustername, String targethost) {
		super(host, port, clustername, ComponentDic.HDFS_COMPONENT_SECONDARY_NAMENODE, targethost, ComponentDic.SERVICE_HDFS);
		// TODO Auto-generated constructor stub
	}

}
