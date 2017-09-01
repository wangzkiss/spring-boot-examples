package cn.vigor.API.manager.hdfs;


import java.util.HashMap;
import java.util.Map;

import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.manager.ServiceCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class Hdfs extends ServiceCommon{
	
	
	private static Map<String,ComponentCommon> initComponents(String host, String port, String clustername){
		Map<String,ComponentCommon> components = new HashMap<String,ComponentCommon>();
		components.put(ComponentDic.HDFS_COMPONENT_DATAENODE,new Datanode(host,port,clustername));
		components.put(ComponentDic.HDFS_COMPONENT_HDFS_CLIENT,new HdfsClient(host,port,clustername));
		components.put(ComponentDic.HDFS_COMPONENT_JOURNALNODE,new JournalNode(host,port,clustername));
		components.put(ComponentDic.HDFS_COMPONENT_NAMENODE,new Namenode(host,port,clustername));
		components.put(ComponentDic.HDFS_COMPONENT_NFS_GATEWAY,new NfsGateway(host,port,clustername));
		components.put(ComponentDic.HDFS_COMPONENT_SECONDARY_NAMENODE,new SecondNamenode(host,port,clustername));
		components.put(ComponentDic.HDFS_COMPONENT_ZKFC,new ZKFC(host,port,clustername));
		return components;
	}


	public Hdfs(String host, String port, String clustername,String content) {
		super(host, port, clustername, ComponentDic.SERVICE_HDFS, content, initComponents(host,port,clustername));
	}




}
