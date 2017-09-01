package cn.vigor.API.manager.hdfs;



import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class Namenode extends ComponentCommon{


	
	public Namenode(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.HDFS_COMPONENT_NAMENODE,ComponentDic.SERVICE_HDFS);
	}

	public Namenode(String host, String port, String clustername,String targethost) {
		super(host, port, clustername, ComponentDic.HDFS_COMPONENT_NAMENODE, targethost, ComponentDic.SERVICE_HDFS);
		// TODO Auto-generated constructor stub
	}


}
