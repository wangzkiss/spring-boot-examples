package cn.vigor.modules.meta.entity;


import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;

import cn.vigor.common.persistence.DataEntity;
import cn.vigor.common.utils.excel.annotation.ExcelField;
import cn.vigor.modules.sys.entity.User;

/**
 * 数据权限Entity
 * @author kiss
 * @version 2016-05-31
 */
public class DataPermission extends DataEntity<DataPermission> {
	
	private static final long serialVersionUID = 1L;
	private String dataType;	 // 数据类型，所属哪个个表里的数据如 连接信息表  e_dbcon 存放字典对面对应  数据权限key值  
	private String dataId; // 数据id，关联数据表的id
	private String officeId;		 // 角色id
	private User user;		    // 关联用户表的id
	private String query;		// 是否有查询权限，Y 表示有 ；N 表示无
	private String update;		// 是否有修改权限，Y 表示有 ；N 表示无
	private String delete;		// 是否有删除权限，Y 表示有 ；N 表示无
	private String start;		// 是否有启动权限，Y 表示有 ；N 表示无  （集群表的信息才用到）
	private String stop;		// 是否有停止权限，Y 表示有 ；N 表示无  （集群表的信息才用到）
	private String audit;		// 是否有审核权限，Y 表示有 ；N 表示无  （特殊表的信息才用到）
	private String assign;		// 是否有分配权限，Y 表示有 ；N 表示无  （特殊表的信息才用到）
	private String share;		// 是否开启共享权限，Y 表示开 ；N 表示关  （特殊表的信息才用到）
	private String url; //记录跳转路径
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private int index=0;
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	private List<DataPermission> dataper = Lists.newArrayList();		// 子表列表
	

	public List<DataPermission> getDataper() {
		return dataper;
	}

	public void setDataper(List<DataPermission> dataper) {
		this.dataper = dataper;
	}

	public DataPermission() {
		super();
	}
	
    public void setAll(String str)
    {
    	str=str.toUpperCase();
    	this.query=str;
    	this.update=str;
    	this.delete=str;
    	this.start=str;
    	this.stop=str;
    	this.audit=str;
    	this.assign=str;
    	this.share=str;
    }
	public DataPermission(String id){
		super(id);
	}

	@Length(min=1, max=50, message="数据类型，所属哪个个表里的数据如 连接信息表  e_dbcon 存放字典对面对应  数据权限key值  长度必须介于 1 和 50 之间")
	@ExcelField(title="数据类型，所属哪个个表里的数据如 连接信息表  e_dbcon 存放字典对面对应  数据权限key值  ", align=2, sort=1)
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	@Length(min=0, max=10, message="角色id长度必须介于 0 和 10 之间")
	@ExcelField(title="角色id", fieldType=String.class, value="", align=2, sort=3)
	public String getOfficeId() {
		return officeId;
	}
	
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	@NotNull(message="关联用户表的id不能为空")
	@ExcelField(title="关联用户表的id", fieldType=User.class, value="user.name", align=2, sort=4)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=0, max=1, message="是否有查询权限，Y 表示有 ；N 表示无长度必须介于 0 和 1 之间")
	@ExcelField(title="是否有查询权限，Y 表示有 ；N 表示无", dictType="", align=2, sort=5)
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	@Length(min=0, max=1, message="是否有修改权限，Y 表示有 ；N 表示无长度必须介于 0 和 1 之间")
	@ExcelField(title="是否有修改权限，Y 表示有 ；N 表示无", dictType="", align=2, sort=6)
	public String getUpdate() {
		return update;
	}

	public void setUpdate(String update) {
		this.update = update;
	}
	
	@Length(min=0, max=1, message="是否有删除权限，Y 表示有 ；N 表示无长度必须介于 0 和 1 之间")
	@ExcelField(title="是否有删除权限，Y 表示有 ；N 表示无", dictType="", align=2, sort=7)
	public String getDelete() {
		return delete;
	}

	public void setDelete(String delete) {
		this.delete = delete;
	}
	
	@Length(min=0, max=1, message="是否有启动权限，Y 表示有 ；N 表示无  （集群表的信息才用到）长度必须介于 0 和 1 之间")
	@ExcelField(title="是否有启动权限，Y 表示有 ；N 表示无  （集群表的信息才用到）", dictType="", align=2, sort=8)
	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}
	
	@Length(min=0, max=1, message="是否有停止权限，Y 表示有 ；N 表示无  （集群表的信息才用到）长度必须介于 0 和 1 之间")
	@ExcelField(title="是否有停止权限，Y 表示有 ；N 表示无  （集群表的信息才用到）", dictType="", align=2, sort=9)
	public String getStop() {
		return stop;
	}

	public void setStop(String stop) {
		this.stop = stop;
	}
	
	@Length(min=0, max=1, message="是否有审核权限，Y 表示有 ；N 表示无  （特殊表的信息才用到）长度必须介于 0 和 1 之间")
	@ExcelField(title="是否有审核权限，Y 表示有 ；N 表示无  （特殊表的信息才用到）", dictType="", align=2, sort=10)
	public String getAudit() {
		return audit;
	}

	public void setAudit(String audit) {
		this.audit = audit;
	}
	
	@Length(min=0, max=1, message="是否有分配权限，Y 表示有 ；N 表示无  （特殊表的信息才用到）长度必须介于 0 和 1 之间")
	@ExcelField(title="是否有分配权限，Y 表示有 ；N 表示无  （特殊表的信息才用到）", dictType="", align=2, sort=11)
	public String getAssign() {
		return assign;
	}

	public void setAssign(String assign) {
		this.assign = assign;
	}
	
	@Length(min=0, max=1, message="是否开启共享权限，Y 表示开 ；N 表示关  （特殊表的信息才用到）长度必须介于 0 和 1 之间")
	@ExcelField(title="是否开启共享权限，Y 表示开 ；N 表示关  （特殊表的信息才用到）", dictType="", align=2, sort=12)
	public String getShare() {
		return share;
	}

	public void setShare(String share) {
		this.share = share;
	}
	
}