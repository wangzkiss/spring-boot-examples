package cn.vigor.API.manager.hdfs;



import cn.vigor.API.manager.ComponentCommon;
import cn.vigor.API.model.metrics.ComponentDic;

public class JournalNode extends ComponentCommon{

	public JournalNode(String host, String port, String clustername) {
		super(host, port, clustername, ComponentDic.HDFS_COMPONENT_JOURNALNODE,ComponentDic.SERVICE_HDFS);
	}

	public JournalNode(String host, String port, String clustername, String targethost) {
		super(host, port, clustername, ComponentDic.HDFS_COMPONENT_JOURNALNODE, targethost, ComponentDic.SERVICE_HDFS);
		// TODO Auto-generated constructor stub
	}


}
