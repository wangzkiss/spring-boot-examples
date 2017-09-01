package cn.vigor.modules.compute.bean;

import java.util.Date;
import java.util.List;

/**
 * 数据源实体类
 * @author Hewei
 *
 */
public class ComputeMetaSource extends ModelObject{
	
	private int id;
	
	private int repoId;
	
	private String sourceName;
	
	private int sourceType;
	
	private String delimiter;
	
	private String sourceDesc;
	
	private String sourceFile;
	
	private Date createTime;
	
	private Date updateTime;
	
	private String createUser;
	
	private char delFlag;
	
	private List<MetaSourceField> metaSourceField ;
	
	private int loginUserId;
	
	public int getLoginUserId() {
		return loginUserId;
	}

	public void setLoginUserId(int loginUserId) {
		this.loginUserId = loginUserId;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRepoId() {
		return repoId;
	}

	public void setRepoId(int repoId) {
		this.repoId = repoId;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public int getSourceType() {
		return sourceType;
	}

	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getSourceDesc() {
		return sourceDesc;
	}

	public void setSourceDesc(String sourceDesc) {
		this.sourceDesc = sourceDesc;
	}

	public String getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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

	public List<MetaSourceField> getMetaSourceField() {
		return metaSourceField;
	}

	public void setMetaSourceField(List<MetaSourceField> metaSourceField) {
		this.metaSourceField = metaSourceField;
	}
}
