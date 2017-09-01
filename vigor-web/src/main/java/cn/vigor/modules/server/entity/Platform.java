package cn.vigor.modules.server.entity;


import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;

import cn.vigor.common.persistence.DataEntity;
import cn.vigor.common.utils.excel.annotation.ExcelField;

/**
 * 集群信息Entity
 * @author kiss
 * @version 2016-06-30
 */
public class Platform extends DataEntity<Platform> {
	
	private static final long serialVersionUID = 1L;
	private String platformName;		// 集群名称
	private String platformIp;		// 主节点IP
	private Integer platformPort;		// 主节点端口
	private String platformDir;		// 集群所属目录
	private String platformUrl;		// 集群agent服务url
	private Integer platformState;		// 集群状态信息，0停止，1运行中，3不可操作 
	private Integer platformType;		// 集群类型，1集群，3基础平台信息
	private List<PlatformNode> platformNodeList = Lists.newArrayList();		// 子表列表
	
	public Platform() {
		super();
	}

	public Platform(String id){
		super(id);
	}

	@Length(min=1, max=50, message="集群名称长度必须介于 1 和 50 之间")
	@ExcelField(title="集群名称", align=2, sort=1)
	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	
	@Length(min=0, max=16, message="主节点IP长度必须介于 0 和 16 之间")
	@ExcelField(title="主节点IP", align=2, sort=2)
	public String getPlatformIp() {
		return platformIp;
	}

	public void setPlatformIp(String platformIp) {
		this.platformIp = platformIp;
	}
	
	@ExcelField(title="主节点端口", align=2, sort=3)
	public Integer getPlatformPort() {
		return platformPort;
	}

	public void setPlatformPort(Integer platformPort) {
		this.platformPort = platformPort;
	}
	
	@Length(min=0, max=500, message="集群所属目录长度必须介于 0 和 500 之间")
	@ExcelField(title="集群所属目录", align=2, sort=4)
	public String getPlatformDir() {
		return platformDir;
	}

	public void setPlatformDir(String platformDir) {
		this.platformDir = platformDir;
	}
	
	@Length(min=1, max=500, message="集群agent服务url长度必须介于 1 和 500 之间")
	@ExcelField(title="集群agent服务url", align=2, sort=5)
	public String getPlatformUrl() {
		return platformUrl;
	}

	public void setPlatformUrl(String platformUrl) {
		this.platformUrl = platformUrl;
	}
	
	@ExcelField(title="集群状态信息，0停止，1运行中，3不可操作 ", align=2, sort=6)
	public Integer getPlatformState() {
		return platformState;
	}

	public void setPlatformState(Integer platformState) {
		this.platformState = platformState;
	}
	
	@ExcelField(title="集群类型，1集群，3基础平台信息", align=2, sort=7)
	public Integer getPlatformType() {
		return platformType;
	}

	public void setPlatformType(Integer platformType) {
		this.platformType = platformType;
	}
	
	public List<PlatformNode> getPlatformNodeList() {
		return platformNodeList;
	}

	public void setPlatformNodeList(List<PlatformNode> platformNodeList) {
		this.platformNodeList = platformNodeList;
	}
}