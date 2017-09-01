package cn.vigor.modules.server.entity;


import org.hibernate.validator.constraints.Length;

import cn.vigor.common.persistence.DataEntity;
import cn.vigor.common.utils.excel.annotation.ExcelField;

/**
 * 集群信息Entity
 * @author kiss
 * @version 2016-06-30
 */
public class PlatformNode extends DataEntity<PlatformNode> {
	
	private static final long serialVersionUID = 1L;
	private Platform platformId;		// 外键 父类
	private String nodeName;		// 主机名
	private String physicalId;		// 物理信息表id
	private Integer nodeType;		// 类型
	private Integer nodePort;		// 端口
	private String nodeDir;		// 根目录
	private String nodeIp;		// IP地址
	private String nodeUrl;		// agent地址
	private String nodeUser;		// 用户名
	private String nodePassword;		// 密码
	private Integer nodeState;		// 节点状态
	private Integer etlTaskCount;		// 集群任务数
	
	public PlatformNode() {
		super();
	}

	public PlatformNode(String id){
		super(id);
	}

	public PlatformNode(Platform platformId){
		this.platformId = platformId;
	}

	@Length(min=1, max=10, message="外键长度必须介于 1 和 10 之间")
	public Platform getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Platform platformId) {
		this.platformId = platformId;
	}
	
	@Length(min=1, max=50, message="主机名长度必须介于 1 和 50 之间")
	@ExcelField(title="主机名", align=2, sort=2)
	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	@Length(min=0, max=10, message="物理信息表id长度必须介于 0 和 10 之间")
	@ExcelField(title="物理信息表id", align=2, sort=3)
	public String getPhysicalId() {
		return physicalId;
	}

	public void setPhysicalId(String physicalId) {
		this.physicalId = physicalId;
	}
	
	@ExcelField(title="类型", align=2, sort=4)
	public Integer getNodeType() {
		return nodeType;
	}

	public void setNodeType(Integer nodeType) {
		this.nodeType = nodeType;
	}
	
	@ExcelField(title="端口", align=2, sort=5)
	public Integer getNodePort() {
		return nodePort;
	}

	public void setNodePort(Integer nodePort) {
		this.nodePort = nodePort;
	}
	
	@Length(min=0, max=500, message="根目录长度必须介于 0 和 500 之间")
	@ExcelField(title="根目录", align=2, sort=6)
	public String getNodeDir() {
		return nodeDir;
	}

	public void setNodeDir(String nodeDir) {
		this.nodeDir = nodeDir;
	}
	
	@Length(min=0, max=16, message="IP地址长度必须介于 0 和 16 之间")
	@ExcelField(title="IP地址", align=2, sort=7)
	public String getNodeIp() {
		return nodeIp;
	}

	public void setNodeIp(String nodeIp) {
		this.nodeIp = nodeIp;
	}
	
	@Length(min=0, max=500, message="agent地址长度必须介于 0 和 500 之间")
	@ExcelField(title="agent地址", align=2, sort=8)
	public String getNodeUrl() {
		return nodeUrl;
	}

	public void setNodeUrl(String nodeUrl) {
		this.nodeUrl = nodeUrl;
	}
	
	@Length(min=0, max=500, message="用户名长度必须介于 0 和 500 之间")
	@ExcelField(title="用户名", align=2, sort=9)
	public String getNodeUser() {
		return nodeUser;
	}

	public void setNodeUser(String nodeUser) {
		this.nodeUser = nodeUser;
	}
	
	@Length(min=0, max=500, message="密码长度必须介于 0 和 500 之间")
	@ExcelField(title="密码", align=2, sort=10)
	public String getNodePassword() {
		return nodePassword;
	}

	public void setNodePassword(String nodePassword) {
		this.nodePassword = nodePassword;
	}
	
	@ExcelField(title="节点状态", align=2, sort=11)
	public Integer getNodeState() {
		return nodeState;
	}

	public void setNodeState(Integer nodeState) {
		this.nodeState = nodeState;
	}
	
	@ExcelField(title="集群任务数", align=2, sort=12)
	public Integer getEtlTaskCount() {
		return etlTaskCount;
	}

	public void setEtlTaskCount(Integer etlTaskCount) {
		this.etlTaskCount = etlTaskCount;
	}
	
}