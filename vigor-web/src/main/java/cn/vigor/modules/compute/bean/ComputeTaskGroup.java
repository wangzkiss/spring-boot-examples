package cn.vigor.modules.compute.bean;

import java.io.Serializable;
import java.util.Date;

/**
 *  任务分组实体类
 * @author Hewei
 *
 */
public class ComputeTaskGroup implements Serializable{
	
	private int groupId;
	
	private String groupName;
	
	private String createUser;
	
	private Date createTime;
	
	private int groupType;
	
	public int getGroupType() {
		return groupType;
	}
	public void setGroupType(int groupType) {
		this.groupType = groupType;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
