package cn.vigor.modules.meta.entity;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;

import cn.vigor.common.persistence.DataEntity;
import cn.vigor.common.utils.excel.annotation.ExcelField;
import cn.vigor.modules.meta.util.MetaUtil;
import cn.vigor.modules.sys.entity.User;
import cn.vigor.modules.sys.utils.UserUtils;

/**
 * 数据库连接信息Entity
 * @author kiss
 * @version 2016-05-13
 */
public class MetaRepo extends DataEntity<MetaRepo> {
	
	private static final long serialVersionUID = 1L;
	private String connName;		// 连接名称
	private String ip;		// IP地址
	/**
 	1	flume_exec
	2	ftp文件
	3	hdfs
	4	hbase
	5	hive
	6	mysql
	7	oracle
	8	mongdb
	9	kafka
	10	sqlserver 
	11 trafodion
	*/
	private int repoType;		// 资源类型mysql

	private String userName;		// 用户名
	private String userPwd;		// 密码
	private String port;		// 端口
	private Integer metaType;		// 数据存放方式
	private String repoName;		// 目录/数据库
	private String repoDesc;		// 描述
	private List<MetaResult> metaResultList = Lists.newArrayList();		// 子表列表
	private List<MetaSource> metaSourceList = Lists.newArrayList();		// 子表列表
	private List<MetaStore> metaStoreList = Lists.newArrayList();		// 子表列表
	private List<MetaTable> tables = Lists.newArrayList();
	private String typeString;
	private String repoInstance;//oracle下数据库实例,即数据库名
	
	
    public MetaRepo(String connName, String ip, int repoType, String userName, String userPwd, String port,
			Integer metaType, String repoName, String repoDesc) {
		super();
		this.connName = connName;
		this.ip = ip;
		this.repoType = repoType;
		this.userName = userName;
		this.userPwd = userPwd;
		this.port = port;
		this.metaType = metaType;
		this.repoName = repoName;
		this.repoDesc = repoDesc;
	}
	public String getTypeString()
    {
        typeString=MetaUtil.getTypeString(repoType);
        return typeString;
    }
	public List<MetaTable> getTables() {
		return tables;
	}

	public void setTables(List<MetaTable> tables) {
		this.tables = tables;
	}

	/**
	 * 插入之前执行方法，需要手动调用
	 */
	@Override
	public void preInsert() {
		
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())){
			this.updateBy = user;
			this.createBy = user;
		}
		this.updateDate = new Date();
		this.createDate = this.updateDate;
		
	}

	public MetaRepo() {
		super();
	}

	public MetaRepo(String id){
		this.setId(id);
	}

	@Length(min=1, max=64, message="连接名称长度必须介于 1 和 64 之间")
	@ExcelField(title="连接名称", align=2, sort=1)
	public String getConnName() {
		return connName;
	}

	public void setConnName(String connName) {
		this.connName = connName;
	}
	
	@Length(min=1, message="IP地址长度必须介于 1 和 15之间")
	@ExcelField(title="IP地址", align=2, sort=2)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@NotNull(message="资源类型不能为空")
	@ExcelField(title="资源类型", dictType="repo_type", align=2, sort=3)
	public int getRepoType() {
		return repoType;
	}
	public void setRepoType(int repoType) {
		this.repoType = repoType;
	}
	
	@Length(min=1, max=25, message="用户名长度必须介于 1 和 64 之间")
	@ExcelField(title="用户名", align=2, sort=4)
	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	//@Length(min=0, max=25, message="密码长度必须介于 0和25 之间")
	@ExcelField(title="密码", align=2, sort=5)
	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	
	@NotNull(message="端口不能为空")
	@ExcelField(title="端口", align=2, sort=6)
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
	
	@ExcelField(title="数据存放方式", dictType="meta_type", align=2, sort=7)
	public Integer getMetaType() {
		return metaType;
	}

	public void setMetaType(Integer metaType) {
		this.metaType = metaType;
	}
	
	@Length(min=1, max=64, message="目录/数据库长度必须介于 1 和 64 之间")
	@ExcelField(title="目录/数据库", align=2, sort=8)
	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}
	
	@Length(min=0, max=644, message="描述长度必须介于 0 和 644 之间")
	@ExcelField(title="描述", align=2, sort=9)
	public String getRepoDesc() {
		return repoDesc;
	}

	public void setRepoDesc(String repoDesc) {
		this.repoDesc = repoDesc;
	}
	
	public List<MetaResult> getMetaResultList() {
		return metaResultList;
	}

	public void setMetaResultList(List<MetaResult> metaResultList) {
		this.metaResultList = metaResultList;
	}
	public List<MetaSource> getMetaSourceList() {
		return metaSourceList;
	}

	public void setMetaSourceList(List<MetaSource> metaSourceList) {
		this.metaSourceList = metaSourceList;
	}
	public List<MetaStore> getMetaStoreList() {
		return metaStoreList;
	}

	public void setMetaStoreList(List<MetaStore> metaStoreList) {
		this.metaStoreList = metaStoreList;
	}
    public String getRepoInstance()
    {
        return repoInstance;
    }
    public void setRepoInstance(String repoInstance)
    {
        this.repoInstance = repoInstance;
    }
}