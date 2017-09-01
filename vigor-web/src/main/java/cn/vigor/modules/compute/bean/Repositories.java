package cn.vigor.modules.compute.bean;

import java.util.Date;

/**
 * 资源库实体类
 * @author Hewei
 *
 */
public class Repositories extends ModelObject{

	private int id;
	
	private String connName;
	
	private String ip;
	
	private String repoType;
	
	private String userName;
	
	private String userPwd;
	
	private String port;
	
	private int metaType;
	
	private String repoName;
	
	private String repoDesc;
	
	private Date updateTime;
	
	private Date createTime;
	
	private String createUser;
	
	private char delFlag;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getConnName() {
		return connName;
	}

	public void setConnName(String connName) {
		this.connName = connName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRepoType() {
		return repoType;
	}

	public void setRepoType(String repoType) {
		this.repoType = repoType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public int getMetaType() {
		return metaType;
	}

	public void setMetaType(int metaType) {
		this.metaType = metaType;
	}

	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public String getRepoDesc() {
		return repoDesc;
	}

	public void setRepoDesc(String repoDesc) {
		this.repoDesc = repoDesc;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	public char getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(char delFlag) {
		this.delFlag = delFlag;
	}
}